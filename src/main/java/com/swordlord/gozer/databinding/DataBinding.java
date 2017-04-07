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
 ** $Id: DataBinding.java 1291 2011-12-12 19:25:11Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.databinding;

import java.io.Serializable;
import java.util.LinkedList;

import org.apache.cayenne.FaultFailureException;
import org.apache.commons.lang.ObjectUtils;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.datatypeformat.DataTypeFormat;
import com.swordlord.gozer.datatypeformat.DataTypeHelper;
import com.swordlord.gozer.datatypeformat.EmptyTypeFormat;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;
import com.swordlord.jalapeno.dataview.OrderingParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@SuppressWarnings("serial")
public class DataBinding implements Serializable
{
    protected static final Log LOG = LogFactory.getLog(DataBinding.class);

    protected DataBindingManager _dataBindingManager;
    protected DataBindingMember _dataBindingMember;
    protected DataBindingField _dataBindingField;

    public DataBinding(ObjectBase objectBase)
    {
        _dataBindingManager = objectBase.getDataBindingManager();
        _dataBindingMember = objectBase.getDataBindingMember();
        _dataBindingField = new DataBindingField(objectBase, this);
    }

    public DataBinding(ObjectBase objectBase, DataBindingManager dataBindingManager, DataBindingMember dataBindingMember)
    {
        _dataBindingManager = dataBindingManager;
        _dataBindingMember = dataBindingMember;
        _dataBindingField = new DataBindingField(objectBase, this);
    }

    public int getCount()
    {
        return getDataBindingManager().getCount(this.getDataBindingMember());
    }

    public DataRowBase getCurrentRootRow()
    {
        return getDataBindingManager().getCurrentRootRow(this.getDataBindingMember());
    }

    public DataRowBase getCurrentRow()
    {
        return getDataBindingManager().getCurrentRow(this.getDataBindingMember());
    }

    public void deleteCurrentRow()
    {
        getDataBindingManager().deleteCurrentRow(this.getDataBindingMember());
    }

    public DataBindingField getDataBindingField()
    {
        return _dataBindingField;
    }

    public String getDataBindingFieldName()
    {
        // _dataBindingField
        return _dataBindingMember.getDataBindingFieldName();
    }

    public DataBindingManager getDataBindingManager()
    {
        return _dataBindingManager;
    }

    public DataBindingMember getDataBindingMember()
    {
        return _dataBindingMember;
    }

    public String getDataBindingPathName()
    {
        return _dataBindingMember.getDataBindingPathName();
    }

    public Object getFormattedValue()
    {
        DataRowBase row = getCurrentRow();

        if (row == null)
        {
            // this happens if there is a relation which is not set yet
            return "";
        }
        else
        {
            return getFormattedValue(row);
        }
    }

    public Object getFormattedValue(DataRowBase rowCurrent)
    {
        Object oValue = null;

        // First, find correct row!
        if (_dataBindingField.isForeignKey())
        {
            LinkedList<DataBindingElement> elements = rowCurrent.getFKeyDisplayPath(_dataBindingField.getFieldName());

            DataRowBase rowFKey = getResolvedRow(elements, rowCurrent);

            oValue = getRawValue(rowFKey, elements.getLast().getPathElement());
        }
        else if ((_dataBindingMember.getDataBindingPathName().split("\\.").length > 1))
        {
            // based on row, find referenced row
            if (_dataBindingField._strTableName.equals(rowCurrent.getTableName()))
                oValue = getRawValue(rowCurrent);
            else
                oValue = getRawValue(getResolvedRow(rowCurrent));
        }
        else
        {
            // then, read value from row
            oValue = getRawValue(rowCurrent);
        }

        String strFormattedValue = null;

        DataTypeFormat format = _dataBindingField.getDataFormat();
        if ((format == null) || (format instanceof EmptyTypeFormat))
        {
            return ObjectUtils.toString(oValue);

        }
        else
        {
            try
            {
                if (oValue == null)
                    strFormattedValue = null;
                else
                    strFormattedValue = format.getFieldFormat().format(oValue);
            }
            catch (Exception ex)
            {
                LOG.debug(ex.getMessage());
                strFormattedValue = "";
            }
        }

        return strFormattedValue;
    }

    public int getPosition()
    {
        return getDataBindingManager().getPosition(this.getDataBindingMember());
    }

    public Object getRawValue()
    {
        DataRowBase row = getCurrentRow();

        return getRawValue(row);
    }

    public Object getRawValue(DataRowBase row)
    {
        Object o = null;
        if (row != null)
        {
            try
            {
                o = row.getProperty(getDataBindingFieldName());
            }
            catch (FaultFailureException e)
            {
                LOG.error(e.getMessage());
            }
        }

        return o;
    }

    public Object getRawValue(DataRowBase row, String strFieldName)
    {
        if ((row == null) || ((strFieldName == null) || (strFieldName.length() == 0)))
        {
            return null;
        }

        return row.getProperty(strFieldName);
    }

    public DataRowBase getResolvedRow(DataRowBase dataRow)
    {
        return getDataBindingManager().getResolvedRow(this.getDataBindingMember(), dataRow);
    }

    public DataRowBase getResolvedRow(LinkedList<DataBindingElement> elements, DataRowBase dataRow)
    {
        return getDataBindingManager().getResolvedRow(elements, this.getDataBindingMember().getDataBindingRootTableName(), dataRow);
    }

    public void move(DataRowKeyBase key)
    {
        getDataBindingManager().move(this.getDataBindingMember(), key);
    }

    public void moveFirst()
    {
        getDataBindingManager().moveFirst(this.getDataBindingMember());
    }

    public void moveLast()
    {
        getDataBindingManager().moveLast(this.getDataBindingMember());
    }

    public void moveNext()
    {
        getDataBindingManager().moveNext(this.getDataBindingMember());
    }

    public void movePrevious()
    {
        getDataBindingManager().movePrevious(this.getDataBindingMember());
    }

    public void setFormattedValue(DataRowBase row, Object value)
    {
        Object rawValue = toRawValue(value);
        setRawValue(row, rawValue);
    }

    public void setFormattedValue(Object value)
    {
        DataRowBase row = getCurrentRow();
        setFormattedValue(row, value);
    }

    // TODO hack, remove me
    public void setOrdering(OrderingParam ordering)
    {
        if (ordering != null)
        {
            try
            {
                getDataBindingManager().getDataView(this.getDataBindingMember()).setOrdering(ordering);
            }
            catch (Exception e)
            {
                LOG.error("Can't set Order: " + e.getMessage());
            }
        }
    }

    public void setRawValue(DataRowBase row, Object strValue)
    {
        DataRowBase rowPersist = null;

        if (_dataBindingField.isForeignKey())
        {
            rowPersist = row;
        }
        else if (!row.getTableName().equals(_dataBindingMember.getDataBindingTableName()))
        {
            // based on row, find referenced row
            LOG
                    .error("THIS CASE MUST BE ANAYLZED : 1. In which case occurs this case ? 2. Perhaps we need to catch the rootRow ? 3. The resolve does not work ?");
            rowPersist = getResolvedRow(row);
        }
        else
        {
            // then, read value from row
            rowPersist = row;
        }

        if (rowPersist == null)
        {
            LOG.warn("rowPersist is null");
            return;
        }

        rowPersist.setProperty(getDataBindingFieldName(), strValue);
    }

    public void setRelation(DataRowBase rowReference)
    {
        DataRowBase row = getCurrentRow();
        row.setRelation(rowReference);
    }

    public Object toRawValue(Object value)
    {
        if (value == null)
        {
            return null;
        }

        // 
        Object v = null;
        try
        {
            Class<?> untypedValueClass = _dataBindingField.getFieldType();
            v = DataTypeHelper.fromDataType(untypedValueClass, value);
        }
        catch (Exception ex)
        {
            LOG.error(ex);
        }

        return v;
    }

    public void writeValue(String strValue)
    {
        DataRowBase row = getCurrentRow();
        setRawValue(row, strValue);
    }
}
