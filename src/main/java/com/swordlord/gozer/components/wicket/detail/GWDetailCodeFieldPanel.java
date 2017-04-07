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
** $Id: GWDetailCodeFieldPanel.java 1291 2011-12-12 19:25:11Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.wicket.CodeChoiceRenderer;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.OrderingParam;
import com.swordlord.repository.datatable.Code.CodeDataTable;
import com.swordlord.repository.datatable.Code.base.CodeDataTableBase;
import com.swordlord.sobf.common.config.UserPrefs;

@SuppressWarnings("serial")
public class GWDetailCodeFieldPanel extends GWPanel
{
	protected DropDownChoice<?> _dropdownBox;
	protected boolean isReadOnly;

	public GWDetailCodeFieldPanel(String id, IModel<?> model, GCodeField fieldForm)
	{
		super(id, model);

		isReadOnly = fieldForm.isReadOnly();

		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(), fieldForm.getCaption());

		final DataBinding dataBinding = fieldForm.getDataBinding();

		DataBindingField field = dataBinding.getDataBindingField();

		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		final DataBindingModel dropDownModel = new DataBindingModel(dataBinding);

		CodeDataTable tabCode = new CodeDataTable(new DataContainer());
		tabCode.fillByLanguageAndCodeName(UserPrefs.instance().getLanguageCode(), fieldForm.getCodeType());

        OrderingParam orderingParam = new OrderingParam(CodeDataTableBase.SORT_NR_PROPERTY, true, false);
        List<DataRowBase> codes = tabCode.getDataRows(orderingParam);

		_dropdownBox = new DropDownChoice("dropdownBox", dropDownModel, codes, new CodeChoiceRenderer(field));
		add(_dropdownBox);
		
		// This shows feedback when the name input is not correct.
        FeedbackLabel feedbackLabel = new FeedbackLabel("feedback", _dropdownBox);
        feedbackLabel.setOutputMarkupId(true);
        add(feedbackLabel);
        
        //TODO Validierung läuft noch nicht sauber -> Disable
        //_dropdownBox.add(new ErrorBehaviour("onblur", feedbackLabel));
	}

	@Override
	public void onBeforeRender()
	{
		 super.onBeforeRender();

		 IGozerFrameExtension gfe = getFrameExtension();

         if (isReadOnly || !((DataBindingModel) _dropdownBox.getDefaultModel()).hasRows())
		 {
			 _dropdownBox.setEnabled(false);
		 }
		 else
		 {
			 _dropdownBox.setEnabled((gfe.getGozerController().getFrameStatus() == GozerFrameStatus.EDIT)
				 || (gfe.getGozerController().getFrameStatus() == GozerFrameStatus.NEW));
		 }
	}
}
