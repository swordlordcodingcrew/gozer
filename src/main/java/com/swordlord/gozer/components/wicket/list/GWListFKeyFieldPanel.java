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
 ** $Id: GWListFKeyFieldPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import java.text.MessageFormat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.GWFKeyButton;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datatable.DataTableBase;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.jalapeno.fkey.FKeyBase;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;
import com.swordlord.sobf.wicket.ui.gozerframe.GozerModalWindow;

/**
 * TODO JavaDoc for GWListFKeyFieldPanel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWListFKeyFieldPanel extends GWPanel
{
    /**
	 * 
	 */
	protected Label _label;

    /**
     * @param id
     * @param model
     * @param fieldForm
     * @param dataBinding
     * @param list
     * @throws Exception
     */
	public GWListFKeyFieldPanel(String id, IModel<?> model, GField fieldForm, DataBinding dataBinding, GWListPanel list) throws Exception
	{
		super(id, model);

        final DataBindingField field = dataBinding.getDataBindingField();
		
		DataViewBase dv = dataBinding.getDataBindingManager().getDataView(dataBinding.getDataBindingMember());
		DataTableBase dt = dv.getDataTable();

		FKeyBase fkb = null;
		IGozerFrameExtension gfe = null;

		String strFieldName = field.getFieldName();
		if (dt.hasFKey(strFieldName))
		{
			fkb = dt.getFKey(strFieldName);
			if (fkb != null)
			{
				gfe = fkb.getFrameExtension((IGozerSessionInfo) getSession());
				if(gfe == null)
				{
					throw new Exception(MessageFormat.format("FrameExtension is empty for FKey {0}", fkb));
				}
			}
			else
			{
				throw new Exception(MessageFormat.format("There is no FKey for field {0} on DataTable {1}", strFieldName, dt.getAbsoluteTableName()));
			}
		}
		else
		{
			throw new Exception(MessageFormat.format("Field {0} is not an FKey but renderer created an FKey Field Panel.", field.getFieldName()));
		}
		
		DataBinding dataBindingFKey = fkb.getDisplayDataBinding(field.getObjEntity(), fieldForm, dataBinding.getDataBindingManager());

		final DataBindingModel textfieldmodel = new DataBindingModel(dataBindingFKey, (DataRowBase) model.getObject());

		_label = new Label("textFKey", textfieldmodel);
		_label.setOutputMarkupId(true);
		add(_label);

		// Add: First Selected Row (active fkey)
        GozerModalWindow _fkey = new GozerModalWindow("divFKey", gfe, dataBinding);
		add(_fkey);

		_fkey.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			@Override
            public void onClose(AjaxRequestTarget target)
			{
				// just make a refresh. the update was done by the fkey itself.
                target.add(_label);
			}
		});

		GWContext context = list.getGWContext();
		GozerController controller = context.getFrameExtension().getGozerController();

        add(new GWFKeyButton("fkey", controller, context, dataBinding, new PackageResourceReference("img"), _fkey));
	}
}
