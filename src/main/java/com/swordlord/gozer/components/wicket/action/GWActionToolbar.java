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
 ** $Id: GWActionToolbar.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.wicket.ActionBaseListView;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

@SuppressWarnings("serial")
public class GWActionToolbar extends GWAbstractActionContainer
{
	protected GActionToolbar _actionToolbar;

	public GWActionToolbar(String id, IModel<?> model, GActionToolbar actionToolbar, final Form<?> form)
	{
		super(id, model);

		_actionToolbar = actionToolbar;

		final GWContext context = getGWContext();
		final GozerController gc = context.getFrameExtension().getGozerController();

		final boolean isModal = context.isModal();

		List<GActionBase> actions = null;
		
		if (!isModal)
		{
			actions = filterKnownActions(actionToolbar, actionToolbar.getChildren());
		} else
		{
			actions = getModalActions(actionToolbar, actionToolbar.getObjectTree());
		}

		final ObjectBase obAB = actionToolbar;
		
		WebMarkupContainer toolbar = new WebMarkupContainer("action_toolbar");
		add(toolbar);
		
		ActionBaseListView listView = new ActionBaseListView("eachAction", actions, context, obAB, gc, form);
		listView.setReuseItems(true);		
		toolbar.add(listView);
		
		if(actions.size() == 0)
		{
			toolbar.setVisible(false);
			this.setVisible(false);
		}
	}
}
