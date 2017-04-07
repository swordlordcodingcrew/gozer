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
** $Id: DataMapDiagramTableModel.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.tablemodel;

import javax.swing.table.AbstractTableModel;

import org.apache.cayenne.map.Attribute;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.Entity;
import com.swordlord.sobf.common.i18n.Translator;
import com.swordlord.sobf.swing.Controller;

@SuppressWarnings("serial")
public class DataMapDiagramTableModel extends AbstractTableModel
{
	private Entity _entity;
	private Translator _ls = Controller.instance().getTranslator();
	
	public DataMapDiagramTableModel(Entity entity)
	{
		 _entity = entity;
	} 
	
	public int getColumnCount()
	{
		return 2;
	}

	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex)
		{
			case 0: return Boolean.class;
			case 1: return String.class;
		  	default: return Object.class;
		}
	}

	public String getColumnName(int column)
	{
		switch (column)
		{
			case 0:
				return _ls.getString("PK", ObjEntityTableModel.class);
			case 1:
				return _ls.getString("Field", ObjEntityTableModel.class);
			default:
				throw new Error(_ls.getString("column_out_of_range", ObjEntityTableModel.class));
		}
	} 

	public int getRowCount()
	{
		return _entity.getAttributes().size();
	}
	
	public Object getValueAt(int rowIndex,int columnIndex)
	{
		if(rowIndex < 0 | rowIndex > getRowCount())
		{
			throw new Error(_ls.getString("column_out_of_range", ObjEntityTableModel.class));
		}
		
		Attribute attribute = (Attribute) _entity.getAttributes().toArray()[rowIndex];

		switch (columnIndex)
		{
			case 0:
			{
				if(attribute instanceof DbAttribute)
				{
					return new Boolean(((DbAttribute) attribute).isPrimaryKey());
				}
				else
				{
					return Boolean.FALSE;
				}
			}
			case 1:
				return attribute.getName();
			default:
				return "";
		}		
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	} 
	
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
	}
}
