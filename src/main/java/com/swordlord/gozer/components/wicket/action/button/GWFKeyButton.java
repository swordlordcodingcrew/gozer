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
** $Id: GWFKeyButton.java 1361 2012-04-15 11:04:14Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.common.ui.icons.Icons;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;
import com.swordlord.sobf.wicket.ui.modal.ModalWindowEx;

/**
 * TODO JavaDoc for GWFKeyButton.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWFKeyButton extends GWAjaxLinkAction
{
	private ModalWindowEx _modalWindow;

    /**
     * @param id
     * @param gc
     * @param context
     * @param binding
     * @param resource
     * @param modalWindow
     */
    public GWFKeyButton(String id, GozerController gc, GWContext context, DataBinding binding, PackageResourceReference resource, ModalWindowEx modalWindow)
	{
		super(id, gc, context, binding, resource);

		_modalWindow = modalWindow;
	}

	@Override
    public String getActionID()
	{
		return null;
	}

	@Override
 	public void onBeforeRender()
 	{
 		 super.onBeforeRender();
 		 
 		 GozerFrameStatus status = _context.getFrameExtension().getGozerController().getFrameStatus();

 		DataViewBase dv = _binding.getDataBindingManager().getDataView(_binding.getDataBindingMember()); 
 		
        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_TABLE_RELATIONSHIP);

 		 if (((status == GozerFrameStatus.EDIT) || (status == GozerFrameStatus.NEW))
                && dv.size() > 0)
 		 {
            _image.setImageResourceReference(new PackageResourceReference("img"), pp);

 			 _link.setEnabled(true);
 			 setEnabled(true);
 		 }
 		 else
 		 {
            pp.add("inactive", 1); // add the inactivity filter
            _image.setImageResourceReference(new PackageResourceReference("img"), pp);

			 _link.setEnabled(false);
 			 setEnabled(false);
 		 }
 	}

	@Override
	public void onLinkClick(AjaxRequestTarget target)
	{
		// only works in edit mode!
	    _modalWindow.show(target);
	}

	@Override
    public void setActionID()
	{
		// not possible in predefined GActions
	}
}