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
** $Id: DataRowChoiceRenderer.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.wicket;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class DataRowChoiceRenderer implements IChoiceRenderer<DataRowBase>
{
	private String _key;
	
	public DataRowChoiceRenderer(String key)
	{
		_key = key;
	}
	
	public Object getDisplayValue(DataRowBase row)
	{
        return row.getProperty(_key);
    }

    public String getIdValue(DataRowBase row, int index)
    {
    	Object value = row.getProperty(row.getKeyName());
        return value == null ? null : value.toString();
    }
}
