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
 ** $Id: DataRowBase.java 1295 2011-12-15 15:41:02Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datarow;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import com.swordlord.jalapeno.DBGenerator;
import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import com.swordlord.gozer.databinding.DataBindingElement;
import com.swordlord.jalapeno.access.DataContextEx;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.ead.EADManager;
import com.swordlord.repository.businessobject.EntityAttributeValue.EntityAttributeValueBO;
import com.swordlord.repository.datarow.EntityAttributeDefinition.EntityAttributeDefinitionDataRow;
import com.swordlord.repository.datarow.EntityAttributeValue.EntityAttributeValueDataRow;
import com.swordlord.repository.datatable.EntityAttributeValue.EntityAttributeValueDataTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@SuppressWarnings("serial")
public class DataRowBase extends CayenneDataObject
{
	public static final byte USAGE_CATEGORY_UNKNOWN = 0;
	public static final byte USAGE_CATEGORY_SYSTEM = 1;
	public static final byte USAGE_CATEGORY_REPOSITORY = 2;
	public static final byte USAGE_CATEGORY_ENVIRONMENT = 3;

	protected static final Log LOG = LogFactory.getLog(DataRowBase.class);

	static final Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();

	static
	{
		classMapping.put("boolean", Boolean.class);
		classMapping.put("int", Integer.class);
		classMapping.put("char", Character.class);
		classMapping.put("float", Float.class);
		classMapping.put("byte", Byte.class);
		classMapping.put("short", Short.class);
		classMapping.put("long", Long.class);
		classMapping.put("double", Double.class);
	}

	public static String[] getPropertyList()
	{
		return null;
	}

	public String[] getProperties()
	{
		return null;
	}

	private LinkedList<String> _errors;

	// Extension Points
	protected UUID uuidEPDBCommon = null;

	public DataRowBase()
	{
		super();

		clearErrors();
	}

	public void addError(String strField, String strError)
	{
		// Field is not yet supported, but interface has it for compatibility
		// reasons
		_errors.add(strError);
	}

	public void clearErrors()
	{
		_errors = new LinkedList<String>();
	}

	// ----------------------------------------------------------------
	public void fieldChanged(String strFieldName, Object oOldVal, Object oNewVal)
	{
		/*
		 * // call extensions, if any if(uuidEPDBCommon != null) {
		 * List<IDBCommon> listPlugins = getExtensionObjects(uuidEPDBCommon);
		 * 
		 * for(IDBCommon plug : listPlugins) { try { plug.fieldChanged(this,
		 * strFieldName, oOldVal, oNewVal); } catch(Exception ex) {
		 * Log.instance().getLogger().info(plug.getClass());
		 * Log.instance().getLogger().error(ex); } } }
		 */
	}

	private String getClassName()
	{
		return this.getClass().getName();
	}

	public LinkedList<String> getErrors()
	{
		return _errors;
	}

	public UUID getExtensionPoint()
	{
		return uuidEPDBCommon;
	}

	// This is the standard behaviour where the fkey field is displayed
	// when writing a subclass this should be replaced with the path description
	// linking to the fkey'ed table.
	public LinkedList<DataBindingElement> getFKeyDisplayPath(String strFieldName)
	{
		DataBindingElement element = new DataBindingElement();

		element.setPathElement(strFieldName);
		element.setField(false);

		LinkedList<DataBindingElement> elements = new LinkedList<DataBindingElement>();

		elements.add(element);

		return elements;
	}

	// ----------------------------------------------------------------
	// TO BE OVERWRITTEN
	// ----------------------------------------------------------------
	public DataRowKeyBase getKey()
	{
		throw new InternalError("getKey must be overwritten!");
	}

	public String getKeyName()
	{
		throw new InternalError("getKeyName must be overwritten!");
	}

	private String getName()
	{
		String strClassName = getClassName();

		String[] elements = strClassName.split("\\.");
		String strName = elements[elements.length - 1];

		return strName.replaceAll("DataRow", "");
	}

	public String getTableName()
	{
		return getName();
	}

	public Object getProperty(String strKey)
	{
		if(this.getPersistenceState() == PersistenceState.HOLLOW)
		{
			LOG.error("Somebody emptied this datarow '" + this);
			
			//DataContext ctx = getDataContext();
			//ctx.prepareForAccess(object, property)
	        //DataObject dobj = (DataObject) DataObjectUtils.objectForPK(ctx,	objectId);
		}
		
		if (EADManager.instance().isAttributeVirtual(getTableName(), strKey))
		{
			Object val = null;
			DataContainer dc = null;

			if (getObjectContext() instanceof DataContextEx)
			{
				dc = ((DataContextEx) getObjectContext()).getDataContainer();
			} else
			{
				dc = new DataContainer();
			}
			
			// Search EntityAttributeDefinition for the Table/strKey
			EntityAttributeDefinitionDataRow definitionRow = EADManager.instance().getAttributeDefinition(getTableName(), strKey);

			EntityAttributeValueBO eav = new EntityAttributeValueBO(dc);
			eav.loadDC(getKey());

			EntityAttributeValueDataTable dataTable = new EntityAttributeValueDataTable(dc);

            for (final EntityAttributeValueDataRow row : dataTable.getDataRows(EntityAttributeValueDataRow.class))
            {
                if (row.getForeignKey().toString().equals(getKey().toUUID().toString())
                        && row.getEntityAttributeDefinitionId().equals(definitionRow.getEntityAttributeDefinitionId()))
                {
                    val = this.decodeObject(definitionRow.getAttributeType(), definitionRow.getAttributeFormat(), row.getValue());
                }
            }

			return val;
        }
        return readProperty(strKey);
	}

	public int getPropertyAsInt(String strKey)
	{
		Object val = getProperty(strKey);
		if (val instanceof Long)
		{
			return ((Long) val).intValue();
		} else
		{
			return ((Integer) getProperty(strKey)).intValue();
		}
	}

	public String getPropertyAsString(String strKey)
	{
		return (String) getProperty(strKey);
	}

	public String getPropertyAsStringForce(String strKey)
	{
		Object obj = getProperty(strKey);
		if (obj == null)
		{
			LOG.error("property '" + strKey + "' is unknown");
			return "";
		} else
		{
			if (obj instanceof String)
			{
				return (String) obj;
			} else if (obj instanceof Double)
			{
				return Double.toString((Double) obj);
			} else if (obj instanceof Integer)
			{
				return Integer.toString((Integer) obj);
			} else if (obj instanceof Long)
			{
				return Long.toString((Long) obj);
			} else if (obj instanceof Short)
			{
				return Short.toString((Short) obj);
			} else if (obj instanceof UUID)
			{
				return ((UUID) obj).toString();
			} else
			{
				return (String) getProperty(strKey);
			}
		}
	}

	public byte getUsageCategory()
	{
		return USAGE_CATEGORY_UNKNOWN;
	}

	public boolean hasErrors()
	{
		return _errors.size() > 0;
	}

	public void setKey(String strKey)
	{
		setProperty(getKeyName(), strKey);
	}

	public void setProperty(String strKey, Object value)
	{
		if (value instanceof DataRowBase)
		{
			// save your a.. neck function :)
			setRelation((DataRowBase) value);
		} else
		{
			if (EADManager.instance().isAttributeVirtual(getTableName(), strKey))
			{
				DataContainer dc;
				if (getObjectContext() instanceof DataContextEx)
				{
					dc = ((DataContextEx) getObjectContext()).getDataContainer();
				} else
				{
					dc = new DataContainer();
				}

				// Search EntityAttributeDefinition for the Table/strKey
				EntityAttributeDefinitionDataRow definitionRow = EADManager.instance().getAttributeDefinition(getTableName(), strKey);

				EntityAttributeValueBO eav = new EntityAttributeValueBO(dc);
				eav.loadDC(getKey());

				EntityAttributeValueDataTable dataTable = new EntityAttributeValueDataTable(dc);
                boolean attrAvailable = false;
                for (final EntityAttributeValueDataRow row : dataTable.getDataRows(EntityAttributeValueDataRow.class))
				{

					// Check searched attribute
					if (row.getForeignKey().toString().equals(getKey().toString()) && row.getEntityAttributeDefinitionId().equals(definitionRow.getEntityAttributeDefinitionId()))
					{
						row.setValue(encodeObject(definitionRow.getAttributeFormat(), value));
						attrAvailable = true;
					}
				}

				if (!attrAvailable)
				{
					EntityAttributeValueDataRow row = dc.getObjectContext().newObject(EntityAttributeValueDataRow.class);
					row.setEntityAttributeDefinitionId(definitionRow.getEntityAttributeDefinitionId());
					row.setForeignKey(this.getKey().toUUID());
					if (definitionRow.getAttributeType().toLowerCase().equals("java.util.date"))
					{
						SimpleDateFormat df = new SimpleDateFormat(definitionRow.getAttributeFormat());
						row.setValue(df.format((Date) value));
					} else
						row.setValue(value.toString());
				}

			} else
				writeProperty(strKey, value);
		}
	}

	// ----------------------------------------------------------------
	// called if another record was dropped onto this one -> create a link
	public void setRelation(DataRowBase targetRow)
	{
		/*
		 * // call extensions, if any if(uuidEPDBCommon != null) {
		 * List<IDBCommon> listPlugins = getExtensionObjects(uuidEPDBCommon);
		 * 
		 * for(IDBCommon plug : listPlugins) { try {
		 * plug.setRelation(targetObject, this); } catch(Exception ex) {
		 * Controller.instance().getLogger().info(plug);
		 * Controller.instance().getLogger().error(ex); } } }
		 */

		// Find relation information
		String strTargetName = targetRow.getName();

		String strSourceClassName = getClassName();

		Collection<ObjEntity> entities = getObjectContext().getEntityResolver().getObjEntities();

		Iterator<ObjEntity> itEntities = entities.iterator();
		while (itEntities.hasNext())
		{
			ObjEntity entity = itEntities.next();
			if (strSourceClassName.compareTo(entity.getClassName()) == 0)
			{
				// we found the source entity, now lets find all relations from
				// this entity
				Collection<ObjRelationship> relations = entity.getRelationships();
				Iterator<ObjRelationship> itRelations = relations.iterator();
				while (itRelations.hasNext())
				{
					ObjRelationship relation = itRelations.next();
					if (strTargetName.compareToIgnoreCase(relation.getTargetEntityName()) == 0)
					{
						// Switch ObjectContext before setting relation
						// problem is that different rows exist in different
						// contexts (different gozer frames)
						DataRowBase row = (DataRowBase) getObjectContext().localObject(targetRow.getObjectId(), targetRow);

						// set the relation
						if (relation.isToMany())
						{
							addToManyTarget(relation.getName(), row, true);
						} else
						{
							setToOneTarget(relation.getName(), row, true);
						}

						// This is an ugly hack to set the property as well, not
						// only the relation.
						// we have to do this since Cayenne otherwise can't
						// persist because of a validation error (ID missing)
						// TODO: Clean this up. Cayenne has to do something
						// along this before persisting
						// so lets look up how they work out that problem and
						// copy it to here.
						if (relation.isToPK())
						{
							this.setProperty(targetRow.getKeyName(), targetRow.getKey().toString());
						} else
						{
							targetRow.setProperty(this.getKeyName(), this.getKey().toString());
						}

						break;
					}
				}
			}
		}
	}

	public String toHTML()
	{
		StringBuffer sb = this.toStringBuffer(new StringBuffer(), true);
		return sb.toString();
	}

	// ----------------------------------------------------------------
	// When property is changing, call the
	@Override
	public void writePropertyDirectly(String strFieldName, Object oNewVal)
	{
		// only do this, if we have somebody listening for events
		if (uuidEPDBCommon != null)
		{
			// Object oOldVal = readProperty(strFieldName);

			super.writePropertyDirectly(strFieldName, oNewVal);

			// fieldChanged(strFieldName, oOldVal, oNewVal);
		} else
		{
			super.writePropertyDirectly(strFieldName, oNewVal);
		}
	}

	private Object decodeObject(String type, String format, String value)
	{

		if (null == value)
		{
			return null;
		}

		Class<?> objectClass = classMapping.get(type);
		if (null == objectClass)
		{
			try
			{
				objectClass = Class.forName(type);
			} catch (Exception e)
			{
				throw new CayenneRuntimeException("Unrecognized class '" + objectClass + "'", e);
			}
		}

		// handle dates using hardcoded format....
		if (Date.class.isAssignableFrom(objectClass))
		{
			try
			{
				return new SimpleDateFormat(format).parse(value);
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
		}

		// handle all other primitive types...
		try
		{
			Constructor<?> c = objectClass.getConstructor(String.class);
			return c.newInstance(value);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private String encodeObject(String format, Object value)
	{

		if (null == value)
		{
			return null;
		}

		if (value instanceof Date)
		{
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(value);
		} else
			return value.toString();

	}

}
