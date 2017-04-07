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
** $Id: GLibraryField.java 741 2009-04-19 18:11:08Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.field;

import com.swordlord.gozer.builder.ObjectTree;

/**
 * Field for a ont to N relations field
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GOneToNField extends GField
{
    /**
     * The field to display from N relation
     */
    public static String ATTRIBUTE_DISPLAY_FIELD = "displayField";

    /**
     * The separator for the values in the field
     */
    public static String ATTRIBUTE_SEPARATOR = "separator";
    
    
    /**
     * Databinding for the target entity
     */
    public static String ATTRIBUTE_DATABINDING_TARGET = "DataBindingTarget";
    
    
	public static String getObjectTag()
	{
		return "onetonfield";
	}

	/**
     * Constructor
     * 
     * @param root
     */
	public GOneToNField(ObjectTree root)
	{
		super(root);
	}

	/**
     * @return the display field tag
     */
	public String getDisplayField()
    {
        return getAttribute(ATTRIBUTE_DISPLAY_FIELD);
    }
	
	/**
     * @return the separator tag for the values
     */
    public String getSeparator()
    {
        return getAttribute(ATTRIBUTE_SEPARATOR);
    }
    
    /**
     * @return the databindingTarget tag for the values
     */
    public String getDataBindingTarget()
    {
        return getAttribute(ATTRIBUTE_DATABINDING_TARGET);
    }
}
