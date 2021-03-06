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
** $Id: GWAjaxLinkAction.java 1361 2012-04-15 11:04:14Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button;

import com.swordlord.gozer.ui.gozerframe.GWContext;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.session.IGozerSessionInfo;

@SuppressWarnings("serial")
public abstract class GWAjaxLinkAction extends Panel implements IGozerAction
{
	protected static final Log LOG = LogFactory.getLog(GWAjaxLinkAction.class);

	protected GozerController _gc;
	protected DataBinding _binding;
	protected GWContext _context;

	protected Image _image;
	protected AjaxLink<?> _link;

    public GWAjaxLinkAction(String id, GozerController gc, GWContext context, DataBinding binding, PackageResourceReference resource)
	{
		super(id);

		_gc = gc;
		_binding = binding;
		_context = context;

		_image = new Image("image", resource);

		_link = new AjaxLink<Object>("link")
		{
			@Override
		    public void onClick(AjaxRequestTarget target)
		    {
				onLinkClick(target);
		    }
	    };

		_link.add(_image);

		add(_link);
	}

	@Override
    public DataBinding getDataBinding()
	{
		return _binding;
	}
	
	@Override
    public DataBinding getDataBindingTwo()
	{
		throw new NotImplementedException();
	}

	public GozerController getGozerController()
	{
		return _gc;
	}

	public IGozerSessionInfo getGozerSession()
	{
		return (IGozerSessionInfo) getSession();
	}

	public boolean isOK()
	{
		return false;
	}

	protected abstract void onLinkClick(AjaxRequestTarget target);
}