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
 ** $Id: GWAddButton.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.generic;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GAddAction;
import com.swordlord.gozer.components.generic.action.GOtherAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAjaxFallbackAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;

/**
 * Add a Button
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWOtherButton extends GWAbstractAjaxFallbackAction
{
    private String _imageIcon;
    
	/**
     * Constructor.
     * 
     * @param id
     *            Component Id
     * @param gc
     *            Gozer controller
     * @param actionBase
     *            action Base
     * @param form
     *            form
     */
	public GWOtherButton(String id, GozerController gc, GActionBase actionBase, Form<?> form)
	{
        super(id, gc, actionBase, new PackageResourceReference("img"), form);
		
		GOtherAction otherAction = (GOtherAction) actionBase;

		String desc = getTranslator().getString(getTranslationName(), otherAction.getAttribute(GOtherAction.ATTRIBUTE_DESCRIPTION));
        add(new AttributeModifier("value", new Model<String>(desc)));
        add(new AttributeModifier("alt", new ResourceModel(desc)));
        add(new AttributeModifier("title", new ResourceModel(desc)));

        if (otherAction.getAttribute(GOtherAction.ATTRIBUTE_IMAGE).toLowerCase().equals("table_gear"))
        {
            _imageIcon = Icons.ICON_TABLE_GEAR;
        }
        else
        {
            LOG.error("Image is not defined");
        }

	}

	@Override
    public String getActionID()
	{
		return GAddAction.getObjectTag();
	}

		// remain on this page, hence do not set response page
	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();

        PageParameters pp = new PageParameters();
        pp.add("id", _imageIcon);

        if (_gc.getFrameStatus() == GozerFrameStatus.EDIT && _gc.getGozerFrameExtension().canNew())
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
    public void setActionID()
	{
		// not possible in predefined GActions
	}
}
