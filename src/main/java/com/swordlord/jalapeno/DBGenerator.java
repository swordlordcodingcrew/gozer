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
** $Id: DBGenerator.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.DbGenerator;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DBGenerator
{
	protected static final Log LOG = LogFactory.getLog(DBGenerator.class);

	private DbGenerator _dbgen = null;
	private boolean _bHasErrors = false;

	public DBGenerator() {}

	// Returns SQL statements generated for selected schema generation options.
    protected void createSQL(DbGenerator generator)
    {
        // convert them to string representation for display
        StringBuffer buf = new StringBuffer();
        Iterator it = generator.configuredStatements().iterator();
        String batchTerminator = generator.getAdapter().getBatchTerminator();

        String lineEnd = (batchTerminator != null)
                ? "\n" + batchTerminator + "\n"
                : "\n";

        while (it.hasNext())
        {
            buf.append(it.next()).append(lineEnd);
        }

        LOG.info(buf.toString());
    }

	public boolean hasErrors()
	{
		return _bHasErrors;
	}

	public void initialiseFreshDB(DataContext context, DataMap dataMap)
	{
	    try
		{
	    	DataNode dataNode = context.getParentDataDomain().lookupDataNode(dataMap);
	    	DbAdapter adapter = dataNode.getAdapter();

		    _dbgen = new DbGenerator(adapter, dataMap, new DBEventLogger());
		    _dbgen.setShouldCreatePKSupport(false);
		    _dbgen.setShouldCreateTables(true);
		    _dbgen.setShouldDropPKSupport(false);
		    _dbgen.setShouldDropTables(false);

		    // TODO: set to true
		    _dbgen.setShouldCreateFKConstraints(false);

		    createSQL(_dbgen);

		    _dbgen.runGenerator(dataNode.getDataSource());

		    ValidationResult failures = _dbgen.getFailures();
	        if ((failures == null) || !failures.hasFailures())
	        {
	        	LOG.info("Schema Generation Complete.");
	        }
	        else
	        {
	        	LOG.error("failure: " + failures);
	        }
        }
        catch (Exception ex)
		{
        	LOG.error("Exception {}", ex);
        	_bHasErrors = true;
        }
	}

	// Check if Database running in this context is up and structure was defined already
	public boolean isDBAvailableAndConfigured(DataContext context)
	{
		Collection<DataMap> datamaps = context.getEntityResolver().getDataMaps();
		if(datamaps.size() == 0)
		{
			return false;
		}

		boolean bEverythingThereYet = true;

		Iterator<DataMap> iterator = datamaps.iterator();
		while(iterator.hasNext())
		{
			DataMap datamap = iterator.next();

			if(!isDBAvailableAndConfigured(context, datamap))
			{
				// TODO clean this up
				Properties sprops = System.getProperties();
				if(sprops.getProperty("target.ui").compareToIgnoreCase("wicket") != 0)
				{
					bEverythingThereYet = false;
					initialiseFreshDB(context, datamap);
				}
			}
		}

		return bEverythingThereYet;
	}

    private boolean isDBAvailableAndConfigured(DataContext context, DataMap dataMap)
	{
		DataNode dataNode = context.getParentDataDomain().lookupDataNode(dataMap);
		boolean bResult = true;

    	try
    	{
    		Connection connection = dataNode.getDataSource().getConnection();

    		DatabaseMetaData metadata = connection.getMetaData();

    		String[] types = {"TABLE"};
            ResultSet resultSet = metadata.getTables(null, null, "%", types);

            // If there is at least one, OK
            if(!resultSet.next())
            {
            	LOG.info("Database exists but no structure defined");
            	bResult = false;
            }

        	resultSet.close();
        	connection.close();
    	}
    	catch(SQLException e)
    	{
    		LOG.info("SQL exception {}", e);
        	bResult = false;
    	}

    	return bResult;
	}
}
