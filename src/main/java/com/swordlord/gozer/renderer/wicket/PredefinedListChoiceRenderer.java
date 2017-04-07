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
** $Id: PredefinedListChoiceRenderer.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.wicket;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import com.swordlord.gozer.databinding.DataBindingField;

@SuppressWarnings("serial")
public class PredefinedListChoiceRenderer implements IChoiceRenderer<Object>
{
	protected DataBindingField _dbField;
	
	public PredefinedListChoiceRenderer(DataBindingField dbField)
	{
		_dbField = dbField;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object object)
	{
        	return object.toString();

    }

    /* (non-Javadoc)
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
     */
    public String getIdValue(Object key, int index)
    {
        	return key.toString();
    }
}
