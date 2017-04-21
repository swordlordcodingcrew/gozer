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
 ** $Id: GWActivateButton.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GActivateAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;

/**
 * TODO JavaDoc for GWActivateButton.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWActivateButton extends GWAbstractAction
{
	private DataRowKeyBase _key;

    /**
     * @param id
     * @param gc
     * @param actionBase
     * @param key
     */
	public GWActivateButton(String id, GozerController gc, GActionBase actionBase, DataRowKeyBase key)
	{
        super(id, gc, actionBase, new PackageResourceReference("img"));

		_key = key;

        add(new AttributeModifier("value", new ResourceModel("action.activate")));
        add(new AttributeModifier("class", new Model<String>("list_activate_button")));
	}

	@Override
    public String getActionID()
	{
		return GActivateAction.getObjectTag();
	}

	public DataRowKeyBase getKey()
	{
		return _key;
	}

	@Override
	public void onBeforeRender()
	{
		 super.onBeforeRender();
	}

	@Override
    public void setActionID()
	{
		// not possible in predefined GActions
	}
}
