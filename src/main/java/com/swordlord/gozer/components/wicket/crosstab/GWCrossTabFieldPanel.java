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
** $Id: GWCrossTabFieldPanel.java 1312 2011-12-16 19:38:15Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.crosstab;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.list.GWListFieldPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * A panel to represent a field in a Gozer list.
 */
@SuppressWarnings("serial")
public class GWCrossTabFieldPanel extends GWListFieldPanel
{
	/**
	 * @param model must contain a {@link DataRowBase}.
	 */
	public GWCrossTabFieldPanel(String id, IModel<?> model, GField field, DataBinding dataBinding, GWCrossTabPanel list)
	{
		super(id, model);
        final DataRowBase row = (DataRowBase) model.getObject();
		add(new Label("name", row.getPropertyAsString(field.getDataBinding().getDataBindingFieldName())));
	}
}
