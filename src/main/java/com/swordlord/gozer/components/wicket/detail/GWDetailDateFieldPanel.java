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
 ** $Id: GWDetailDateFieldPanel.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.datepicker.DatePicker.ShowOnEnum;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;

/**
 * TODO JavaDoc for GWDetailDateFieldPanel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWDetailDateFieldPanel extends GWPanel
{
    /**
	 * 
	 */
	protected DatePicker<Date> _datePicker;
    /**
	 * 
	 */
	protected boolean isReadOnly;

    /**
     * Constructor
     * 
     * @param id
     * @param model
     * @param fieldForm
     */
	public GWDetailDateFieldPanel(String id, IModel<?> model, GField fieldForm)
	{
		super(id, model);

		isReadOnly = fieldForm.isReadOnly();

		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(),  fieldForm.getCaption());

		final DataBinding dataBinding = fieldForm.getDataBinding();

		final DataBindingModel textfieldmodel = new DataBindingModel(dataBinding);

		DataBindingField field = dataBinding.getDataBindingField();

		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		_datePicker = new DatePicker<Date>("datePicker");
		_datePicker.setDefaultModel(textfieldmodel);
		_datePicker.setShowButtonPanel(true);
		_datePicker.setShowOn(ShowOnEnum.FOCUS);
		add(_datePicker);
		
        Image image = new Image("cmdShowDatePicker", "");

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_DATE);

        image.setImageResourceReference(new PackageResourceReference("img"), pp);
		image.setOutputMarkupPlaceholderTag(true);
		
		WiQueryEventBehavior event = new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;

			/* (non-Javadoc)
			 * @see org.odlabs.wiquery.core.events.Event#callback()
			 */
			@Override
			public JsScope callback() {
				return JsScope.quickScope(_datePicker.show().render());
			}
		});	
		event.bind(image);
		
		/*
		image.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;

			/* (non-Javadoc)
			 * @see org.odlabs.wiquery.core.events.Event#callback()
			 
			@Override
			public JsScope callback() {
				return JsScope.quickScope(_datePicker.show().render());
			}
		}));		
		*/
		add(image);
		
		
		// This shows feedback when the name input is not correct.
        FeedbackLabel feedbackLabel = new FeedbackLabel("feedback", _datePicker);
        feedbackLabel.setOutputMarkupId(true);
        add(feedbackLabel);
        
        //TODO Validierung läuft noch nicht sauber -> Disable
        //_datePicker.add(new ErrorBehaviour("onblur", feedbackLabel));
	}

	@Override
	public void onBeforeRender()
	{
		 super.onBeforeRender();

		 IGozerFrameExtension gfe = getFrameExtension();

         if (isReadOnly || !((DataBindingModel) _datePicker.getDefaultModel()).hasRows())
		 {
			 _datePicker.setEnabled(false);
		 }
		 else
		 {
			 _datePicker.setEnabled((gfe.getGozerController().getFrameStatus() == GozerFrameStatus.EDIT)
				 || (gfe.getGozerController().getFrameStatus() == GozerFrameStatus.NEW));
		 }
	}
}
