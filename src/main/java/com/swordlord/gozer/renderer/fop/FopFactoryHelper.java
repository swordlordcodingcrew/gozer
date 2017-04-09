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
** $Id: FopFactoryHelper.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.fop;

import java.io.OutputStream;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import com.swordlord.common.prefs.UserPrefsFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


/**
 * @author LordEidi
 *
 */
public class FopFactoryHelper
{
	protected static final Log LOG = LogFactory.getLog(FopFactoryHelper.class);

	private static FopFactory _instance;
	
	public static FopFactory instance()
	{
		if (_instance == null) 
		{
			_instance = FopFactory.newInstance();	
            // TODO: load a config for the fopFactory
			//DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
			//Configuration cfg = cfgBuilder.buildFromFile(new File("C:/Temp/mycfg.xml"));
			//fopFactory.setUserConfig(cfg);
		}
		return _instance;
	}
	
	public FOUserAgent newFOUserAgent()
	{
		FOUserAgent foUserAgent = FopFactoryHelper.instance().newFOUserAgent();
		
	    // configure foUserAgent as desired
	    foUserAgent.setCreator("Gozer.FOP");
	    
	    return foUserAgent;
	}
	
	public Fop newFop(FOUserAgent foUserAgent, OutputStream stream)
	{
		Fop fop = null;
		
	    // Construct fop with desired output format
		try
		{
			fop = FopFactoryHelper.instance().newFop(MimeConstants.MIME_PDF, foUserAgent, stream);		
		}
		catch(FOPException e)
		{
			LOG.error(MessageFormat.format("newFOP threw an exception: {0}", e.getMessage()));
		}
		
		return fop;
	}
	
	public String getCharset()
	{
		return UserPrefsFactory.getUserPrefs().getProperty("charset", "UTF-8", FopFactoryHelper.class);
	}
	
	public void transform(String strSource, Result result) throws TransformerConfigurationException, TransformerException
	{
		StringReader sr = new StringReader(strSource);
		// Setup input stream
        Source src = new StreamSource(sr);
        
        try 
        {
	        // Setup JAXP using identity transformer
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Transformer transformer = factory.newTransformer();
			
	        transformer.setErrorListener(new FopTransformerErrorListener());
        
	        String strEncoding = getCharset();
			transformer.setOutputProperty(OutputKeys.ENCODING, strEncoding);
        
			//System.setProperty("java.awt.headless", "true");
			//LOG.info("Headless mode before FOPing: " + GraphicsEnvironment.isHeadless());
		
			// Start XSLT transformation and FOP processing
			transformer.transform(src, result);
		} 
        catch (TransformerConfigurationException e) 
		{
            LOG.error(MessageFormat.format("FOP transformation finalisation crashed: {0}", e.getLocalizedMessage()));
            throw(e);
		}
        catch (TransformerException e) 
        {
            LOG.error(MessageFormat.format("FOP transformation finalisation crashed: {0}", e.getLocalizedMessage()));
            throw(e);
		} 
	}
}
