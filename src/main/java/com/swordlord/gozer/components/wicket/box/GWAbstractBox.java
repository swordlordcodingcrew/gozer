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
 ** $Id: GWAbstractBox.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.box;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GActionBox;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.GListAndDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GHBox;
import com.swordlord.gozer.components.generic.box.GTabbedPanel;
import com.swordlord.gozer.components.generic.box.GVBox;
import com.swordlord.gozer.components.generic.crosstab.GCrossTab;
import com.swordlord.gozer.components.generic.report.GReport;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.GWActionBox;
import com.swordlord.gozer.components.wicket.action.GWActionToolbar;
import com.swordlord.gozer.components.wicket.crosstab.GWCrossTabPanel;
import com.swordlord.gozer.components.wicket.detail.GWDetailPanel;
import com.swordlord.gozer.components.wicket.list.GWListAndDetail;
import com.swordlord.gozer.components.wicket.list.GWListPanel;
import com.swordlord.gozer.components.wicket.report.GWReport;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

/**
 * 
 */
@SuppressWarnings("serial")
public abstract class GWAbstractBox extends GWPanel
{
	public GWAbstractBox(String id, final IModel<?> model)
	{
		super(id, model);
	}

	protected Panel getPanelFromObjectBase(String id, ObjectBase ob, IModel<?> model, final Form<?> form)
	{
		if (ob.getClass().equals(GListAndDetail.class))
		{
			return new GWListAndDetail(id, model, (GListAndDetail) ob, form);
		} 
		else if (ob.getClass().equals(GActionBox.class))
		{
			return new GWActionBox(id, model, (GActionBox) ob, form);
		} 
		else if (ob.getClass().equals(GActionToolbar.class))
		{
			return new GWActionToolbar(id, model, (GActionToolbar) ob, form);
		} 
		else if (ob.getClass().equals(GCrossTab.class))
		{
			return new GWCrossTabPanel(id, model, (GCrossTab) ob);
		} 
		else if (ob.getClass().equals(GDetail.class))
		{
			return new GWDetailPanel(id, model, (GDetail) ob, form);
		} 
		else if (ob.getClass().equals(GHBox.class))
		{
			return new GWHBoxPanel(id, model, (GHBox) ob, form);
		} 
		else if (ob.getClass().equals(GList.class))
		{
			return new GWListPanel(id, model, (GList) ob, form);
		} 
		else if (ob.getClass().equals(GReport.class))
		{
			GWContext context = ((GWContext) getModelObject());
			if (context != null)
			{
				return new GWReport(id, model, (GReport) ob, context.getFrameExtension().getDataBindingContext());
			} 
			else
			{
				return new GWReport(id, model, (GReport) ob, null);
			}
		} 
		else if (ob.getClass().equals(GTabbedPanel.class))
		{
			return new GWTabbedPanel(id, model, (GTabbedPanel) ob, form);
		} 
		else if (ob.getClass().equals(GVBox.class))
		{
			return new GWVBoxPanel(id, model, (GVBox) ob, form);
		} 
		else
		{
			LOG.error("Element unknown in this context: " + ob);
			return null;
		}
	}
}
