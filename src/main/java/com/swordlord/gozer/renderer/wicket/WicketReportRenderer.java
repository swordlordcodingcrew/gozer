/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
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
 ** $Id: WicketReportRenderer.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.wicket;

import com.swordlord.gozer.ui.gozerframe.GWReportContext;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.report.GReportPanel;
import com.swordlord.gozer.components.wicket.report.GWReportPanel;
import com.swordlord.gozer.frame.GozerReportExtension;
import com.swordlord.gozer.renderer.generic.ReportRendererBase;

/**
 * TODO JavaDoc for WicketReportRenderer.java
 * 
 * @author LordEidi
 * 
 */
public class WicketReportRenderer extends ReportRendererBase
{
	private GWReportContext _context;

    /**
     * @param ot
     * @param context
     */
	public WicketReportRenderer(ObjectTree ot, GWReportContext context)
	{
		super(ot, (GozerReportExtension)context.getFrameExtension());

		_context = context;
	}

	@Override
	public Panel renderTree()
	{
		return renderTree("gozerform");
	}

    /**
     * @param id
     * @return
     */
	public Panel renderTree(String id)
	{
		ObjectBase ob = getObjectTree().getRoot();

		if (ob.getClass().equals(GReportPanel.class))
		{
			return new GWReportPanel(id, new Model<GWReportContext>(_context), (GReportPanel) ob);
		}

        return new EmptyPanel(id);
	}
}
