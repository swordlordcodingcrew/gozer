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
 ** $Id: GWListLinkFieldPanel.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * A panel to represent a linkfield in a Gozer list.
 */
@SuppressWarnings("serial")
public class GWListLinkFieldPanel extends GWPanel
{
	/**
	 * @param model
	 *            must contain a {@link DataRowBase}.
	 */
	@SuppressWarnings("unchecked")
	public GWListLinkFieldPanel(String id, IModel<?> model, GField field, DataBinding dataBinding, DataBinding dataBindingLink, GWListPanel list)
	{
		super(id, model);

		DataRowBase row = (DataRowBase) model.getObject();
		final DataBindingModel textfieldmodel = new DataBindingModel(dataBinding, row);
		final DataBindingModel linkfieldmodel = new DataBindingModel(dataBindingLink, row);

        final ExternalLink contentLink = new ExternalLink("link", (IModel) linkfieldmodel);
		final Label linkText = new Label("linkText", textfieldmodel);
		linkText.setRenderBodyOnly(true);

        contentLink.add(linkText);

		// TODO: add binding related information!!!
        contentLink.add(new AttributeModifier("title", textfieldmodel));
        add(contentLink);
	}
}
