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
 ** $Id: ExcelRenderer.java 1356 2012-02-20 13:17:11Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.excel;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Application;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.csv.GCsvDetail;
import com.swordlord.gozer.components.csv.GCsvFrame;
import com.swordlord.gozer.components.csv.GCsvList;
import com.swordlord.gozer.components.csv.GCsvListAndDetail;
import com.swordlord.gozer.components.csv.GCsvObjectBase;
import com.swordlord.gozer.components.csv.GCsvReport;
import com.swordlord.gozer.components.csv.GCsvReportPanel;
import com.swordlord.gozer.components.csv.GCsvTabbedPanel;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.GListAndDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GFrame;
import com.swordlord.gozer.components.generic.box.GTabbedPanel;
import com.swordlord.gozer.components.generic.report.GReport;
import com.swordlord.gozer.components.generic.report.GReportPanel;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.generic.FrameRendererBase;
import com.swordlord.gozer.session.IGozerSessionInfo;


/**
 * @author LordEidi
 *         put into its own workbook.
 */
public class ExcelRenderer extends FrameRendererBase
{
    protected static final Log LOG = LogFactory.getLog(ExcelRenderer.class);

	private Application _app;
	private IGozerSessionInfo _session;
	
    /**
     * @param ot
     * @param gfe
     * @param app
     * @param session
     */
	public ExcelRenderer(ObjectTree ot, IGozerFrameExtension gfe, Application app, IGozerSessionInfo session)
	{
		super(ot, gfe);
		
		_app = app;
		_session = session;
	}

	private GCsvList createGCsvList(ObjectBase ob)
	{
		GCsvList gfl = new GCsvList(getGozerFrameExtension(), (GList) ob);

		return gfl;
	}

	private GCsvListAndDetail createGCsvListAndDetail(ObjectBase ob)
	{
		GCsvListAndDetail gfl = new GCsvListAndDetail(getGozerFrameExtension());

		LinkedList<ObjectBase> children = ob.getChildren();

		for(ObjectBase obChild : children)
		{
			if (obChild.getClass().equals(GList.class))
			{
				GCsvList gflchild = new GCsvList(getGozerFrameExtension(), (GList) obChild);
				if(gflchild != null)
				{
					gfl.putChild(gflchild);
				}
			}
		}

		return gfl;
	}
	
	private GCsvReport createGCsvReport(ObjectBase ob)
	{
		return new GCsvReport(getGozerFrameExtension(), (GReport) ob, _app, _session);
	}
	
	private GCsvTabbedPanel createGCsvTabbedPanel(ObjectBase ob)
	{
		GCsvTabbedPanel gf = new GCsvTabbedPanel(getGozerFrameExtension(), (GTabbedPanel) ob);

		LinkedList<ObjectBase> children = ob.getChildren();

		for(ObjectBase obChild : children)
		{
			if (obChild.getClass().equals(GList.class))
			{
				GCsvList gflchild = createGCsvList(obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass().equals(GDetail.class))
			{
				GCsvDetail gflchild = new GCsvDetail(getGozerFrameExtension(), (GDetail) obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass().equals(GListAndDetail.class))
			{
				GCsvListAndDetail gflchild = createGCsvListAndDetail(obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
			else if (obChild.getClass().equals(GReport.class))
			{
				GCsvReport gflchild = createGCsvReport(obChild);
				if(gflchild != null)
				{
					gf.putChild(gflchild);
				}
			}
		}

		return gf;
	}

	private GCsvObjectBase renderObject(ObjectBase ob, GCsvObjectBase parent)
	{
		GCsvObjectBase pnl = null;

		if(ob.getClass().equals(GList.class))
		{
			pnl = createGCsvList(ob);
		}
		else if(ob.getClass().equals(GListAndDetail.class))
		{
			pnl = createGCsvListAndDetail(ob);
		}
		else if(ob.getClass().equals(GTabbedPanel.class))
		{
			pnl = createGCsvTabbedPanel(ob);
		}
		else if (ob.getClass().equals(GReport.class))
		{
			pnl = createGCsvReport(ob);
		}

		return pnl;
	}
	
	@Override
	public Workbook renderTree()
	{
		return renderTree(null);
	}
	
    /**
     * @param wb
     * @return
     */
	public Workbook renderTree(Workbook wb)
	{
        if (wb == null)
        {
            wb = new HSSFWorkbook();
            // Workbook wb = new XSSFWorkbook();
        }

        ObjectTree ot = getObjectTree();
        if (ot == null)
        {
            LOG.error("ObjectTree is null");
            return wb;
        }

        ObjectBase ob = ot.getRoot();
        if (ob == null)
        {
            LOG.error("Root is null");
            LOG.info(ot.toString());
            return wb;
        }
		
		// the first panel - should be a GFrame
		if(ob.getClass().equals(GFrame.class))
		{
			GCsvFrame frame = new GCsvFrame(getGozerFrameExtension());

			LinkedList<ObjectBase> children = ob.getChildren();
			for(ObjectBase obChild : children)
			{
				GCsvObjectBase child = renderObject(obChild, frame);
				if(child != null)
				{
					frame.putChild(child);
				}
			}

			frame.renderToWorkbook(wb);

			return wb;
		}
		// or a report panel
		else if(ob.getClass() == GReportPanel.class)
		{
			GCsvReportPanel frame = new GCsvReportPanel(getGozerFrameExtension());

			LinkedList<ObjectBase> children = ob.getChildren();
			for(ObjectBase obChild : children)
			{
				GCsvObjectBase child = renderObject(obChild, frame);
				if(child != null)
				{
					frame.putChild(child);
				}
			}

			frame.renderToWorkbook(wb);

			return wb;
		}

		return null;
	}
}
