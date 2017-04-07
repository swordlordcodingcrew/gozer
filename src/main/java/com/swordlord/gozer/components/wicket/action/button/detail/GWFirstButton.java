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
 ** $Id: GWFirstButton.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.detail;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GFirstAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;

/**
 * TODO JavaDoc for GWFirstButton.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWFirstButton extends GWAbstractAction
{
    /**
     * @param id
     * @param gc
     * @param actionBase
     */
	public GWFirstButton(String id, GozerController gc, GActionBase actionBase)
	{
        super(id, gc, actionBase, new PackageResourceReference("img"));
        add(new AttributeModifier("value", new Model<String>("First")));
	}

	@Override
    public String getActionID() {
		return GFirstAction.getObjectTag();
	}

	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();
		
        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_ACTIONS_MOVE_FIRST);

		boolean bEnabled = true;
		
		if(getDataBinding().getPosition() <= 0)
		{
            pp.add("inactive", 1); // add the inactivity filter
            bEnabled = false;
		}
		
        setImageResourceReference(new PackageResourceReference("img"), pp);

		setEnabled(bEnabled);
	}


	@Override
    public void setActionID() {
		// not possible in predefined GActions
	}
}
