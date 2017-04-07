/*-----------------------------------------------------------------------------
**
** - Open Risk and Compliance (ORICO) Framework -
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
** $Id: EADManager.java 1352 2012-02-07 10:11:59Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.ead;

// IMPORT
import java.util.Hashtable;

import com.swordlord.jalapeno.DBGenerator;
import org.apache.commons.lang.ArrayUtils;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.repository.businessobject.EntityAttributeDefinition.EntityAttributeDefinitionBO;
import com.swordlord.repository.datarow.EntityAttributeDefinition.EntityAttributeDefinitionDataRow;
import com.swordlord.repository.datatable.EntityAttributeDefinition.EntityAttributeDefinitionDataTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EADManager
{
    protected static final Log LOG = LogFactory.getLog(EADManager.class);

    private static EADManager _instance;
	private Hashtable<String, EntityAttributeDefinitionDataRow> _attributesDef = new Hashtable<String, EntityAttributeDefinitionDataRow>();
	private DataContainer _dc;

	public static EADManager instance()
	{
		if (_instance == null)
		{
            final EADManager inst = new EADManager();
			inst._dc = new DataContainer();
			
			//Search EntityAttributeDefinition for the strKey
            EntityAttributeDefinitionBO eavDefinition = new EntityAttributeDefinitionBO(inst._dc);
			eavDefinition.loadDC();
            EntityAttributeDefinitionDataTable dataTableDefinition = new EntityAttributeDefinitionDataTable(inst._dc);
            for (EntityAttributeDefinitionDataRow row : dataTableDefinition.getDataRows(EntityAttributeDefinitionDataRow.class))
			{
                String strAttribute = inst.buildKey(row.getTargetTableName(), row.getAttributeName());

                if (strAttribute != null)
                {
                    inst._attributesDef.put(strAttribute, row);
                }
            }
            // publish instance after initialization
            _instance = inst;
		}
		return _instance;
	}
	
    /**
     * Reset the singleton.
     */
    public static void resetInstance()
	{
		_instance = null;
	}

	private String buildKey(String tableName, String attributeName)
	{
        if (tableName == null)
        {
            LOG.fatal("tableName is null");
            return null;
        }
        if (attributeName == null)
        {
            LOG.fatal("attributeName is null");
            return null;
        }

		return tableName.toUpperCase() + "-" + attributeName.toUpperCase();
	}
	
	public EntityAttributeDefinitionDataRow getAttributeDefinition(String tableName, String attributeName)
	{
		return _attributesDef.get(buildKey(tableName, attributeName));
	}
	
    /**
     * Returns wherever a given attribute is virtual - thus means it is not
     * defined on the physical table but in the
     * {@link EntityAttributeDefinitionDataTable}.
     * 
     * @param tableName
     *            The 'owning' table
     * @param attributeName
     *            The attribute name
     * @return True if the specified attribute is virtual
     */
    public boolean isAttributeVirtual(String tableName, String attributeName)
    {
		return _attributesDef.containsKey(buildKey(tableName, attributeName));
	}

    /**
     * Returns wherever the specified attribute is defined by checking its
     * owning table and by checking virtual definitions using
     * {@link #isAttributeVirtual(String, String)}.
     * 
     * @param listofTableFields
     *            The fields of the specified table or null
     * @param tableName
     *            The 'owning' table
     * @param attributeName
     *            The attribute name
     * @return True if: The passed field list was null or the specified
     *         attribute was found in the passed list or if a virtual attribute
     *         is defined for the attribute in question
     */
    public boolean isAttributeDefined(String[] listofTableFields, String tableName, String attributeName)
	{
        // is it a table field?
        if (listofTableFields == null || ArrayUtils.contains(listofTableFields, attributeName))
        {
            return true;
        }
        // check for virtual fields
        return isAttributeVirtual(tableName, attributeName);
	}
}


