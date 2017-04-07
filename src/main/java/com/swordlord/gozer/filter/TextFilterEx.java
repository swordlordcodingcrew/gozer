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
 ** $Id: TextFilterEx.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.filter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.AbstractFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class TextFilterEx extends AbstractFilter
{
    private final TextField<String> filter;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            model for the underlying form component
	 * @param form
	 *            filter form this filter will be added to
	 */
    public TextFilterEx(String id, IModel<String> model, FilterForm form, boolean autoSubmit)
	{
		super(id, form);
        filter = new TextField<String>("filter", model);
		
		if (autoSubmit)
		{
            filter.add(new AttributeModifier("onblur", new Model<String>("this.form.submit();")));
		}
		
		enableFocusTracking(filter);
		add(filter);
	}

	/**
	 * @return underlying {@link TextField} form component that represents this filter
	 */
    public final TextField<String> getFilter()
	{
		return filter;
	}
}