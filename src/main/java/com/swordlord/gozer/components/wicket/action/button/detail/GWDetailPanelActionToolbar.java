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
package com.swordlord.gozer.components.wicket.action.button.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GAddAction;
import com.swordlord.gozer.components.generic.action.GDeleteAction;
import com.swordlord.gozer.components.generic.action.GFirstAction;
import com.swordlord.gozer.components.generic.action.GLastAction;
import com.swordlord.gozer.components.generic.action.GNewAction;
import com.swordlord.gozer.components.generic.action.GNextAction;
import com.swordlord.gozer.components.generic.action.GOtherAction;
import com.swordlord.gozer.components.generic.action.GPrevAction;
import com.swordlord.gozer.components.generic.action.GRemoveAction;
import com.swordlord.gozer.components.generic.action.GToggleAction;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.GWSwitchToListButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWAddButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWDeleteButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWNewButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWOtherButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWRemoveButton;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

@SuppressWarnings("serial")
public class GWDetailPanelActionToolbar extends GWPanel
{
    private static final String ACTION_WICKET_ID = "action";

    protected GDetail _detail;

    public GWDetailPanelActionToolbar(String name, IModel<GWContext> model, GDetail detail, final Form<?> form)
    {
        super(name, model);

        _detail = detail;

        final GWContext context = getGWContext();
        final GozerController gc = context.getFrameExtension().getGozerController();

        List<GActionBase> actions = getKnownActions();

        ListView<GActionBase> listView = new ListView<GActionBase>("eachAction", actions)
        {
            @Override
            protected void populateItem(ListItem<GActionBase> item)
            {
                GActionBase ob = item.getModelObject();

                if (ob.getClass().equals(GNewAction.class))
                {
                    item.add(new GWNewButton(ACTION_WICKET_ID, gc, ob, form));
                }
                else if (ob.getClass().equals(GDeleteAction.class))
                {
                    item.add(new GWDeleteButton(ACTION_WICKET_ID, gc, ob, form));
                }
                else if (ob.getClass().equals(GAddAction.class))
                {
                    item.add(new GWAddButton(ACTION_WICKET_ID, gc, ob, form));
                }
                else if (ob.getClass().equals(GRemoveAction.class))
                {
                    item.add(new GWRemoveButton(ACTION_WICKET_ID, gc, ob, form));
                }
                else if (ob.getClass().equals(GToggleAction.class))
                {
                    item.add(new GWSwitchToListButton(ACTION_WICKET_ID, gc, ob));
                }
                else if (ob.getClass().equals(GFirstAction.class))
                {
                    item.add(new GWFirstButton(ACTION_WICKET_ID, gc, ob));
                }
                else if (ob.getClass().equals(GPrevAction.class))
                {
                    item.add(new GWPrevButton(ACTION_WICKET_ID, gc, ob));
                }
                else if (ob.getClass().equals(GNextAction.class))
                {
                    item.add(new GWNextButton(ACTION_WICKET_ID, gc, ob));
                }
                else if (ob.getClass().equals(GLastAction.class))
                {
                    item.add(new GWLastButton(ACTION_WICKET_ID, gc, ob));
                }
                else if (ob.getClass().equals(GOtherAction.class))
                {
                    item.add(new GWOtherButton(ACTION_WICKET_ID, gc, ob, form));
                }
            }
        };
        listView.setReuseItems(true);
        add(listView);

        if (actions.size() == 0)
        {
            listView.setVisible(false);
            this.setVisible(false);
        }
    }

    private GActionToolbar searchActionToolbar(GDetail list)
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

    private Boolean searchActionItem(GActionToolbar actionToolbar, String itemClass)
    {
        Boolean itemFound = false;
        ListIterator<ObjectBase> iter = actionToolbar.getChildren().listIterator();
        while (iter.hasNext())
        {
            ObjectBase obj = iter.next();
            if (obj.getClass().getSimpleName().equals(itemClass))
            {
                itemFound = true;
            }
        }
        return itemFound;
    }

    private ObjectBase searchActionItemObject(GActionToolbar actionToolbar, String itemClass)
    {
        ObjectBase objBase = null;
        ListIterator<ObjectBase> iter = actionToolbar.getChildren().listIterator();
        while (iter.hasNext())
        {
            ObjectBase obj = iter.next();
            if (obj.getClass().getSimpleName().equals(itemClass))
            {
                objBase = obj;
            }
        }
        return objBase;
    }

    private List<GActionBase> getKnownActions()
    {
        List<GActionBase> actions = new ArrayList<GActionBase>();

        GActionToolbar actionToolbar = searchActionToolbar(_detail);
        if (actionToolbar != null)
        {
            ObjectTree ot = actionToolbar.getObjectTree();

            if (searchActionItem(actionToolbar, GToggleAction.class.getSimpleName()))
            {
                GToggleAction action = new GToggleAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GNewAction.class.getSimpleName()))
            {
                GNewAction action = new GNewAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GDeleteAction.class.getSimpleName()))
            {
                GDeleteAction action = new GDeleteAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GAddAction.class.getSimpleName()))
            {
                // Take the available GAddAction Object and not crreate a new
                // GAddAction, else are properties like identifier not taken.
                GAddAction action = (GAddAction) searchActionItemObject(actionToolbar, GAddAction.class.getSimpleName());
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GRemoveAction.class.getSimpleName()))
            {
                // Take the available GRemoveAction Object and not crreate a new
                // GRemoveAction, else are properties like identifier not taken.
                GRemoveAction action = (GRemoveAction) searchActionItemObject(actionToolbar, GRemoveAction.class.getSimpleName());
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GOtherAction.class.getSimpleName()))
            {
                GOtherAction action = (GOtherAction) searchActionItemObject(actionToolbar, GOtherAction.class.getSimpleName());
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GFirstAction.class.getSimpleName()))
            {
                GFirstAction action = new GFirstAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GPrevAction.class.getSimpleName()))
            {
                GPrevAction action = new GPrevAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GNextAction.class.getSimpleName()))
            {
                GNextAction action = new GNextAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
            if (searchActionItem(actionToolbar, GLastAction.class.getSimpleName()))
            {
                GLastAction action = new GLastAction(ot);
                Parser.inherit(actionToolbar, action);
                actions.add(action);
            }
        }

        return actions;
    }
}
