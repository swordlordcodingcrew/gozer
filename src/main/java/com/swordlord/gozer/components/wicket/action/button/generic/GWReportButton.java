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
 ** $Id: GWFopButton.java 1111 2011-01-14 16:18:21Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.generic;

import com.swordlord.gozer.session.SecuredWebSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GReportAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.datarow.DataRowBase;
/**
 * Button action to display Reports on a new window
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWReportButton extends GWAbstractAction
{
    private String reportSource = null;
    private String reportTitle = null;

    /**
     * Constructor
     * 
     * @param id
     *            See Component
     * @param gc
     *            Controller
     * @param reportAction
     *            action
     */
    public GWReportButton(String id, GozerController gc, GReportAction reportAction)
    {
        super(id, gc, reportAction, new PackageResourceReference("img"));
        add(new AttributeModifier("value", new Model<String>("Report")));
        add(new AttributeModifier("alt", new ResourceModel("action.report")));
        add(new AttributeModifier("title", new ResourceModel("action.report")));
        
        String winWidth = reportAction.getWindowWidth();
        if (winWidth == null || winWidth.isEmpty())
            winWidth = "1200";
        String winHeight = reportAction.getWindowHeight();
        if (winHeight == null || winHeight.isEmpty())
            winHeight = "560";

        add(new AttributeModifier("onclick", new Model<String>(
                "window.open('', 'popup', 'status=no,toolbar=no,location=no,menubar=no,resizable=yes,directories=no,scrollbars=yes,width=" + winWidth
                        + ",height=" + winHeight + "');this.form.target='popup'; return true;")));
        add(new AttributeModifier("onsubmit", new Model<String>("document.applForm.submit(); return false")));
                
        reportSource = reportAction.getSource();
        reportTitle = reportAction.getTitle();

    }

    @Override
    public String getActionID()
    {
        return GReportAction.getObjectTag();
    }

    @Override
    public void onBeforeRender()
    {
        super.onBeforeRender();

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_REPORT_GO);

        setImageResourceReference(new PackageResourceReference("img"), pp);
        setEnabled(true);
    }

    @Override
    public void onSubmit()
    {      
        DataRowBase drb = getDataBinding().getCurrentRow();
               
        IGozerSessionInfo sessionInfo = (SecuredWebSession) getSession();
        IGozerFrameExtension gfe = null;        

        try
        {           
            gfe = new GenericReportFrameExtension(sessionInfo, reportTitle, reportSource, drb.getKey().toUUID());
        }
        catch (IllegalArgumentException e)
        {
            LOG.error(e.getCause());
        }
        catch (SecurityException e)
        {
            LOG.error(e.getCause());
        }
      
        SimplePage page = SimplePage.getFrame(gfe);

        if (page != null)
        {
            setResponsePage(page);
        }
        else
        {
            LOG.error("GozerFile does not exist, is null or empty: " + gfe.getGozerLayoutFileName());
        }
    }

    @Override
    public void setActionID()
    {
        // not possible in predefined GActions
    }
    
    
}
