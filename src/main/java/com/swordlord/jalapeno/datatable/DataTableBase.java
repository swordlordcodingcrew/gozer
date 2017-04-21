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
** $Id: LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datatable;

import java.io.Serializable;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.swordlord.gozer.security.Roles;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.util.ConversionUtil;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datatable.event.DataTableChangeListener;
import com.swordlord.jalapeno.datatable.event.DataTableEventDispatcher;
import com.swordlord.jalapeno.dataview.OrderingParam;
import com.swordlord.jalapeno.dataview.ViewFilter;
import com.swordlord.jalapeno.fkey.FKeyBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @since 2
 * @author LordEidi
 */
@SuppressWarnings("serial")
public class DataTableBase implements Serializable
{
    protected static final Log LOG = LogFactory.getLog(DataTableBase.class);

    /**
     * 
     */
    protected DataContainer _dc;

    /**
     * 
     */
    protected DataTableEventDispatcher _changeDispatcher;

    /**
     * 
     */
    public DataTableBase()
    {
    	_dc = new DataContainer();

        _dc.addTable(this);
    }

    /**
     * @param dc
     */
    public DataTableBase(DataContainer dc)
    {
    	_dc = dc;

        _dc.addTable(this);
    }
    
    
    /**
     * @return
     */
    public int fill()
	{
    	return 0;
	}
    
    /**
     * @param strField
     * @return
     */
    public boolean FieldHasOwnComparator(String strField)
    {
    	return false;
    }
    
    /**
     * @param bAscending
     * @param strField
     * @param value1
     * @param value2
     * @return
     */
    public int compare(boolean bAscending, String strField, Object value1, Object value2)
    {
    	int compareResult = ConversionUtil.toComparable(value1).compareTo(ConversionUtil.toComparable(value2));
        return (bAscending) ? compareResult : -compareResult;
    }

	// Event handling stuff
    public void addDataObjectChangeListener(DataTableChangeListener listener)
    {
        _changeDispatcher = DataTableEventDispatcher.add(_changeDispatcher, listener);
    }

	public void clearDataTableChangeListeners()
    {
        if (_changeDispatcher != null)
        {
            _changeDispatcher.clear();
            _changeDispatcher = null;
        }
    }

	public DataRowBase get(int index)
    {
        return getDataRows().get(index);
    }

	private String getClassName()
    {
    	return this.getClass().getName();
    }

	public DataRowBase getDataRow(int index)
    {
        return getDataRows().get(index);
    }

    public DataRowBase getDataRow(int index, ViewFilter filter)
    {
        return getDataRows(filter).get(index);
    }

	public String getAbsoluteDataRowName()
	{
		String strClassName = this.getClass().getName();
    	strClassName = strClassName.replaceAll("DataTable", "DataRow");
    	strClassName = strClassName.replaceAll("datatable", "datarow");

    	return strClassName;
	}

    public List<DataRowBase> getDataRows()
    {
    	return _dc.getDataRows(this);
    }
    
    public <T> List<T> getDataRows(Class<T> clazz)
    {
    	return _dc.getDataRows(this, clazz);
    }

    public List<DataRowBase> getDataRows(OrderingParam ordering)
    {
    	return _dc.getDataRows(this, ordering);
    }
    
    public <T> List<T> getDataRows(OrderingParam ordering, Class<T> clazz)
    {
    	return _dc.getDataRows(this, ordering, clazz);
    }

    /**
     * @param filter
     * @return
     */
    public List<DataRowBase> getDataRows(ViewFilter filter)
    {
        return _dc.getDataRows(this, filter);
    }

    public List<DataRowBase> getDataRows(ViewFilter filter, OrderingParam ordering)
    {
    	return _dc.getDataRows(this, filter, ordering);
    }

    public Format getFieldFormat(String strField)
    {
    	String strClassName = getClassName();
    	strClassName = strClassName.replaceAll("DataTable", "DataRow");
    	strClassName = strClassName.replaceAll("datatable", "datarow");

    	Collection<ObjEntity> entities = _dc.getObjectContext().getEntityResolver().getObjEntities();

    	Iterator<ObjEntity> itEntities = entities.iterator();
    	while (itEntities.hasNext())
    	{
    		ObjEntity entity = itEntities.next();
    		if(strClassName.compareTo(entity.getClassName()) == 0)
    		{
    	    	Collection<ObjAttribute> attributes = entity.getAttributes();

    	    	Iterator<ObjAttribute> itAttributes = attributes.iterator();
    	    	while (itAttributes.hasNext())
    	    	{
    	    		ObjAttribute attribute = itAttributes.next();
    	    		if(attribute.getName().compareToIgnoreCase(strField) == 0)
    	    		{
    	    			// TODO some calculation how to find field format from type etc
    	    			return null;
    	    		}
    	    	}

    		}
    	}

    	return null;
    }

    public Class<?> getFieldType(String strField)
    {
		String strClassName = this.getAbsoluteDataRowName();

    	Collection<ObjEntity> entities = _dc.getObjectContext().getEntityResolver().getObjEntities();

    	Iterator<ObjEntity> itEntities = entities.iterator();
    	while (itEntities.hasNext())
    	{
    		ObjEntity entity = itEntities.next();
    		if(strClassName.compareTo(entity.getClassName()) == 0)
    		{
    	    	Collection<ObjAttribute> attributes = entity.getAttributes();

    	    	Iterator<ObjAttribute> itAttributes = attributes.iterator();
    	    	while (itAttributes.hasNext())
    	    	{
    	    		ObjAttribute attribute = itAttributes.next();
    	    		if(attribute.getName().compareToIgnoreCase(strField) == 0)
    	    		{
    	    			try
    	    			{
    	    				return Class.forName(attribute.getType());
    	    			}
    	    			catch(Exception e)
    	    			{
    	    				LOG.error(e);
    	    				return null;
    	    			}
    	    		}
    	    	}

    		}
    	}

		LOG.info(MessageFormat.format("Field '{0}' not found in Table '{1}'", strField, this.getClass().getName()));
    	return null;
    }

    public FKeyBase getFKey(String strField)
	{
		return null;
	}

    public String getTableId()
	{
		String strClassName = this.getClass().getName();
		String[] strElements = strClassName.split("\\.");
		if(strElements.length == 0)
		{
			throw new InternalError("strClassName is not OK");
		}

		String strName = strElements[strElements.length - 1];
		strName = strName.replaceAll("DataTable", "");

    	return strName.toLowerCase();
	}

    public String getAbsoluteTableName()
	{
		return this.getClass().getName();
	}

    public ObjectContext getObjectContext()
	{
		return _dc.getObjectContext();
	}
    
    public DataContainer getDataContainer()
    {
    	return _dc;
    }

    public boolean hasFKey(String strField)
	{
		return false;
	}

    public boolean isFilteredEnvironment()
	{
		return this._dc.hasSessionInfo() && !this._dc.getSessionInfo().hasRole(Roles.ADMIN);
	}

    public void removeDataObjectChangeListener(DataTableChangeListener listener)
    {
        _changeDispatcher = DataTableEventDispatcher.remove(_changeDispatcher, listener);
    }

    public int size()
    {
        return getDataRows().size();
    }

    public int size(ViewFilter filter)
    {
        return getDataRows(filter).size();
    }

    public List<DataRowBase> subList(int fromIndex, int toIndex) {
        List<DataRowBase> rows = getDataRows();
    	return rows.subList(fromIndex, toIndex);
    }

	public String toHTML()
	{
        List<DataRowBase> rows = getDataRows();

		StringBuffer sb = new StringBuffer();
		sb.append("\n\r");
		sb.append("\n\r");

		sb.append(this.getAbsoluteTableName());

		for(DataRowBase row : rows)
		{
			sb.append(row.toHTML());
			sb.append("\n\r");
		}

		return sb.toString();
	}
	
	public String[] getPropertyList()
	{
		return null;
	}

    /**
     * Compare Fields with "." like 1.2.4
     * 
     * @param bAscending
     *            Ascending compare
     * @param value1
     *            value to compare
     * @param value2
     *            value to compare
     * @return
     */
    protected int compareDotNumber(boolean bAscending, Object value1, Object value2)
    {
        String strVal1 = "";
        String strVal2 = "";

        if (value1 instanceof List)
        {
            strVal1 = ((List<?>) value1).get(0).toString().trim();
        }
        else
        {
            strVal1 = value1.toString();
        }

        if (value2 instanceof List)
        {
            strVal2 = ((List<?>) value2).get(0).toString().trim();
        }
        else
        {
            strVal2 = value2.toString();
        }

        String[] strArr1 = strVal1.split("\\.");
        String[] strArr2 = strVal2.split("\\.");

        // this is a neat little hack to make sure that both arrays are of the
        // same size
        if (strArr1.length > strArr2.length)
        {
            for (int i = strArr2.length; i < strArr1.length; i++)
            {
                strVal2 += ".0";
            }

            strArr2 = strVal2.split("\\.");
        }
        else
        {
            for (int i = strArr1.length; i < strArr2.length; i++)
            {
                strVal1 += ".0";
            }

            strArr1 = strVal1.split("\\.");
        }

        for (int i = 0; i < strArr1.length; i++)
        {
            try
            {
                Integer int1 = Integer.parseInt(strArr1[i].trim());
                Integer int2 = Integer.parseInt(strArr2[i].trim());

                int compareResult = int1.compareTo(int2);
                if (compareResult != 0)
                {
                    return (bAscending) ? compareResult : -compareResult;
                }
            }
            catch (NumberFormatException e)
            {
                int compareResult = ConversionUtil.toComparable(strArr1[i]).compareTo(ConversionUtil.toComparable(strArr2[i]));
                if (compareResult != 0)
                {
                    return (bAscending) ? compareResult : -compareResult;
                }
            }
        }

        return 0;
    }


}
