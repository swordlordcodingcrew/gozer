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

import java.lang.reflect.Constructor;
import java.util.UUID;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import com.swordlord.gozer.frame.GozerReportExtension;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.sobf.wicket.ui.page.ContentPage;

public class GozerReportPage extends ContentPage
{
	public static GozerReportPage getFrame(GozerReportExtension gfe)
	{
		GozerReportPage page = null;

		try
		{
			page = new GozerReportPage(gfe);
		} 
		catch (Exception e)
		{
			LOG.error(e);
		}

		return page;
	}

	public static GozerReportPage getFrame(GozerReportExtension gfe, Class<? extends GozerReportPage> clazz)
	{
		GozerReportPage page = null;

		try
		{
			Constructor<? extends GozerReportPage> c = clazz.getConstructor(new Class[] { GozerReportExtension.class });
			page = c.newInstance(new Object[] { gfe });
		} 
		catch (Exception e)
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		}

		return page;
	}

	protected UUID _uuidHelpContent;

	protected UUID _uuidFrameId;

	protected WicketGozerReportPanel _gp;

	protected GozerReportPage(GozerReportExtension gfe)
	{
		super();

		if (gfe != null)
		{
			_uuidHelpContent = gfe.getHelpId();
			_uuidFrameId = gfe.getFrameId();

			_gp = new WicketGozerReportPanel("gozerpanel", gfe);

			_content.add(_gp);

			if (_gp.getIsLandscape())
			{
				changePageOrientationToLandscape();
			}
		} 
		else
		{
			_content.add(new EmptyPanel("gozerpanel"));
		}
	}

	@Override
	protected boolean isPageLayoutLandscape()
	{
		return _gp == null ? false : _gp.getIsLandscape();
	}

	@Override
	protected boolean isSidebarPanelVisible()
	{
		return _gp == null ? true : !_gp.getIsLandscape();
	}

	@Override
	protected void onBeforeRender()
	{
		if (_gp != null)
		{
			DataContainer dc = _gp.getDataContainer();
			if ((dc != null) && dc.hasErrors())
			{
				for (String error : dc.getErrors())
				{
					this.error(error);
				}
			}
		}

		super.onBeforeRender();
	}
}
