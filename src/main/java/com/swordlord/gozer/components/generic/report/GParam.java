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
 ** $Id: GParam.java 1344 2011-12-28 11:23:25Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.report;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;

/**
 * Base class for report parameters. Child element of {@link GQueries}.
 */
@SuppressWarnings("serial")
public abstract class GParam extends ObjectBase
{
    /** The parameter name. */
	public static String ATTRIBUTE_PARAM_IDENT = "param_ident";


	public GParam(ObjectTree root)
	{
		super(root);
	}

    /**
     * Returns the name of the parameter.
     * 
     * @return The parameter name
     */
	public String getParamIdent()
	{
		return getAttribute(ATTRIBUTE_PARAM_IDENT);
	}
}
