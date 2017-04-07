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
package com.swordlord.gozer.components.wicket.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GAddAction;
import com.swordlord.gozer.components.generic.action.GAddReferenceAction;
import com.swordlord.gozer.components.generic.action.GCancelAction;
import com.swordlord.gozer.components.generic.action.GCsvAction;
import com.swordlord.gozer.components.generic.action.GDefaultFrameActions;
import com.swordlord.gozer.components.generic.action.GDeleteAction;
import com.swordlord.gozer.components.generic.action.GDetailAction;
import com.swordlord.gozer.components.generic.action.GEditAction;
import com.swordlord.gozer.components.generic.action.GFopAction;
import com.swordlord.gozer.components.generic.action.GGoBackAction;
import com.swordlord.gozer.components.generic.action.GNewAction;
import com.swordlord.gozer.components.generic.action.GOKAction;
import com.swordlord.gozer.components.generic.action.GOtherAction;
import com.swordlord.gozer.components.generic.action.GPersistAction;
import com.swordlord.gozer.components.generic.action.GRemoveAction;
import com.swordlord.gozer.components.generic.action.GReportAction;
import com.swordlord.gozer.components.wicket.GWPanel;

@SuppressWarnings("serial")
public abstract class GWAbstractActionContainer extends GWPanel
{
	private static final Set<Class<? extends ObjectBase>> KNOW_ACTIONS = new HashSet<Class<? extends ObjectBase>>();

	private static final Set<Class<? extends ObjectBase>> MODAL_ACTIONS = new HashSet<Class<? extends ObjectBase>>();

	static
	{
		KNOW_ACTIONS.add(GNewAction.class);
		KNOW_ACTIONS.add(GCancelAction.class);
		KNOW_ACTIONS.add(GDeleteAction.class);
		KNOW_ACTIONS.add(GDefaultFrameActions.class);
		KNOW_ACTIONS.add(GDetailAction.class);
		KNOW_ACTIONS.add(GGoBackAction.class);
		KNOW_ACTIONS.add(GPersistAction.class);
		KNOW_ACTIONS.add(GEditAction.class);
		KNOW_ACTIONS.add(GFopAction.class);
		KNOW_ACTIONS.add(GReportAction.class);
		KNOW_ACTIONS.add(GCsvAction.class);
		KNOW_ACTIONS.add(GAddAction.class);
		KNOW_ACTIONS.add(GAddReferenceAction.class);
		KNOW_ACTIONS.add(GRemoveAction.class);
		KNOW_ACTIONS.add(GOtherAction.class);
	}

	static
	{
		MODAL_ACTIONS.add(GCancelAction.class);
		MODAL_ACTIONS.add(GOKAction.class);
	}

	public GWAbstractActionContainer(String id, IModel<?> model)
	{
		super(id, model);
	}

	protected List<GActionBase> filterKnownActions(ObjectBase parent, LinkedList<ObjectBase> children)
	{
		List<GActionBase> actions = new ArrayList<GActionBase>();
		
		ObjectBase[] arrKiddies = children.toArray(new ObjectBase[0]);
		for (ObjectBase ob : arrKiddies)
		{
			if(ob.getClass().equals(GDefaultFrameActions.class))
			{
				// This is actually a placeholder for different buttons/actions
				LOG.info("Adding default GDefaultFrameAction to this ActionContainer");
				
				ObjectTree ot = ob.getObjectTree();
				
				GEditAction ea = new GEditAction(ot);
				Parser.inherit(parent, ea);
				actions.add(ea);
				
				GCancelAction ca = new GCancelAction(ot);
				Parser.inherit(parent, ca);
				actions.add(ca);

				GPersistAction pa = new GPersistAction(ot);
				Parser.inherit(parent, pa);
				actions.add(pa);

				GFopAction fa = new GFopAction(ot);
				Parser.inherit(parent, fa);
				actions.add(fa);

				GCsvAction csa = new GCsvAction(ot);
				Parser.inherit(parent, csa);
				actions.add(csa);
			}
			else if(KNOW_ACTIONS.contains(ob.getClass()))
			{
				actions.add((GActionBase)ob);
			}
		}
		
		return actions;
	}

	protected List<GActionBase> getModalActions(ObjectBase parent, ObjectTree root)
	{
		List<GActionBase> actions = new ArrayList<GActionBase>();
		
		GOKAction oa = new GOKAction(root);
		Parser.inherit(parent, oa);
		actions.add(oa);
		
		GCancelAction ca = new GCancelAction(root);
		Parser.inherit(parent, ca);
		actions.add(ca);

		return actions;
	}
}
