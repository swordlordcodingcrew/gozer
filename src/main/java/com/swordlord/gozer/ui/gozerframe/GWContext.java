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
package com.swordlord.gozer.ui.gozerframe;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.swordlord.gozer.components.wicket.box.GWFrame;
import com.swordlord.gozer.eventhandler.generic.GozerDisplayMode;
import com.swordlord.gozer.frame.IGozerFrameExtension;

@SuppressWarnings("serial")
public class GWContext implements Serializable
{
	protected static final Log LOG = LogFactory.getLog(WicketGozerPanel.class);

	protected IGozerFrameExtension _gfe;
	protected GWFrame formPanel;
	private GozerModalWindow _modalWindow;

	public GWContext(GWFrame formPanel, IGozerFrameExtension gfe, GozerDisplayMode displayMode)
	{
		this._gfe = gfe;
		this.formPanel = formPanel;

		setDisplayMode(displayMode);
	}

	public GWContext(GWFrame formPanel, IGozerFrameExtension gfe)
	{
		this._gfe = gfe;
		this.formPanel = formPanel;
	}

	public GWFrame getFormPanel()
	{
		return formPanel;
	}

	public IGozerFrameExtension getFrameExtension()
	{
		return _gfe;
	}

	public GozerModalWindow getModalWindow()
	{
		return _modalWindow;
	}

	public boolean hasModalWindow()
	{
		return _modalWindow != null;
	}

	public boolean isModal()
	{
		return getFrameExtension().getGozerController().isModal();
	}

	protected void setDisplayMode(GozerDisplayMode displayMode)
	{
		getFrameExtension().getGozerController().setDisplayMode(displayMode);
	}

	public void setFormPanel(GWFrame formPanel)
	{
		this.formPanel = formPanel;
	}
	public void setModalWindow(GozerModalWindow modalWindow)
	{
		this._modalWindow = modalWindow;
	}
}
