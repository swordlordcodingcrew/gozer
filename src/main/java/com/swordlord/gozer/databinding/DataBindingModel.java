/*-----------------------------------------------------------------------------
**
** -Gozer is not Zuul-
**
** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
** and individual authors
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
package com.swordlord.gozer.databinding;

import org.apache.wicket.model.IModel;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.repository.datarow.Code.CodeDataRow;

@SuppressWarnings("serial")
public class DataBindingModel implements IModel<Object>
{
	// TODO: add type conversion to the binding...

	private DataBinding _binding;
	private DataRowBase _row;

	public DataBindingModel(DataBinding binding)
	{
		_binding = binding;
	}

	public DataBindingModel(DataBinding binding, DataRowBase row)
	{
		_binding = binding;

		_row = row;
	}

	public void detach()
	{
	}

	public Object getObject()
	{
		return _row == null ? _binding.getFormattedValue() : _binding.getFormattedValue(_row);
	}

	public void setObject(Object object)
	{
		if (object != null)
		{
			// Hack, but probably the only solution for now...
			if(object instanceof CodeDataRow)
			{
				object = ((CodeDataRow)object).getCodeValue();
			}

			if(_row == null)
			{
				_binding.setFormattedValue(object);
			}
			else
			{
				_binding.setFormattedValue(_row, object);
			}
		}
	}
	
	/**
     * Check if the Model have rows
     * 
     * @return
     */
	public Boolean hasRows()
    {
	    DataViewBase dv = this._binding._dataBindingManager.getDataView(this._binding._dataBindingMember);

        if (dv.size() > 0)
            return true;
        else
            return false;
    }
}