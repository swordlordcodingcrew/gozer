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
 ** $Id: LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GLinkField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;

@SuppressWarnings("serial")
public class GWDetailLinkFieldPanel extends GWPanel
{
	protected ExternalLink _link;
	protected boolean isReadOnly;

	public GWDetailLinkFieldPanel(String id, IModel<?> model, GLinkField fieldForm)
	{
		super(id, model);

		isReadOnly = fieldForm.isReadOnly();

		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(),  fieldForm.getCaption());		

		final DataBinding dataBinding = fieldForm.getDataBinding();
		final DataBinding dataBindingLink = fieldForm.getDataBindingLink();

		final DataBindingModel textfieldmodel = new DataBindingModel(dataBinding);
		final DataBindingModel linkfieldmodel = new DataBindingModel(dataBindingLink);

		DataBindingField field = dataBinding.getDataBindingField();

		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		// TODO 11.2.2008: should we use TranslatorModel for GozerFields? If,
		// then gozer file should
		// have caption with resource key, not the translation itself!
		// Label label = new Label("label", new
		// TranslatorModel(GWDetailFieldPanel.class, "caption"));

		// AjaxEditableMultiLineLabel ta = new
		// AjaxEditableMultiLineLabel("textArea", textfieldmodel);

		_link = new ExternalLink("link", (IModel)linkfieldmodel);
		final Label linkText = new Label("linkText", textfieldmodel);
		linkText.setRenderBodyOnly(true);

		_link.add(linkText);
		add(_link);
	}

	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();
	}
}
