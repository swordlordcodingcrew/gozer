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
 ** $Id: GozerDataTable.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.datatable;

import java.util.List;

import com.swordlord.gozer.dataprovider.GozerSortableFilterableDataProvider;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.list.GWListActionToolbar;
import com.swordlord.gozer.components.wicket.action.button.list.GWListFilterToolbar;
import com.swordlord.gozer.components.wicket.list.GWListPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * Gozer version of a {@link DataTable}.<br>
 * Normally used inside a {@link GWListPanel}.
 */
@SuppressWarnings("serial")
public class GozerDataTable extends DataTable<DataRowBase, String>
{
    /** The databinding of the table. */
    private DataBinding _dataBinding;

    /** The parent panel. */
    private GWPanel _gwParent;

    /** True if the current row should use a special appearance. */
    private final boolean _markCurrentRow;

    /**
     * Constructor
     * 
     * @param id
     * @param columns
     * @param dataProvider
     * @param rowsPerPage
     * @param dataBinding
     * @param parent
     * @param markCurrentRow
     */
    public GozerDataTable(String id, List<IColumn<DataRowBase, String>> columns, ISortableDataProvider<DataRowBase, String> dataProvider, int rowsPerPage,
            DataBinding dataBinding,
            GWPanel parent, boolean markCurrentRow)
	{
        super(id, columns, dataProvider, rowsPerPage);
        _markCurrentRow = markCurrentRow;
        _dataBinding = dataBinding;
        _gwParent = parent;
	}

    /**
     * Default constructor
     * 
     * @param id
     *            The wicket component id
     * @param columns
     *            The tables column
     * @param dataProvider
     *            The tables data provider
     * @param rowsPerPage
     *            The number of rows to show per page
     * @param model
     *            Model wrapping the {@link com.swordlord.gozer.ui.gozerframe.GWContext} to use
     * @param list
     *            The gozer definition of the table
     * @param form
     *            The form to which this table belongs
     * @param parent
     *            The parent panel
     */
    public GozerDataTable(String id, List<IColumn<DataRowBase, String>> columns, GozerSortableFilterableDataProvider dataProvider, int rowsPerPage,
                          IModel<?> model,
                          GList list,
                          Form<?> form, GWPanel parent)
	{
		super(id, columns, dataProvider, rowsPerPage);
        _markCurrentRow = list.getShowCurrent();
        _dataBinding = list.getDataBinding();
        _gwParent = parent;

		setOutputMarkupId(true);
		setVersioned(false);
		
		addTopToolbar(new GWListActionToolbar(model, this, list, form));
		addTopToolbar(new AjaxFallbackHeadersToolbar(this, dataProvider));
        // never show the filter on a modal window (it does not work properly
        // there ...)
        // on normal window we show the filter only if desired
        if (list.getShowFilter() && !parent.getGWContext().hasModalWindow())
		{
			addTopToolbar(new GWListFilterToolbar(this, dataProvider));
		}
		addBottomToolbar(new AjaxNavigationToolbar(this));
		addBottomToolbar(new NoRecordsToolbar(this));
	}

    /**
     * {@inheritDoc}
     */
	@Override
	protected Item<DataRowBase> newRowItem(String id, int index, IModel<DataRowBase> model)
	{
		return new GozerItem(id, index, model, this);
	}
	
    /**
     * The data binding of the table.
     * 
     * @return The data binding
     */
    public DataBinding getDataBinding()
    {
        return _dataBinding;
    }

    /**
     * Returns the 'gozer' parent panel.
     * 
     * @return The parent panel
     */
    public GWPanel getGWParent()
    {
        return _gwParent;
    }

    /**
     * Returns wherever the current row should be marked/highlighted or not.
     * highlight
     * 
     * @return True to highlight it, false to let it in is default appearance
     */
    public boolean isMarkCurrentRow()
    {
        return _markCurrentRow;
    }

    /**
     * Returns the current sort state.
     * 
     * @return The sort state or null
     */
    public ISortState getSortState()
    {
        final IDataProvider<DataRowBase> provider = getDataProvider();
        if (provider instanceof ISortableDataProvider)
        {
            return ((ISortableDataProvider<DataRowBase, String>) provider).getSortState();
        }
        return null;
    }

    /**
     * Returns the current filter state.
     * 
     * @return The filter state or null
     */
    public Object getFilterState()
    {
        final IDataProvider<DataRowBase> provider = getDataProvider();
        if (provider instanceof IFilterStateLocator)
        {
            return ((IFilterStateLocator) provider).getFilterState();
        }
        return null;
    }
}