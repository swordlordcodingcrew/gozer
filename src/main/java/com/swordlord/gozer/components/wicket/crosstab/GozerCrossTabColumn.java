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
** $Id: GozerCrossTabColumn.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.crosstab;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.datatable.GozerColumn;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GozerCrossTabColumn extends GozerColumn
{
	private boolean _bIsHeader;
	private GField _child;
	private DataBinding _dataBindingChild;
	private GWCrossTabPanel _gwCrossTabPanel;

	public GozerCrossTabColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, boolean bIsHeader,
			GField child, DataBinding dataBindingChild, GWCrossTabPanel gwCrossTabPanel) 
	{
		super(displayModel, sortProperty, false);
		
		_bIsHeader = bIsHeader;
		
		_child = child;
		_dataBindingChild = dataBindingChild;
		_gwCrossTabPanel = gwCrossTabPanel;
	}

	@Override
	public String getCssClass()
	{
		return _bIsHeader ? "header" : "";
	}

	@Override
	public Component getHeader(String strComponentId)
	{
		return super.getHeader(strComponentId);
	}

	@Override
	public void populateItem(Item<ICellPopulator<DataRowBase>> cellItem, String componentId, IModel<DataRowBase> rowModel)
	{
		cellItem.add(new GWCrossTabFieldPanel(componentId, rowModel, _child, _dataBindingChild, _gwCrossTabPanel));
	}
}
