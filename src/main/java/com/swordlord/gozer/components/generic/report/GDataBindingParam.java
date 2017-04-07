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
package com.swordlord.gozer.components.generic.report;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.databinding.DataBindingMember;

/**
 * Report parameter retrieving its value over a {@link DataBindingMember}.
 * 
 * @author LordEidi
 */
public class GDataBindingParam extends GParam
{
    /**
     * Returns the corresponding XML tag used in the gozer files.
     * 
     * @return The XML tag representing this object
     */
    public static String getObjectTag()
    {
        return "param";
    }

    /**
     * Default constructor.
     * 
     * @param root
     *            The root of the parent tree
     */
    public GDataBindingParam(ObjectTree root)
    {
        super(root);
    }
}
