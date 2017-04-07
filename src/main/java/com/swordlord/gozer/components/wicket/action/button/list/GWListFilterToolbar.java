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
 ** $Id: GWListFilterToolbar.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.list;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.NoFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * TODO JavaDoc for GWListFilterToolbar.java
 * 
 * @author LordEidi
 * 
 */
public class GWListFilterToolbar extends AbstractToolbar
{
	private static final long serialVersionUID = 1L;
	private static final String FILTER_COMPONENT_ID = "filter";

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be added to
	 * @param form
	 *            the filter form
	 * @param stateLocator
	 *            locator responsible for finding object used to store filter's state
	 */
    public GWListFilterToolbar(final DataTable<?, String> table, final FilterForm form, final IFilterStateLocator stateLocator)
	{
		super(table);
		
		add(form);

		if (table == null)
		{
			throw new IllegalArgumentException("argument [table] cannot be null");
		}
		if (stateLocator == null)
		{
			throw new IllegalArgumentException("argument [stateLocator] cannot be null");
		}

		// populate the toolbar with components provided by filtered columns

		RepeatingView filters = new RepeatingView("filters");
		filters.setRenderBodyOnly(true);
		form.add(filters);

        Iterator<?> it = table.getColumns().iterator();
        while (it.hasNext())
		{
			WebMarkupContainer item = new WebMarkupContainer(filters.newChildId());
			item.setRenderBodyOnly(true);

            IColumn<?, String> col = (IColumn<?, String>) it.next();
			Component filter = null;

			if (col instanceof IFilteredColumn)
			{
                IFilteredColumn<?, String> filteredCol = (IFilteredColumn<?, String>) col;
				filter = filteredCol.getFilter(FILTER_COMPONENT_ID, form);
			}

			if (filter == null)
			{
				filter = new NoFilter(FILTER_COMPONENT_ID);
			}
			else
			{
				if (!filter.getId().equals(FILTER_COMPONENT_ID))
				{
					throw new IllegalStateException(
						"filter component returned  with an invalid component id. invalid component id [" +
							filter.getId() +
							"] required component id [" +
							FILTER_COMPONENT_ID +
							"] generating column [" + col.toString() + "] ");
				}
			}

			item.add(filter);

			filters.add(item);
		}

	}
	
    public GWListFilterToolbar(final DataTable<?, String> table, final IFilterStateLocator stateLocator)
    {
	    this(table, new FilterForm("filterform", stateLocator), stateLocator);
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
	}
}
