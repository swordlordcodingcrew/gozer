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
 ** $Id: CrossTabDataTable.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.crosstab;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.datatable.GozerDataTable;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * TODO JavaDoc for CrossTabDataTable.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class CrossTabDataTable extends GozerDataTable
{
    /**
     * @param id
     * @param columns
     * @param dataProvider
     * @param rowsPerPage
     * @param dataBinding
     * @param parent
     * @param markCurrentRow
     */
    public CrossTabDataTable(String id, List<IColumn<DataRowBase, String>> columns, ISortableDataProvider<DataRowBase, String> dataProvider, int rowsPerPage,
            DataBinding dataBinding, GWPanel parent, boolean markCurrentRow)
	{
        super(id, columns, dataProvider, rowsPerPage, dataBinding, parent, markCurrentRow);

		initialise(dataProvider);
	}
	
    /**
     * @param id
     * @param columns
     * @param dataProvider
     * @param rowsPerPage
     * @param model
     * @param list
     * @param parent
     * @param markCurrentRow
     */
    public CrossTabDataTable(String id, List<IColumn<DataRowBase, String>> columns, ISortableDataProvider<DataRowBase, String> dataProvider, int rowsPerPage,
            IModel<?> model,
            GList list, GWPanel parent, boolean markCurrentRow)
	{
        super(id, columns, dataProvider, rowsPerPage, list.getDataBinding(), parent, markCurrentRow);

		initialise(dataProvider);
	}
	
    private void initialise(ISortableDataProvider<DataRowBase, String> dataProvider)
	{
		setOutputMarkupId(true);
		setVersioned(false);

		addTopToolbar(new AjaxFallbackHeadersToolbar(this, dataProvider));
		addBottomToolbar(new AjaxNavigationToolbar(this));
		addBottomToolbar(new NoRecordsToolbar(this));
	}
}