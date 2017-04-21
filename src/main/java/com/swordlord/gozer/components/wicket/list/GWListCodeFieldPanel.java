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
 ** $Id: GWListCodeFieldPanel.java 1312 2011-12-16 19:38:15Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import java.util.Arrays;
import java.util.List;

import com.swordlord.common.prefs.UserPrefsFactory;
import com.swordlord.gozer.ui.gozerframe.GWContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.code.CodeDropDown;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.OrderingParam;

/**
 * A panel to represent a field in a Gozer list.
 */
@SuppressWarnings("serial")
public class GWListCodeFieldPanel extends GWPanel
{
    protected CodeDropDown _dropdownBox;
    protected GWContext _gwContext;

    /**
     * @param model
     *            must contain a {@link DataRowBase}.
     */
    public GWListCodeFieldPanel(String id, IModel<?> model, GCodeField field, DataBinding dataBinding, GWContext gwContext)
    {
        super(id, model);

        _gwContext = gwContext;

        final DataBindingModel dropDownModel = new DataBindingModel(dataBinding, (DataRowBase) model.getObject());

        // TODO re-add code handling
		/*
        CodeDataTable tabCode = new CodeDataTable(new DataContainer());
        tabCode.fillByLanguageAndCodeName(UserPrefsFactory.getUserPrefs().getLanguageCode(), field.getCodeType());

        OrderingParam orderingParam = new OrderingParam(CodeDataTableBase.SORT_NR_PROPERTY, true, false);
        List<DataRowBase> codes = tabCode.getDataRows(orderingParam);


        List<String> codes =  Arrays.asList("not", "implemented", "yet");

        _dropdownBox = new CodeDropDown("dropdownBox", dataBinding, dropDownModel, codes);
        _dropdownBox.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
            }
        });

        add(_dropdownBox);

        // make sure that the onclick event does not get bubbled to the
        // tr.onclick
        JsStatement jss = new JsStatement();
        jss.append("$(\"select\").click(function(event){\r\n");
        jss.append("	// stop the propagation of this event so that the row.onclick does not get called\r\n");
        jss.append("	event.stopPropagation();\r\n");
        jss.append("	});  \r\n");

        // render it on page
        JsQuery jsq = new JsQuery();
        jsq.setStatement(jss);
        jsq.contribute(this);
         */
    }

    @Override
    public void onBeforeRender()
    {
        super.onBeforeRender();

        IGozerFrameExtension gfe = _gwContext.getFrameExtension();

        if (!((DataBindingModel) _dropdownBox.getDefaultModel()).hasRows())
            _dropdownBox.setEnabled(false);
        else
            _dropdownBox.setEnabled((gfe.getGozerController().getFrameStatus() == GozerFrameStatus.EDIT)
                    || (gfe.getGozerController().getFrameStatus() == GozerFrameStatus.NEW));
    }
}
