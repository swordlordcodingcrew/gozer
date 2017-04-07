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
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;

@SuppressWarnings("serial")
public class GWDetailBooleanFieldPanel extends GWPanel
{
	protected CheckBox _checkBox;
	protected boolean isReadOnly;

	public GWDetailBooleanFieldPanel(String id, IModel<?> model, GField fieldForm)
	{
		super(id, model);

		isReadOnly = fieldForm.isReadOnly();

		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(),  fieldForm.getCaption());		

		final DataBinding dataBinding = fieldForm.getDataBinding();

		DataBindingField field = dataBinding.getDataBindingField();

		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		final DataBindingModel checkBoxModel = new DataBindingModel(dataBinding);

		
		_checkBox = new CheckBox("checkBox");
		_checkBox.setDefaultModel(checkBoxModel);
		add(_checkBox);
		
		// This shows feedback when the name input is not correct.
        FeedbackLabel feedbackLabel = new FeedbackLabel("feedback", _checkBox);
        feedbackLabel.setOutputMarkupId(true);
        add(feedbackLabel);
        
        //TODO Validierung läuft noch nicht sauber -> Disable
        //_checkBox.add(new ErrorBehaviour("onblur", feedbackLabel));
	}

	@Override
	public void onBeforeRender()
	{
		 super.onBeforeRender();

		 IGozerFrameExtension gfe = getFrameExtension();

         if (isReadOnly || !((DataBindingModel) _checkBox.getDefaultModel()).hasRows())
		 {
			 _checkBox.setEnabled(false);
		 }
		 else
		 {
			 _checkBox.setEnabled((gfe.getGozerController().getFrameStatus() == GozerFrameStatus.EDIT)
				 || (gfe.getGozerController().getFrameStatus() == GozerFrameStatus.NEW));
		 }
	}
}
