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
 ** $Id: ReportController.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.eventhandler.generic;

import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.graph.GGraph;
import com.swordlord.gozer.components.generic.report.GReportPanel;
import com.swordlord.gozer.components.wicket.report.GWReport;
import com.swordlord.gozer.frame.GozerReportExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.repository.gozerframe.common.DefaultReportPanelExtension;
import com.swordlord.sobf.wicket.security.SecuredWebSession;
import com.swordlord.sobf.wicket.ui.gozerframe.WicketGozerReportPanel;

/**
 * TODO JavaDoc for ReportController.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class ReportController extends GozerController
{
    /**
	 * 
	 */
	protected GozerDisplayMode _displayMode = GozerDisplayMode.WEB;

    /**
	 * 
	 */
	protected WebPage _webPage;


	    /**
     * Constructor of GEventControl. Per GozerFrame one GEventControl is
     * intended.
     * 
     * @param re
     */
	public ReportController(GozerReportExtension re)
	{
	   super(re);
	}

	/**
	 * Call another Graph from a graph.
	 * 
	 * @param target  AjaxRequestTarget
	 * @param panel   Panel that contain the graph
	 * @param graph Graph that is actually displayed
	 * @param targetId Id parameter for the next graph.
	 * @param graphSubTitle Sub-Title for the next graph.
	 * 
	 */
	public void replaceDetailsAction(AjaxRequestTarget target, GWReport panel, GGraph graph, String targetId, String graphSubTitle) {
		
		if (_fe.canClickable()) {

			IGozerSessionInfo sessionInfo = (SecuredWebSession) panel.getSession();
			GReportPanel gReportPanel= (GReportPanel)graph.getObjectTree().getRoot();
			
			DefaultReportPanelExtension gre = new DefaultReportPanelExtension(sessionInfo, graph.getTargetChart(), gReportPanel.getObjectTree().getDataBindingContext(), targetId);
			WicketGozerReportPanel wicketGozerReportPanel = new WicketGozerReportPanel("cell", gre);
			
			//Search all childs to find GGraph and set a sub-Title
			Iterator<ObjectBase> iter = gre.getQueries().getObjectTree().getRoot().getChildren().iterator();
			while (iter.hasNext())
			{
				ObjectBase objectBase = iter.next();
				
				if (objectBase instanceof GGraph) {
					GGraph subGraph = (GGraph) objectBase;
					subGraph.putAttribute(GGraph.ATTRIBUTE_SUBTITLE, graphSubTitle);
				}
			}
			
			panel.replaceWith(wicketGozerReportPanel);
            target.add(wicketGozerReportPanel);
		}
	}


}
