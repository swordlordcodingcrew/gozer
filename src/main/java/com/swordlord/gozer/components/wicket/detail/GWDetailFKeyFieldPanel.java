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
 ** $Id: GWDetailFKeyFieldPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.GWFKeyButton;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.datatable.DataTableBase;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.jalapeno.fkey.FKeyBase;

import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;
import com.swordlord.sobf.wicket.ui.gozerframe.GozerModalWindow;

/**
 * TODO JavaDoc for GWDetailFKeyFieldPanel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWDetailFKeyFieldPanel extends GWPanel
{
	protected boolean isReadOnly;
	protected TextField<DataBindingModel> tf;
	private GozerModalWindow _fkey;
	private DataViewBase _dv;
	
	protected static final Log LOG = LogFactory.getLog(REPLACEME);

    /**
     * @param id
     * @param model
     * @param fieldForm
     * @param detailPanel
     */
	public GWDetailFKeyFieldPanel(String id, IModel<?> model, GField fieldForm, final GWDetailPanel detailPanel)
	{
		super(id, model);

		isReadOnly = fieldForm.isReadOnly();

		final DataBinding dataBinding = fieldForm.getDataBinding();

		DataBindingField field = dataBinding.getDataBindingField();
		
		DataBindingManager dbm = dataBinding.getDataBindingManager();

		_dv = dbm.getDataView(dataBinding.getDataBindingMember());
		DataTableBase dt = _dv.getDataTable();

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
					LOG.error(MessageFormat.format("FrameExtension is empty for FKey {0}", fkb));
				}
			}
			else
			{
				LOG.error(MessageFormat.format("There is no FKey for field {0} on DataTable {1}", strFieldName, dt.getAbsoluteTableName()));
			}
		}
		else
		{
			String error = MessageFormat.format("There is no FKey information for field {1} on table {0}!", dt.getTableId(), strFieldName);
			LOG.error(error);
		}
		
		DataBinding dataBindingFKey = null;
		
		if(fkb != null)
		{
			dataBindingFKey = fkb.getDisplayDataBinding(field.getObjEntity(), fieldForm, dbm);
		}
		else
		{
			dataBindingFKey = dataBinding;
		}
		
		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(),  fieldForm.getCaption());

		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		tf = new TextField("textFKey", new DataBindingModel(dataBindingFKey));
		tf.setOutputMarkupId(true);
		tf.setEnabled(false);
		add(tf);
		
		// This shows feedback when the name input is not correct.
        FeedbackLabel nameFeedbackLabel = new FeedbackLabel("feedback", tf);
        nameFeedbackLabel.setOutputMarkupId(true);
        add(nameFeedbackLabel);

		// TODO: Selected Row in GozerModalWindow (active fkey)
        if(gfe == null)
		{
        	WebMarkupContainer container = new WebMarkupContainer("divFKey");
        	container.setVisible(false);
	        add(container);
		}
        else
        {
			_fkey = new GozerModalWindow("divFKey", gfe, dataBinding);
	        add(_fkey);
	
	        _fkey.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
	        {
	             @Override
                public void onClose(AjaxRequestTarget target)
	             {
	            	 // make sure that detail-bound record updates the child-dataviews
	            	 // otherwise the binding wont update correctly and the old data 
	            	 // will be displayed
	            	 _dv.fireCurrentRowChangedEvent();
	
	            	 // just make a refresh. the update was done by the fkey itself.
                    target.add(tf);
	             }
	        });
        }

        GWContext context = getGWContext();
        GozerController controller = context.getFrameExtension().getGozerController();

        GWFKeyButton showFKey = new GWFKeyButton("fkey", controller, getGWContext(), dataBinding, new PackageResourceReference("img"), _fkey);
        add(showFKey);
        if(gfe == null)
		{
        	showFKey.setVisible(false);
		}
	}
}
