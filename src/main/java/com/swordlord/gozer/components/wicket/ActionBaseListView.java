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
 ** $Id: ActionBaseListView.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket;

import java.util.List;

import com.swordlord.gozer.ui.gozerframe.GWContext;
import com.swordlord.jalapeno.DBGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GAddAction;
import com.swordlord.gozer.components.generic.action.GAddReferenceAction;
import com.swordlord.gozer.components.generic.action.GCancelAction;
import com.swordlord.gozer.components.generic.action.GCsvAction;
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
import com.swordlord.gozer.components.generic.action.GToggleAction;
import com.swordlord.gozer.components.wicket.action.button.GWGoBackButton;
import com.swordlord.gozer.components.wicket.action.button.GWToggleButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWAddButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWAddReferenceButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWCancelButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWCsvButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWDeleteButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWEditButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWFopButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWNewButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWOtherButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWPersistButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWRemoveButton;
import com.swordlord.gozer.components.wicket.action.button.generic.GWReportButton;
import com.swordlord.gozer.components.wicket.action.button.list.GWDetailButton;
import com.swordlord.gozer.components.wicket.action.button.modal.GWModalCancelButton;
import com.swordlord.gozer.components.wicket.action.button.modal.GWModalOKButton;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerController;


/**
 * TODO JavaDoc for ActionBaseListView.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class ActionBaseListView extends ListView<GActionBase>
{
    protected static final Log LOG = LogFactory.getLog(ActionBaseListView.class);

    private static final String ACTION_WICKET_ID = "action";
    private static final String ACTION_PANEL_WICKET_ID = "action_panel";

    private GWContext _context;
    private ObjectBase _ob;
    private GozerController _gc;
    private Form<?> _form;

    /**
     * @param id
     * @param list
     * @param context
     * @param ob
     * @param gc
     * @param form
     */
    public ActionBaseListView(String id, List<? extends GActionBase> list, GWContext context, final ObjectBase ob, GozerController gc, Form<?> form)
    {
        super(id, list);

        _context = context;
        _ob = ob;
        _gc = gc;
        _form = form;
    }

    @Override
    protected void populateItem(ListItem<GActionBase> item)
    {
        GActionBase ob = item.getModelObject();

        if (_context.isModal())
        {
            DataBinding dataBinding = new DataBinding(_ob);

            ImageButton panel = new ImageButton(ACTION_WICKET_ID, "");
            panel.setEnabled(false);
            panel.setVisible(false);
            item.add(panel);

            if (ob.getClass().equals(GCancelAction.class))
            {
                item.add(new GWModalCancelButton(ACTION_PANEL_WICKET_ID, _gc, _context, dataBinding));
            }
            else if (ob.getClass().equals(GOKAction.class))
            {
                item.add(new GWModalOKButton(ACTION_PANEL_WICKET_ID, _gc, _context, dataBinding));
            }
        }
        else
        {
            Panel panel = new EmptyPanel(ACTION_PANEL_WICKET_ID);
            panel.setEnabled(false);
            panel.setVisible(false);
            item.add(panel);

            if (ob.getClass().equals(GNewAction.class))
            {
                item.add(new GWNewButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GCancelAction.class))
            {
                item.add(new GWCancelButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GDeleteAction.class))
            {
                item.add(new GWDeleteButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GDetailAction.class))
            {
                item.add(new GWDetailButton(ACTION_WICKET_ID, _gc, ob));
            }
            else if (ob.getClass().equals(GPersistAction.class))
            {
                item.add(new GWPersistButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GGoBackAction.class))
            {
                item.add(new GWGoBackButton(ACTION_WICKET_ID, _gc, ob));
            }
            else if (ob.getClass().equals(GEditAction.class))
            {
                item.add(new GWEditButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GFopAction.class))
            {
                item.add(new GWFopButton(ACTION_WICKET_ID, _gc, ob));
            }
            else if (ob.getClass().equals(GReportAction.class))
            {
                item.add(new GWReportButton(ACTION_WICKET_ID, _gc, (GReportAction) ob));
            }
            else if (ob.getClass().equals(GCsvAction.class))
            {
                item.add(new GWCsvButton(ACTION_WICKET_ID, _gc, ob));
            }
            else if (ob.getClass().equals(GAddAction.class))
            {
                item.add(new GWAddButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GAddReferenceAction.class))
            {
                item.add(new GWAddReferenceButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GRemoveAction.class))
            {
                item.add(new GWRemoveButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GToggleAction.class))
            {
                item.add(new GWToggleButton(ACTION_WICKET_ID, _gc, ob, _form));
            }
            else if (ob.getClass().equals(GOtherAction.class))
            {
                item.add(new GWOtherButton(ACTION_WICKET_ID, _gc, ob, _form));
            }

            else
            {
                LOG.error("ActionListView can not render the current class: " + ob.getClass());
                item.add(new WebMarkupContainer(ACTION_WICKET_ID).setVisible(false));
            }
        }
    }

}