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
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.components.generic.box.GFrame;
import com.swordlord.gozer.eventhandler.generic.GozerDisplayMode;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.wicket.WicketRenderer;
import com.swordlord.gozer.util.ResourceLoader;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.sobf.common.jcr.JcrHelper;

@SuppressWarnings("serial")
public class WicketGozerPanel extends Panel
{
	private static final Log LOG = LogFactory.getLog(WicketGozerPanel.class);

	private GWContext _context;
	private String _strCaption;

	// TODO remove ugly hack
	private boolean _bIsLandscape = false;

	// Default is un-modal.
	public WicketGozerPanel(String id, IGozerFrameExtension gfe)
	{
		super(id);

		generateContent(gfe, GozerDisplayMode.WEB);
	}

	public WicketGozerPanel(String id, IGozerFrameExtension gfe, GozerDisplayMode displayMode)
	{
		super(id);

		generateContent(gfe, displayMode);
	}

	protected ObjectTree getObjectTree(IGozerFrameExtension gfe)
	{
		try
		{
			String layoutFileName = gfe.getGozerLayoutFileName();
			
			//Search the gozer configuration file first in the JCR Repository then on the file system. 
			InputStream inputStream = JcrHelper.instance().getGozerLayout(layoutFileName);
			if (inputStream == null)
			{			    
                inputStream = ResourceLoader.loadResource(getApplication(), layoutFileName, getClass());
			}
			
			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(inputStream);

			Parser parser = new Parser(gfe.getDataBindingContext());
			parser.createTree(document);
			ObjectTree ot = parser.getTree();
			
			return ot;
		}
		catch(JDOMException e)
		{
			String error = MessageFormat.format("Error when rendering ObjectTree with GozerFile: {0}, Error: {1}, Cause: {2}", gfe.getGozerLayoutFileName(), e, e.getCause());
			LOG.error(error);
		}
		catch (NullPointerException e)
		{
			String error = MessageFormat.format("Error when rendering ObjectTree with GozerFile: {0}, Error: {1}, Cause: {2}", gfe.getGozerLayoutFileName(), e, e.getCause());
			LOG.error(error);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			String error = MessageFormat.format("Error when rendering ObjectTree with GozerFile: {0}, Error: {1}, Cause: {2}", gfe.getGozerLayoutFileName(), e, e.getCause());
			LOG.error(error);
		}
		
		return null;
	}

	public String getCaption()
	{
		return _strCaption;
	}

	public GWContext getContext()
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
	
	private void generateContent(IGozerFrameExtension gfe, GozerDisplayMode displayMode)
	{
		_context = new GWContext(null, gfe, displayMode);
		_strCaption = gfe.getCaption();
		
		// Not visible when modal
		Label label = new Label("gozer_title", getCaption());
		label.setVisible(displayMode == GozerDisplayMode.WEB);
		add(label);
		
		ObjectTree ot = getObjectTree(gfe);
		if(ot == null)
		{
			add(new WebMarkupContainer("gozerform"));
			
			_bIsLandscape = false;
		}
		else
		{
			
			WicketRenderer sr = new WicketRenderer(ot, _context);	
			add(sr.renderTree("gozerform"));
	
			GFrame gozerForm = (GFrame)ot.getRoot();
			_bIsLandscape = gozerForm.getOrientation().compareToIgnoreCase("h") == 0;
		}
	}
}
