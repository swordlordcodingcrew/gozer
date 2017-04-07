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
 ** $Id: GWCrossTabPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.crosstab;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.crosstab.GCrossTab;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.datatable.GozerDataTable;
import com.swordlord.gozer.components.wicket.list.GWListPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

import com.swordlord.sobf.wicket.main.CrossTabDataProvider;

/**
 * TODO JavaDoc for GWCrossTabPanel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWCrossTabPanel extends GWListPanel
{

	private static final int ROWS_PER_PAGE = 20;
	private GozerDataTable _table;

    /**
     * @param id
     * @param model
     * @param listForm
     */
	public GWCrossTabPanel(String id, IModel<?> model, GCrossTab listForm)
	{
		super(id, model);

		IGozerFrameExtension fe = getFrameExtension();
		GozerController gc = fe.getGozerController();

		DataBinding dataBindingList = listForm.getDataBinding();

		SortableDataProvider provider = new CrossTabDataProvider(listForm, dataBindingList);

		DataBindingContext dbc = fe.getDataBindingContext();

        List<IColumn<DataRowBase, String>> columns = createColumns(listForm, fe, gc, dbc);

		int nRows = listForm.getPageSize(ROWS_PER_PAGE);

        _table = new CrossTabDataTable("list", columns, provider, nRows, dataBindingList, this, false);
		add(_table);
	}

	@Override
    public void addTopToolbar(AbstractToolbar toolbar)
	{
		_table.addTopToolbar(toolbar);
	}

    private List<IColumn<DataRowBase, String>> createColumns(GCrossTab crossTab, final IGozerFrameExtension fe, final GozerController gc,
            final DataBindingContext dbc)
	{
		// we don't know the size of list right now, hence
		// fill up a list and convert to an array before return
        List<IColumn<DataRowBase, String>> columns = new ArrayList<IColumn<DataRowBase, String>>();
		
		for (final ObjectBase child : crossTab.getChildren())
		{
			final String caption = child.getCaption();

			if (child.getClass().equals(GField.class))
			{
				final DataBinding dataBindingChild = child.getDataBinding();
				 
				boolean isHeader = false;
				if (crossTab.getChildren().indexOf(child) == 0) 
				{
					isHeader = true;
				} 
				else if (!crossTab.getAttribute(GCrossTab.ATTRIBUTE_SLICE_VERTICAL2).isEmpty()) 
				{
					isHeader = crossTab.getChildren().indexOf(child) == 1;
				}

				GozerCrossTabColumn col = new GozerCrossTabColumn(new Model(caption), null, "expression", isHeader, (GField) child, dataBindingChild, GWCrossTabPanel.this);
				columns.add(col);

			} 
			else
			{
				Log.instance().getLogger().warn("don't know how to handle '" + child.getClass().getName() + "' in GWListPanel.createColumns");
			}
		}

		return columns;
	}
}
