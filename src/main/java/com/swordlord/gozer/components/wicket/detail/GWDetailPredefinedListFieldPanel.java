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

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GPredefinedListField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.wicket.PredefinedListChoiceRenderer;
import com.swordlord.jalapeno.PredefinedListHelper;

@SuppressWarnings("serial")
public class GWDetailPredefinedListFieldPanel extends GWPanel
{
    protected DropDownChoice<?> _dropdownBox;
    protected boolean isReadOnly;

    public GWDetailPredefinedListFieldPanel(String id, IModel<?> model, GPredefinedListField fieldForm)
    {
        super(id, model);

        isReadOnly = fieldForm.isReadOnly();

        final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(), fieldForm.getCaption());

        final DataBinding dataBinding = fieldForm.getDataBinding();

        DataBindingField field = dataBinding.getDataBindingField();

        Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
        add(label);

        final DataBindingModel dropDownModel = new DataBindingModel(dataBinding);

        List<String> tables = PredefinedListHelper.instance().getPredefinedList(fieldForm.getListType());

        _dropdownBox = new DropDownChoice("dropdownBox", dropDownModel, tables, new PredefinedListChoiceRenderer(field));
        add(_dropdownBox);

        // This shows feedback when the name input is not correct.
        FeedbackLabel feedbackLabel = new FeedbackLabel("feedback", _dropdownBox);
        feedbackLabel.setOutputMarkupId(true);
        add(feedbackLabel);

        // TODO Validierung läuft noch nicht sauber -> Disable
        // _dropdownBox.add(new ErrorBehaviour("onblur", feedbackLabel));
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
