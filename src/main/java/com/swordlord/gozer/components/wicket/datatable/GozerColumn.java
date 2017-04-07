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
** $Id: GozerColumn.java 1363 2012-10-19 15:22:22Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * TODO JavaDoc for GozerColumn.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GozerColumn extends AbstractColumn<DataRowBase, String> implements IColumn<DataRowBase, String>
{
	private boolean _bIsFirstRow;
	
    /**
     * Constructor
     * 
     * @param displayModel
     * @param sortProperty
     * @param bIsFirstRow
     */
	public GozerColumn(IModel<String> displayModel, String sortProperty, Boolean bIsFirstRow) 
	{
		super(displayModel, sortProperty);
		
		_bIsFirstRow = bIsFirstRow;
	}
	
    /**
     * Constructor
     * 
     * @param displayModel
     * @param bIsFirstRow
     */
	public GozerColumn(IModel<String> displayModel, Boolean bIsFirstRow) 
	{
		super(displayModel);
		
		_bIsFirstRow = bIsFirstRow;
	}

	@Override
	public String getCssClass()
	{
		return _bIsFirstRow ? "first" : "";
	}

	@Override
	public Component getHeader(String strComponentId)
	{
		return super.getHeader(strComponentId);
	}

	@Override
    public void populateItem(Item<ICellPopulator<DataRowBase>> cellItem, String componentId, IModel<DataRowBase> rowModel) 
	{
		cellItem.add(new Label(componentId, rowModel));
	}
}
