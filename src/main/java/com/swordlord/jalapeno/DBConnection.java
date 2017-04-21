/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 **
 ** This program is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Affero General Public License as published by the Free
 ** Software Foundation, either version 3 of the License, or (at your option)
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful, but WITHOUT
 ** ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 ** FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for
 ** more details.
 **
 ** You should have received a copy of the GNU Affero General Public License along
 ** with this program. If not, see <http://www.gnu.org/licenses/>.
 **
 **-----------------------------------------------------------------------------
 **
 ** $Id: DBConnection.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno;

// IMPORT
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swordlord.gozer.datatypeformat.DataTypeHelper;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.DeleteDenyException;
import org.apache.cayenne.FaultFailureException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataContextFactory;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.DataRowStore;
import org.apache.cayenne.access.ObjectStore;
import org.apache.cayenne.configuration.Configuration;
import org.apache.cayenne.configuration.DefaultConfiguration;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.validation.BeanValidationFailure;
import org.apache.cayenne.validation.ValidationException;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import com.swordlord.jalapeno.access.DataContextEx;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DBConnection
{
	protected Log LOG = LogFactory.getLog(DBConnection.class);

	private static DBConnection _instance;
	private static Configuration _configuration;
	public static String DATA_DOMAIN_REPOSITORY = "repository";
	public static String DATA_MAP_REPOSITORY = "repositoryMap";
	private List<String> listEntityObjects = new ArrayList<String>();

	public List<String> getListEntityObjects()
	{
		return listEntityObjects;
	}

	public static DBConnection instance()
	{
		if (_instance == null)
		{
			_instance = new DBConnection();
            Configuration.initializeSharedConfiguration(new DefaultConfiguration(UserPrefs.getCayenneConfig()));
			_configuration = Configuration.getSharedConfiguration();

			// Check database availability and structure
			DBGenerator dbGen = new DBGenerator();
			if (!dbGen.isDBAvailableAndConfigured(DataContext.createDataContext(DATA_DOMAIN_REPOSITORY)))
			{

			}
		}

		return _instance;
	}

	private boolean _bInitialisationHappened;

	/*
	 * An application can bind a DataContext to a current execution thread.
	 * Later on the code that needs DB access can retrieve this DataContext
	 * without making any assumptions about the environment. This approach is
	 * universal and works in all types of applications (web, standalone, etc.).
	 */
	public ObjectContext getObjectContext()
	{
		// http://cayenne.apache.org/doc/objectcontext-memory-management.html
		// To ensure that weak references are not used, create a DataContext
		// manually, passing a regular HashMap to the ObjectStore constructor.

		// The following code is therefore taken from Cayenne 3.0 bleeding
		// edge...

		DataDomain dataDomain = Configuration.getSharedConfiguration().getDomain();

		buildListEntityObjects(dataDomain);

		// for new dataRowStores use the same name for all stores
		// it makes it easier to track the event subject
		DataRowStore snapshotCache = new DataRowStore(dataDomain.getName(), dataDomain.getProperties(), dataDomain.getEventManager());

		DataContext context;

		DataContextFactory dataContextFactory = dataDomain.getDataContextFactory();
		if (null == dataContextFactory)
		{
			context = new DataContext(dataDomain, new ObjectStore(snapshotCache, new HashMap<Object, Persistent>()));
		} else
		{
			context = dataContextFactory.createDataContext(dataDomain, new ObjectStore(snapshotCache, new HashMap<Object, Persistent>()));
		}
		context.setValidatingObjectsOnCommit(dataDomain.isValidatingObjectsOnCommit());
		return context;

		// originally: DataContext dc = DataContext.createDataContext();
		// experimental: ObjectContext objectContext =
		// DataContext.getThreadDataContext();
	}

	/*
	 * An application can bind a DataContext to a current execution thread.
	 * Later on the code that needs DB access can retrieve this DataContext
	 * without making any assumptions about the environment. This approach is
	 * universal and works in all types of applications (web, standalone, etc.).
	 */
	public ObjectContext getObjectContext(DataContainer dc)
	{
		if (dc == null)
			return this.getObjectContext();
		else
		{
			DataDomain dataDomain = Configuration.getSharedConfiguration().getDomain();

			buildListEntityObjects(dataDomain);

			// for new dataRowStores use the same name for all stores
			// it makes it easier to track the event subject
			DataRowStore snapshotCache = new DataRowStore(dataDomain.getName(), dataDomain.getProperties(), dataDomain.getEventManager());

			DataContextEx context = null;

			DataContextFactory dataContextFactory = dataDomain.getDataContextFactory();
			if (null == dataContextFactory)
			{
				context = new DataContextEx(dataDomain, new ObjectStore(snapshotCache, new HashMap()), dc);
				context.setValidatingObjectsOnCommit(dataDomain.isValidatingObjectsOnCommit());
			} else
			{
				LOG.error("Create DataContext for this option is not implemented");
			}

			return context;
		}
	}

	private void buildListEntityObjects(DataDomain dataDomain)
	{
		if (listEntityObjects.size() == 0)
		{
			for (ObjEntity objEntity : dataDomain.getDataMaps().iterator().next().getObjEntities())
			{
				listEntityObjects.add(objEntity.getName());
			}
			Collections.sort(listEntityObjects);
		}
	}

	public Connection getRawConnection()
	{
		DataContext context = DataContext.createDataContext(DATA_DOMAIN_REPOSITORY);

		DataMap dataMap = context.getEntityResolver().getDataMap(DATA_MAP_REPOSITORY);

		DataNode dataNode = context.getParentDataDomain().lookupDataNode(dataMap);

		Connection connection = null;

		try
		{
			connection = dataNode.getDataSource().getConnection();
		} catch (Exception e)
		{
			LOG.error(e.getCause());
		}

		return connection;

	}

	public boolean hasInitialisationHappened()
	{
		return _bInitialisationHappened;
	}

	public boolean persistObjectContext(ObjectContext oc)
	{
		try
		{
			oc.commitChanges();
		} catch (ValidationException vex)
		{
			String strMessage = "";

			ValidationResult vr = vex.getValidationResult();
			if (vr.hasFailures())
			{
				List<ValidationFailure> failures = vr.getFailures();
				if (failures.size() > 0)
				{
					Iterator<ValidationFailure> it = failures.iterator();
					while (it.hasNext())
					{
						Object info = it.next();
						if (info != null)
						{
							Log.instance().getLogger().info(info);
						}

						if (info instanceof BeanValidationFailure)
						{
							BeanValidationFailure bvf = (BeanValidationFailure) info;
							strMessage += "- " + bvf.getDescription() + ".\n\r";
							System.out.println(bvf.getSource());
						}
					}
				}
			}

			String strError = "Persist crashed: " + vex.getLocalizedMessage() + ": " + vex.getCause() + "\n\r" + strMessage;
			Log.instance().getLogger().info(strError);
            // ErrorDialog.reportError("validation: " + strMessage);
			return false;
		} catch (DeleteDenyException dde)
		{
			String strError = "Persist crashed: " + dde.getLocalizedMessage() + ": " + dde.getCause();
			Log.instance().getLogger().info(strError);
            // ErrorDialog.reportError(dde.getCause().getMessage());
			return false;
		} catch (FaultFailureException ffe)
		{
			String strError = "Persist crashed: " + ffe.getLocalizedMessage() + ": " + ffe.getCause();
			Log.instance().getLogger().info(strError);
            // ErrorDialog.reportError(ffe.getCause().getMessage());
			return false;
		} catch (CayenneRuntimeException cex)
		{
			String strError = "Persist crashed: " + cex.getLocalizedMessage() + ": " + cex.getCause();
			Log.instance().getLogger().info(strError);
            // ErrorDialog.reportError(cex.getCause().getMessage());
			return false;
		} catch (Exception e)
		{
			String strError = "Persist crashed: " + e.getLocalizedMessage() + ": " + e.getCause();
			Log.instance().getLogger().info(strError);
            // ErrorDialog.reportError(e.getCause().getMessage());
			return false;
		}

		return true;
	}

	protected void setInitialisationHappened(boolean b)
	{
		_bInitialisationHappened = b;
	}
}
