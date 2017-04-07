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
 ** $Id: GWListAndDetail.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.GListAndDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.GWSwitchToDetailButton;
import com.swordlord.gozer.components.wicket.action.button.GWSwitchToListButton;
import com.swordlord.gozer.components.wicket.action.button.GWToggleButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWNewButton;
import com.swordlord.gozer.components.wicket.detail.GWDetailPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.eventhandler.generic.GozerEvent;
import com.swordlord.gozer.eventhandler.generic.GozerEventListener;
import com.swordlord.gozer.eventhandler.generic.GozerFeedbackInfoEvent;
import com.swordlord.gozer.eventhandler.generic.GozerSelectionEvent;
import com.swordlord.gozer.eventhandler.generic.GozerUpdateUIEvent;
import com.swordlord.jalapeno.dataview.OrderingParam;

/**
 * Implements gozer element 'listanddetail'. Supports GDetail, GList and
 * GActionBox.
 */
@SuppressWarnings("serial")
public class GWListAndDetail extends GWPanel
{
	private class GozerListener implements GozerEventListener
	{
		@Override
        public void gozerEventPerformed(GozerEvent ge)
		{
			if (ge.getSource() instanceof GozerController)
			{
				// have the children re-render their content
				detailPanel.updateComponents();
			}
		}

		@Override
        public void gozerSelectionPerformed(GozerSelectionEvent gse)
		{
		}

		@Override
        public void gozerUpdateUIPerformed(GozerUpdateUIEvent gui)
		{
			Object source = gui.getSource();
			if (source == null)
				return;

			if (source instanceof GWToggleButton)
			{
				switchEditListMode();
			} 
			else if (source instanceof GWSwitchToDetailButton)
			{
				setDetailMode(gui.getDataBindingPath());
			} 
			else if (source instanceof GWSwitchToListButton)
			{
				setListMode();
			} 
			else if (source instanceof GWNewButton)
			{
				setDetailMode(gui.getDataBindingPath());
			}
		}

		@Override
        public void gozerFeedbackInfoPerformed(GozerFeedbackInfoEvent gui, String message)
		{
			sendFeedbackInfo(message);
		}
	}

	private GWDetailPanel detailPanel;

	private GWListPanel listPanel;

	private boolean listMode = true;
	
	private String _dataBindingPathName;

	public GWListAndDetail(String id, IModel<?> model, GListAndDetail gfListAndDetail, final Form<?> form)
	{
		super(id, model);
		
		DataBinding binding = gfListAndDetail.getDataBinding();
		_dataBindingPathName = binding.getDataBindingPathName();
		
		// TODO add ascending/descending to ordering
		String strOrdering = gfListAndDetail.getOrdering();
		if ((strOrdering != null) && (strOrdering.length() > 0))
		{
            binding.setOrdering(new OrderingParam(strOrdering, true, false));
		}

		for (ObjectBase child : gfListAndDetail.getChildren())
		{
			if (child.getClass().equals(GDetail.class))
			{
				detailPanel = new GWDetailPanel("detail", getModel(), (GDetail) child, form);
				detailPanel.setVisible(!listMode);
				add(detailPanel);
			} 
			else if (child.getClass().equals(GList.class))
			{
				listPanel = new GWListPanel("list", getModel(), (GList) child, form);
				listPanel.setVisible(listMode);
                // suppress the lists gozer feedback - this component processes
                // them
                // already
                // listPanel.setShowGozerFeedback(false);
				add(listPanel);
			}
		}

        WebMarkupContainer label = new WebMarkupContainer("toolbar");
        label.setVisible(false);
        add(label);

		GozerController gc = getGWContext().getFrameExtension().getGozerController();
		gc.addGozerEventListener(new GozerListener());
	}

	public void setDetailMode(String dataBindingPathName)
	{
		if (dataBindingPathName == null || dataBindingPathName.equals(_dataBindingPathName))
		{
			listMode = false;
			updateVisibilityChildren();
		}
	}

	public void setListMode()
	{
		listMode = true;
		updateVisibilityChildren();
	}

	public void switchEditListMode()
	{
		listMode = !listMode;
		updateVisibilityChildren();
	}

	private void updateVisibilityChildren()
	{
		listPanel.setVisible(listMode);
		detailPanel.setVisible(!listMode);
	}
	
	private void sendFeedbackInfo(String message)
	{
		info(message);
	}
}
