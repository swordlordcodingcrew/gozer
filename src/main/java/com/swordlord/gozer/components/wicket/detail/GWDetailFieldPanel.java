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
** $Id: GWDetailFieldPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;

/**
 * TODO JavaDoc for GWDetailFieldPanel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWDetailFieldPanel extends GWPanel
{
    /**
	 * 
	 */
	protected AbstractTextComponent<?> _textField;
    /**
	 * 
	 */
	protected boolean isReadOnly;

    /**
     * @param id
     * @param model
     * @param fieldForm
     */
	public GWDetailFieldPanel(String id, IModel<?> model, GField fieldForm)
    {
		super(id, model);

		isReadOnly = fieldForm.isReadOnly();
	
		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(),  fieldForm.getCaption());

		final DataBinding dataBinding = fieldForm.getDataBinding();

		final DataBindingModel textfieldmodel = new DataBindingModel(dataBinding);

		DataBindingField field = dataBinding.getDataBindingField();

		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		// TODO 11.2.2008: should we use TranslatorModel for GozerFields? If, then gozer file should
		//                 have caption with resource key, not the translation itself!
		//		Label label = new Label("label", new TranslatorModel(GWDetailFieldPanel.class, "caption"));

		//AjaxEditableMultiLineLabel ta = new AjaxEditableMultiLineLabel("textArea", textfieldmodel);
        TextArea ta = new TextArea("textArea", textfieldmodel);
		add(ta);
        TextField tf = new TextField("textField", textfieldmodel);
		add(tf);

		if (field.getMaxLength() > 200)
		{
			_textField = ta;
			_textField.add(new AttributeModifier("rows", new Model(10)));
			tf.setVisible(false);
		}
		else
		{
			_textField = tf;
			ta.setVisible(false);
		}

		_textField.setRequired(field.isMandatory());
		//_textField.error("error");
		
		// This shows feedback when the name input is not correct.
        FeedbackLabel feedbackLabel = new FeedbackLabel("feedback", _textField);
        feedbackLabel.setOutputMarkupId(true);
        add(feedbackLabel);
        
        // TODO validation does not run correctly -> Disabled
        //_textField.add(new ErrorBehaviour("onblur", feedbackLabel));
	}

	@Override
	public void onBeforeRender()
	{
		 super.onBeforeRender();

		 IGozerFrameExtension gfe = getFrameExtension();

		 if (isReadOnly || !((DataBindingModel) _textField.getDefaultModel()).hasRows())
		 {
			 _textField.setEnabled(false);
		 }
		 else
		 {
			 _textField.setEnabled((gfe.getGozerController().getFrameStatus() == GozerFrameStatus.EDIT)
				 || (gfe.getGozerController().getFrameStatus() == GozerFrameStatus.NEW));
		 }
	}
}
