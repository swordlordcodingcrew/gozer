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

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.eventhandler.generic.GozerDisplayMode;
import com.swordlord.gozer.frame.GozerReportExtension;
import com.swordlord.gozer.renderer.wicket.WicketReportRenderer;
import com.swordlord.gozer.util.ResourceLoader;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.sobf.common.jcr.JcrHelper;

@SuppressWarnings("serial")
public class WicketGozerReportPanel extends Panel
{
	private static final Log LOG = LogFactory.getLog(WicketGozerReportPanel.class);

	private GWReportContext _context;

	private boolean _bIsLandscape = false;

	// Default is un-modal.
	public WicketGozerReportPanel(String id, GozerReportExtension gfe)
	{
		super(id);
		this.setOutputMarkupId(true);

		generateContent(gfe, GozerDisplayMode.WEB);
	}

	protected ObjectTree getObjectTree(GozerReportExtension gfe)
	{
		try
		{
			String layoutFileName = gfe.getGozerLayoutFileName();
			
			//Search the gozer configuration file first in the JCR Repository then on the file system. 
			InputStream inputStream = JcrHelper.instance().getGozerLayout(layoutFileName);
			if (inputStream == null)
			{
				LOG.info("Generating report on Local Gozer layout file: " + layoutFileName);
                inputStream = ResourceLoader.loadResource(getApplication(), layoutFileName, getClass());
			}
			
			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(inputStream);

			Parser parser = new Parser(gfe.getDataBindingContext());
			parser.createTree(document);
			ObjectTree ot = parser.getTree();
			return ot;
		}
		catch(JDOMException eJDOM)
		{
			LOG.error(eJDOM);
		}
		catch (Exception e)
		{
			LOG.error(e);
		}
		return null;
	}
	
	public GWReportContext getContext()
	{
		return _context;
	}

	public DataContainer getDataContainer()
	{
		return _context.getFrameExtension().getDataContainer();
	}

	public boolean getIsLandscape()
	{
		return _bIsLandscape;
	}
	
	private void generateContent(GozerReportExtension gfe, GozerDisplayMode displayMode)
	{
		ObjectTree ot = getObjectTree(gfe);
		
		_context = new GWReportContext(null, gfe, displayMode, ot);
	
		WicketReportRenderer sr = new WicketReportRenderer(ot, _context);

		add(sr.renderTree("gozerform"));
	}
}
