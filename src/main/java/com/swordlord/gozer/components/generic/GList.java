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
** $Id: GList.java 1344 2011-12-28 11:23:25Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic;

import com.swordlord.gozer.builder.ObjectTree;

@SuppressWarnings("serial")
public class GList extends ObjectBase
{
	public static String ATTRIBUTE_ORDERING = "ordering";
	public static String ATTRIBUTE_SHOW_CURRENT = "ShowCurrent";
	public static String ATTRIBUTE_SHOW_FILTER = "ShowFilter";

	public static String getObjectTag()
	{
		return "list";
	}

	public GList(ObjectTree root)
	{
		super(root);
	}

	public String getOrdering()
	{
		return getAttribute(ATTRIBUTE_ORDERING);
	}

    /**
     * Returns wherever the current row should use a different appearance than
     * default rows.
     * 
     * @return True if the current row should use a different appearance
     */
	public boolean getShowCurrent()
	{
        final String strValue = getAttribute(ATTRIBUTE_SHOW_CURRENT);
        // a table highlights the current row per default....
        if (strValue == null || strValue.isEmpty())
        {
            return true;
        }
        return Boolean.parseBoolean(strValue);
	}
	
	public boolean getShowFilter()
	{
        final String strValue = getAttribute(ATTRIBUTE_SHOW_FILTER);
        // a table is filterable per default....
        if (strValue == null || strValue.isEmpty())
        {
            return true;
        }
        return Boolean.parseBoolean(strValue);
	}
}
