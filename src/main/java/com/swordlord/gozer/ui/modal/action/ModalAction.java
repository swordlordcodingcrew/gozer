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
** $Id:  $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.ui.modal.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.modal.ModalWindowEx;

@SuppressWarnings("serial")
public abstract class ModalAction extends Panel
{
	protected static final Log LOG = LogFactory.getLog(ModalAction.class);

	protected Image _image;

    public ModalAction(String id, PackageResourceReference resource, final ModalWindowEx mw)
	{
		super(id);

		AjaxLink<?> link = new AjaxLink<Object>("link")
	    {
	        @Override
	        public void onClick(AjaxRequestTarget target)
	        {
	        	// HACK:
	        	// Oh my, what a hack... clean this up...
	        	if(mw != null)
	        	{
	        		boolean bIsOK = isOK();

	        		if(bIsOK)
	        		{
	        			//window.setSelectedRow(dv.getCurrentRow());
	        		}
	        		mw.close(target, bIsOK);
	        	}
	        }
	    };

		_image = new Image("image", resource);

		link.add(_image);

		add(link);
	}

	public boolean isOK()
	{
		return false;
	}
}