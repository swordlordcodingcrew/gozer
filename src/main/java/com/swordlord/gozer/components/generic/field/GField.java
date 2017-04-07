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
** $Id: GField.java 1344 2011-12-28 11:23:25Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.field;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.crosstab.GCrossTab;

@SuppressWarnings("serial")
public class GField extends ObjectBase
{
	public static String ATTRIBUTE_MAX_DISPLAY_WIDTH = "MaxDisplayWidth";
	public static String ATTRIBUTE_IS_FILTERABLE = "filterable";
	
	public static String getObjectTag()
	{
		return "field";
	}

	private GList _list;

	public GField(ObjectTree root)
	{
		super(root);
	}

	public GList getList()
	{
		return _list;
	}
	
	public int getMaxDisplayWidth()
	{
		return getAttributeAsInt(ATTRIBUTE_MAX_DISPLAY_WIDTH, 0);
	}
	
	public boolean isFilterable()
	{
        final String strValue = getAttribute(ATTRIBUTE_IS_FILTERABLE);
        // a field is filterable per default....
        if (strValue == null || strValue.isEmpty())
		{
            return true;
		}
        return Boolean.parseBoolean(strValue);
	}

	@Override
	public void inheritParent(ObjectBase parent)
	{
		super.inheritParent(parent);

		if(parent.getClass().equals(GList.class) || parent.getClass().equals(GCrossTab.class))
		{
			_list = (GList)parent;
		}
	}
}
