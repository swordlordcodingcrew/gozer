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
** $Id: GWModalOKButton.java 1361 2012-04-15 11:04:14Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.modal;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GOKAction;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

/**
 * TODO JavaDoc for GWModalOKButton.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWModalOKButton extends GWModalAction {

    /**
     * @param id
     * @param gc
     * @param context
     * @param binding
     */
	public GWModalOKButton(String id, GozerController gc, GWContext context, DataBinding binding)
	{
        super(id, gc, context, binding, new PackageResourceReference("img"));
	}

	@Override
    public String getActionID() {
		return GOKAction.getObjectTag();
	}

	@Override
	public boolean isOK()
	{
		return true;
	}

	@Override
	public void onBeforeRender()
	{
		 super.onBeforeRender();

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_ACCEPT);
        _image.setImageResourceReference(new PackageResourceReference("img"), pp);

		 setEnabled(true);
	}

	@Override
    public void setActionID() {
		// not possible in predefined GActions
	}
}
