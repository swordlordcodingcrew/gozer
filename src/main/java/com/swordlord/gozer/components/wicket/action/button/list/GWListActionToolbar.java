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
 ** $Id: GWListActionToolbar.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GAddAction;
import com.swordlord.gozer.components.generic.action.GAddReferenceAction;
import com.swordlord.gozer.components.generic.action.GDeleteAction;
import com.swordlord.gozer.components.generic.action.GNewAction;
import com.swordlord.gozer.components.generic.action.GOtherAction;
import com.swordlord.gozer.components.generic.action.GRemoveAction;
import com.swordlord.gozer.components.generic.action.GToggleAction;
import com.swordlord.gozer.components.wicket.ActionBaseListView;
import com.swordlord.gozer.components.wicket.datatable.GozerDataTable;
import com.swordlord.gozer.eventhandler.generic.GozerController;

import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

/**
 * TODO JavaDoc for GWListActionToolbar.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWListActionToolbar extends AbstractToolbar
{
	protected static final Log LOG = LogFactory.getLog(REPLACEME);
	
	private static final Set<Class<? extends ObjectBase>> KNOW_ACTIONS = new HashSet<Class<? extends ObjectBase>>();

	static
	{
		KNOW_ACTIONS.add(GNewAction.class);
		KNOW_ACTIONS.add(GDeleteAction.class);
		KNOW_ACTIONS.add(GAddAction.class);
		KNOW_ACTIONS.add(GAddReferenceAction.class);
		KNOW_ACTIONS.add(GRemoveAction.class);
		KNOW_ACTIONS.add(GToggleAction.class);
		KNOW_ACTIONS.add(GOtherAction.class);
	}

	protected GList _list;

	public GWContext getGWContext()
	{
		return (GWContext) getModelObject();
	}

	protected Object getModelObject()
	{
		return getDefaultModelObject();
	}

	public GWListActionToolbar(IModel<?> model, GozerDataTable dataTable, GList list, final Form<?> form)
	{
		super(model, dataTable);
		
        this.setAuto(true);

		WebMarkupContainer tr = new WebMarkupContainer("tr");
		add(tr);

		WebMarkupContainer td = new WebMarkupContainer("td");
		tr.add(td);

        td.add(new AttributeModifier("colspan", new Model<String>(String.valueOf(dataTable.getColumns().size()))));

		_list = list;

		final GWContext context = getGWContext();
		final GozerController gc = context.getFrameExtension().getGozerController();
		
		ObjectBase at = searchActionToolbar(list);

		List<GActionBase> actions = filterKnownActions(at);
		ActionBaseListView listView = new ActionBaseListView("eachAction", actions, context, list, gc, form);
		listView.setReuseItems(true);
		
		td.add(listView);
		
		if(actions.size() == 0)
		{
			tr.setVisible(false);
		}
	}

	private GActionToolbar searchActionToolbar(GList list)
	{
		GActionToolbar actionToolbar = null;
		ListIterator<ObjectBase> iter = list.getChildren().listIterator();
		while (iter.hasNext() && actionToolbar == null)
		{
			ObjectBase obj = iter.next();
			if (obj instanceof GActionToolbar)
			{
				actionToolbar = (GActionToolbar) obj;
			}
		}
		return actionToolbar;
	}

	
	protected List<GActionBase> filterKnownActions(ObjectBase parent)
	{
		List<GActionBase> actions = new ArrayList<GActionBase>();
		if(parent == null) 
		{
			return actions;
		}
		
		LinkedList<ObjectBase> children = parent.getChildren();
		
		ObjectBase[] arrKiddies = children.toArray(new ObjectBase[0]);
		for (ObjectBase ob : arrKiddies)
		{
			if(KNOW_ACTIONS.contains(ob.getClass()))
			{
				actions.add((GActionBase)ob);
			}
		}
		
		return actions;
	}
}
