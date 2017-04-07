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
 ** $Id: GWPersistButton.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.generic;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.common.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GPersistAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAjaxFallbackAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;

/**
 * TODO JavaDoc for GWPersistButton.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWPersistButton extends GWAbstractAjaxFallbackAction
{
    /**
     * @param id
     * @param gc
     * @param actionBase
     * @param form
     */
	public GWPersistButton(String id, GozerController gc, GActionBase actionBase, Form<?> form)
	{
        super(id, gc, actionBase, new PackageResourceReference("img"), form);
        add(new AttributeModifier("value", new Model<String>("Persist")));
        add(new AttributeModifier("alt", new ResourceModel("action.persist")));
        add(new AttributeModifier("title", new ResourceModel("action.persist")));
	}

	@Override
    public String getActionID() {
		return GPersistAction.getObjectTag();
	}

	@Override
	public void onBeforeRender()
	{
        super.onBeforeRender();

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_ACTIONS_SAVE);

        GozerFrameStatus gfs = _gc.getFrameStatus();
        if (((gfs == GozerFrameStatus.EDIT) || (gfs == GozerFrameStatus.NEW)) && _gc.getGozerFrameExtension().canPersist())
        {
            setImageResourceReference(new PackageResourceReference("img"), pp);

            setEnabled(true);
        }
        else
        {
            pp.add("inactive", 1); // add the inactivity filter
            setImageResourceReference(new PackageResourceReference("img"), pp);

            setEnabled(false);
        }
	}

	@Override
    public void setActionID() {
		// not possible in predefined GActions
	}
}
