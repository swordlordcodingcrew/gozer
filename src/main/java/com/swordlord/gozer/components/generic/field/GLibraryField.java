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
** $Id: GLibraryField.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.field;

import com.swordlord.gozer.builder.ObjectTree;

@SuppressWarnings("serial")
public class GLibraryField extends GField
{
	public static String ATTRIBUTE_CODE = "code";

	public static String getObjectTag()
	{
		return "libraryfield";
	}

	public GLibraryField(ObjectTree root)
	{
		super(root);
	}

	public String getCodeType()
	{
		return getAttribute(ATTRIBUTE_CODE);
	}
}
