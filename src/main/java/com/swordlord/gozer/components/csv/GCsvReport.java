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
** $Id: GCsvReport.java 1187 2011-11-01 09:38:15Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.csv;

import com.swordlord.common.prefs.UserPrefs;
import com.swordlord.gozer.ui.gozerframe.GWReportContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Application;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.components.generic.report.GReport;
import com.swordlord.gozer.eventhandler.generic.GozerDisplayMode;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.excel.ExcelRenderer;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.gozer.util.ResourceLoader;
import com.swordlord.repository.gozerframe.common.DefaultReportPanelExtension;


/**
 * 
 * @author LordEidi
 *
 */
@SuppressWarnings("serial")
public class GCsvReport extends GCsvObjectBase
{
	protected static final Log LOG = LogFactory.getLog(GCsvReport.class);
	
	private ObjectTree _ot;
	private GWReportContext _context;
	private Application _app;
	private IGozerSessionInfo _session;
	
	/**
	 * 
	 * @param gfe
	 * @param report
	 * @param app
	 * @param session
	 */
	public GCsvReport(IGozerFrameExtension gfe, GReport report, Application app, IGozerSessionInfo session)
	{
		super(gfe);
		
		try
		{
			_app = app;
			_session = session;

			// TODO fix this
			String strReportFile = UserPrefs.APP_REPORT_GOZER_FILES_FOLDER + report.getDefiniton();
			
			DefaultReportPanelExtension ext = new DefaultReportPanelExtension(session, strReportFile, gfe.getDataBindingContext());
			
			SAXBuilder sb = new SAXBuilder();
            Document document = sb.build(ResourceLoader.loadResource(_app, strReportFile, getClass()));
	
			Parser parser = new Parser(ext.getDataBindingContext());
			parser.createTree(document);
			
			_ot = parser.getTree();
			
			_context = new GWReportContext(null, ext, GozerDisplayMode.PDF, _ot);
		}
		catch(Exception e)
		{
			LOG.warn(e.getLocalizedMessage());
		}
		
	}

	@Override
	public void renderToWorkbook(Workbook wb)
	{
		ExcelRenderer renderer = new ExcelRenderer(_ot, _context.getFrameExtension(), _app, _session);
		
		// make sure, that no new workbook is created but this content is added to the existing workbook
		renderer.renderTree(wb);
	}
}
