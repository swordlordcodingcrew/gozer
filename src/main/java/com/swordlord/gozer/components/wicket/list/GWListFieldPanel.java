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
 ** $Id: GWListFieldPanel.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import java.text.MessageFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * A panel to represent a field in a Gozer list.
 */
@SuppressWarnings("serial")
public class GWListFieldPanel extends GWPanel
{
	/**
	 * @param model must contain a {@link DataRowBase}.
	 */
	public GWListFieldPanel(String id, IModel<?> model, GField field, DataBinding dataBinding, GWListPanel list)
	{
		super(id, model);
		final DataBindingModel textfieldmodel = new DataBindingModel(dataBinding, (DataRowBase) model.getObject());

		int iMaxDisplayWidth = field.getMaxDisplayWidth();
		if(iMaxDisplayWidth > 0)
		{
            add(new AttributeModifier("style", new Model<String>(MessageFormat.format("max-width: {0}px;", iMaxDisplayWidth))));
		}
		
        final Label label = new Label("name", textfieldmodel);
		// TODO: add binding related information!!!
        label.add(new AttributeModifier("title", textfieldmodel));
        add(label);
	}

	public GWListFieldPanel(String id, IModel<?> model)
	{
		super(id, model);
	}
}
