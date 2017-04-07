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
** $Id: FopRenderer.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.fop;

import java.util.LinkedList;

import org.apache.wicket.Application;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.fop.GFopDetail;
import com.swordlord.gozer.components.fop.GFopFrame;
import com.swordlord.gozer.components.fop.GFopList;
import com.swordlord.gozer.components.fop.GFopListAndDetail;
import com.swordlord.gozer.components.fop.GFopObjectBase;
import com.swordlord.gozer.components.fop.GFopReport;
import com.swordlord.gozer.components.fop.GFopReportPanel;
import com.swordlord.gozer.components.fop.GFopTabbedPanel;
import com.swordlord.gozer.components.fop.graph.GFopAreaChart;
import com.swordlord.gozer.components.fop.graph.GFopPieChart;
import com.swordlord.gozer.components.fop.graph.GFopStackedBarChart;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.GListAndDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GFrame;
import com.swordlord.gozer.components.generic.box.GTabbedPanel;
import com.swordlord.gozer.components.generic.graph.GAreaChart;
import com.swordlord.gozer.components.generic.graph.GPieChart;
import com.swordlord.gozer.components.generic.graph.GStackedBarChart;
import com.swordlord.gozer.components.generic.report.GReport;
import com.swordlord.gozer.components.generic.report.GReportPanel;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.generic.FrameRendererBase;
import com.swordlord.gozer.session.IGozerSessionInfo;

/**
 * @author LordEidi
 *
 */
public class FopRenderer extends FrameRendererBase
{
	private Application _app;
	private IGozerSessionInfo _session;
	
	public FopRenderer(ObjectTree ot, IGozerFrameExtension gfe, Application app, IGozerSessionInfo session)
	{
		super(ot, gfe);
		
		_app = app;
		_session = session;
	}

	private GFopDetail createGFopDetail(ObjectBase ob)
	{
		GFopDetail gf = new GFopDetail(getGozerFrameExtension(), (GDetail) ob);

		return gf;
	}

	private GFopList createGFopList(ObjectBase ob)
	{
		GFopList gfl = new GFopList(getGozerFrameExtension(), (GList) ob);

		return gfl;
	}
	
	private GFopPieChart createGFopPieChart(ObjectBase ob)
	{
		return new GFopPieChart(getGozerFrameExtension(), (GPieChart) ob);
	}
	
	private GFopAreaChart createGFopAreaChart(ObjectBase ob)
	{
		return new GFopAreaChart(getGozerFrameExtension(), (GAreaChart) ob);
	}
	
	private GFopStackedBarChart createGFopStackedBarChart(ObjectBase ob)
	{
		return new GFopStackedBarChart(getGozerFrameExtension(), (GStackedBarChart) ob);
	}
	
	private GFopReport createGFopReport(ObjectBase ob)
	{
		return new GFopReport(getGozerFrameExtension(), (GReport) ob, _app, _session);
	}

	private GFopListAndDetail createGFopListAndDetail(ObjectBase ob)
	{
		GFopListAndDetail gfl = new GFopListAndDetail(getGozerFrameExtension());

		LinkedList<ObjectBase> children = ob.getChildren();

		for(ObjectBase obChild : children)
		{
			if (obChild.getClass() == GList.class)
			{
				GFopList gflchild = new GFopList(getGozerFrameExtension(), (GList) obChild);
				if(gflchild != null)
				{
					gfl.putChild(gflchild);
				}
			}
		}

		return gfl;
	}

	private GFopTabbedPanel createGFopTabbedPanel(ObjectBase ob)
	{
		GFopTabbedPanel gf = new GFopTabbedPanel(getGozerFrameExtension(), (GTabbedPanel) ob);

		LinkedList<ObjectBase> children = ob.getChildren();

		for(ObjectBase obChild : children)
		{
			if (obChild.getClass() == GList.class)
			{
				GFopList gflchild = new GFopList(getGozerFrameExtension(), (GList) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass() == GDetail.class)
			{
				GFopDetail gflchild = new GFopDetail(getGozerFrameExtension(), (GDetail) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass() == GListAndDetail.class)
			{
				GFopListAndDetail gflchild = createGFopListAndDetail((GListAndDetail) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass() == GPieChart.class)
			{
				GFopPieChart gflchild = createGFopPieChart((GPieChart) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass() == GAreaChart.class)
			{
				GFopAreaChart gflchild = createGFopAreaChart((GAreaChart) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass() == GStackedBarChart.class)
			{
				GFopStackedBarChart gflchild = createGFopStackedBarChart((GStackedBarChart) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass() == GReport.class)
			{
				GFopReport gflchild = createGFopReport((GReport) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
		}

		return gf;
	}

	private GFopObjectBase renderObject(ObjectBase ob, GFopObjectBase parent)
	{
		GFopObjectBase pnl = null;

		if(ob.getClass() == GList.class)
		{
			pnl = createGFopList(ob);
		}
		else if(ob.getClass() == GListAndDetail.class)
		{
			pnl = createGFopListAndDetail(ob);
		}
		else if (ob.getClass() == GDetail.class)
		{
			pnl = createGFopDetail(ob);
		}
		else if (ob.getClass() == GTabbedPanel.class)
		{
			pnl = createGFopTabbedPanel(ob);
		}
		else if (ob.getClass() == GPieChart.class)
		{
			pnl = createGFopPieChart(ob);
		}
		else if (ob.getClass() == GAreaChart.class)
		{
			pnl = createGFopAreaChart(ob);
		}
		else if (ob.getClass() == GStackedBarChart.class)
		{
			pnl = createGFopStackedBarChart(ob);
		}
		else if (ob.getClass() == GReport.class)
		{
			pnl = createGFopReport(ob);
		}

		return pnl;
	}

	@Override
	public String renderTree()
	{
		ObjectBase ob = getObjectTree().getRoot();

		// the first panel is either a GFrame
		if(ob.getClass() == GFrame.class)
		{
			GFopFrame frame = new GFopFrame(getGozerFrameExtension());

			LinkedList<ObjectBase> children = ob.getChildren();
			for(ObjectBase obChild : children)
			{
				GFopObjectBase child = renderObject(obChild, frame);
				if(child != null)
				{
					frame.putChild(child);
				}
			}

			return frame.toString();
		}
		// or a report panel
		else if(ob.getClass() == GReportPanel.class)
		{
			GFopReportPanel frame = new GFopReportPanel(getGozerFrameExtension());

			LinkedList<ObjectBase> children = ob.getChildren();
			for(ObjectBase obChild : children)
			{
				GFopObjectBase child = renderObject(obChild, frame);
				if(child != null)
				{
					frame.putChild(child);
				}
			}

			return frame.toString();
		}

		return null;
	}
}
