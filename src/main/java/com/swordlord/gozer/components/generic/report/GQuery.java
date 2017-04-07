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
 ** $Id: GQuery.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.report;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;

@SuppressWarnings("serial")
public class GQuery extends ObjectBase
{
	public static String ATTRIBUTE_TYPE = "type";
	public static String ATTRIBUTE_ROOTOBJECT = "root_object";

	public static String getObjectTag()
	{
		return "query";
	}

	public GQuery(ObjectTree root)
	{
		super(root);
	}

	public String getType()
	{
		return getAttribute(ATTRIBUTE_TYPE);
	}

	public String getRootObject()
	{
		return getAttribute(ATTRIBUTE_ROOTOBJECT);
	}
}
