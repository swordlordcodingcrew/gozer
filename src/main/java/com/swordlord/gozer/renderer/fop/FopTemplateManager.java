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
** $Id: FopTemplateManager.java 1250 2011-11-29 12:38:59Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.fop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.swordlord.gozer.file.GozerFileLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.swordlord.gozer.components.fop.GFopFrame;
import com.swordlord.gozer.components.fop.GFopList;
import com.swordlord.gozer.components.fop.GFopReportPanel;
import org.apache.wicket.Application;


/**
 * @author LordEidi
 *
 */
public class FopTemplateManager
{
	protected static final Log LOG = LogFactory.getLog(FopTemplateManager.class);

	private static final String PATH_PREFIX = "orico/configuration/fop/templates/";
	private static final String FILE_POSTFIX = ".ft";

	private static FopTemplateManager _instance;
	
	private Hashtable<String, String> _htTemplates;
	
	public static FopTemplateManager instance()
	{
		if (_instance == null) 
		{
			_instance = new FopTemplateManager();
		}
		return _instance;
	}
	
	private FopTemplateManager()
	{
		initialiseTemplates();
	}
	
	public void initialiseTemplates()
	{
		_htTemplates = new Hashtable<String, String>();
		
		loadTemplate(GFopFrame.TEMPLATE_NAME, PATH_PREFIX + GFopFrame.TEMPLATE_NAME + FILE_POSTFIX);
		loadTemplate(GFopReportPanel.TEMPLATE_NAME, PATH_PREFIX + GFopReportPanel.TEMPLATE_NAME + FILE_POSTFIX);
		
		// not used yet
		// loadTemplate(jcrH, GFopDetail.TEMPLATE_NAME, PATH_PREFIX + GFopDetail.TEMPLATE_NAME + FILE_POSTFIX);
		
		loadTemplate(GFopList.TEMPLATE_TABLE_NAME, PATH_PREFIX + GFopList.TEMPLATE_TABLE_NAME + FILE_POSTFIX);
		loadTemplate(GFopList.TEMPLATE_COLUMN_NAME, PATH_PREFIX + GFopList.TEMPLATE_COLUMN_NAME + FILE_POSTFIX);
		loadTemplate(GFopList.TEMPLATE_COLUMN_CELL_NAME, PATH_PREFIX + GFopList.TEMPLATE_COLUMN_CELL_NAME + FILE_POSTFIX);
		loadTemplate(GFopList.TEMPLATE_ROW_NAME, PATH_PREFIX + GFopList.TEMPLATE_ROW_NAME + FILE_POSTFIX);
		loadTemplate(GFopList.TEMPLATE_ROW_CELL_NAME, PATH_PREFIX + GFopList.TEMPLATE_ROW_CELL_NAME + FILE_POSTFIX);
	}
	
	private void loadTemplate(String strKey, String strFilePath)
	{
		InputStream isFrame = GozerFileLoader.getGozerLayout(Application.get(), strFilePath);
		if(isFrame != null)
		{
			String strFileContent = GozerFileLoader.convertInputStreamToString(isFrame);
			
			try 
			{
				isFrame.close();
			} 
			catch (IOException e) 
			{
				LOG.error("TransMeta read, inputStream close threw an exception: " + e.getMessage());
				e.printStackTrace();
			}
			
			
			if(strFileContent != null)
			{
				putTemplate(strKey, strFileContent);
			}
		}
		else
		{
			LOG.error("FOP template is empty: " + strFilePath);
		}
	}
	
	public void putTemplate(String strTemplateName, String strTemplate)
	{
		_htTemplates.put(strTemplateName, strTemplate);
	}
	
	public String getTemplate(String strTemplateName)
	{
        final String template = _htTemplates.get(strTemplateName);
        if (template == null)
        {
            LOG.error("Template is null: " + strTemplateName);
            throw new IllegalStateException("Template " + strTemplateName + " not found! Maybe the JCR repository is not initialized?");
        }
        return template;
	}
}
