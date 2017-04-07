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
 ** $Id: GWFopButton.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.generic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.common.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GFopAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.frame.wicket.BinaryStreamWriter;
import com.swordlord.gozer.renderer.fop.FopFactoryHelper;
import com.swordlord.gozer.renderer.fop.FopRenderer;
import com.swordlord.gozer.renderer.fop.FopTemplateManager;
import com.swordlord.gozer.session.IGozerSessionInfo;

/**
 * Renders a PDF button, UIs are then rendered into a dynamic PDF file and
 * presented to the user.
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWFopButton extends GWAbstractAction
{
    /**
     * @param id
     * @param gc
     * @param actionBase
     */
	public GWFopButton(String id, GozerController gc, GActionBase actionBase)
	{
        super(id, gc, actionBase, new PackageResourceReference("img"));
        add(new AttributeModifier("value", new Model<String>("Report")));
        add(new AttributeModifier("alt", new ResourceModel("action.fop")));
        add(new AttributeModifier("title", new ResourceModel("action.fop")));
	}

    private ResourceStreamRequestHandler convertFO2PDF(String strFO, String strAuthor, String strTitle)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		boolean bProcessingFailed = false;
		
		FopFactoryHelper foHelper = new FopFactoryHelper();
    	
    	FOUserAgent foUserAgent = foHelper.newFOUserAgent();
        foUserAgent.setAuthor(strAuthor);
        foUserAgent.setTitle(strTitle);
        foUserAgent.setCreationDate(new Date());
                    
        // Construct fop with desired output format
        Fop fop = foHelper.newFop(foUserAgent, out);
        
        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result res = null;
		try 
		{
			res = new SAXResult(fop.getDefaultHandler());
		} 
		catch (FOPException e) 
		{
			LOG.error(MessageFormat.format("Accessing DefaultHandler crashed: {0}", e.getLocalizedMessage()));
		}
        
		try
		{
			foHelper.transform(strFO, res);
		}
		catch (TransformerConfigurationException e) 
		{
			bProcessingFailed = true;
		}
        catch (TransformerException e) 
        {
			bProcessingFailed = true;
		} 
        
        ResourceStreamRequestHandler ret = null;
        
        if(!bProcessingFailed)
        {
            ret = new ResourceStreamRequestHandler(new BinaryStreamWriter(out, MimeConstants.MIME_PDF));
        }        

        try
        {
        	out.close();
	    }
	    catch (IOException e) 
	    {
	    	LOG.error(MessageFormat.format("Closing stream crashed: {0}", e.getLocalizedMessage()));
		}
        
        return ret;
    }

	@Override
    public String getActionID()
	{
		return GFopAction.getObjectTag();
	}

	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_DOCTYPE_PDF);

        if (_gc.getGozerFrameExtension().canPDF())
		{
            setImageResourceReference(new PackageResourceReference("img"), pp);
	
			setEnabled(true);
		}
		else
		{
            pp.add("inactive", 1); // add the inactivity filter
            setImageResourceReference(new PackageResourceReference("img"), pp);
			
			setEnabled(false);
		}
	}
	
	@Override
	public void onSubmit()
	{		
		IGozerSessionInfo session = getGozerSession();
		
		FopTemplateManager ftm = FopTemplateManager.instance();
		
		// make sure they are reloaded every time we use the template manager
		// TODO: probably undo this and have a special singleton reset mechanism
		ftm.initialiseTemplates();
		
		// TODO
		IGozerFrameExtension gfe = getGozerController().getGozerFrameExtension();
        FopRenderer renderer = new FopRenderer(getObjectTree(gfe), gfe, getApplication(), session);
		
		String fop = renderer.renderTree();
		
        ResourceStreamRequestHandler target = convertFO2PDF(fop, session.getCurrentUser().toString(), gfe.getCaption());
		
		if(target == null)
		{
			// TODO: warn the user!!!
			return;
		}
		else
		{
			target.setFileName(gfe.getCaption().toLowerCase() + ".pdf");
			
			//rc.getResponse().setCharacterEncoding("ISO-8859-1");
			//LOG.info(MessageFormat.format("Current character encoding for response: {0}", rc.getResponse().getCharacterEncoding()));
	
            RequestCycle.get().scheduleRequestHandlerAfterCurrent(target);
		}
	}

	@Override
    public void setActionID()
	{
		// not possible in predefined GActions
	}
}
