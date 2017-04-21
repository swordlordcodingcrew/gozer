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
 ** $Id: GWCsvButton.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button.generic;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.GCsvAction;
import com.swordlord.gozer.components.wicket.action.button.GWAbstractAction;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.frame.wicket.BinaryStreamWriter;
import com.swordlord.gozer.renderer.excel.ExcelRenderer;
import com.swordlord.gozer.session.IGozerSessionInfo;


/**
 * Renders a CSV button, UIs are then rendered into a dynamic Excel files and
 * presented to the user.
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWCsvButton extends GWAbstractAction
{
	protected static final Log LOG = LogFactory.getLog(GWCsvButton.class);
	
    /**
     * @param id
     * @param gc
     * @param actionBase
     */
	public GWCsvButton(String id, GozerController gc, GActionBase actionBase)
	{
        super(id, gc, actionBase, new PackageResourceReference("img"));
        add(new AttributeModifier("value", new Model<String>("Export")));
        add(new AttributeModifier("alt", new ResourceModel("action.csv")));
        add(new AttributeModifier("title", new ResourceModel("action.csv")));
	}

    private ResourceStreamRequestHandler createTarget(Workbook wb)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
        try
        {
            wb.write(out);
        }
        catch (Exception e)
        {
        	LOG.error(e);
        	return null;
        }
        
        ResourceStreamRequestHandler ret = new ResourceStreamRequestHandler(new BinaryStreamWriter(out, "application/vnd.ms-excel"));

    	try
    	{
    		out.close();
    	}
        catch (Exception e)
        {
        	LOG.error(e);
        }
        
        return ret;
    }

	@Override
    public String getActionID()
	{
		return GCsvAction.getObjectTag();
	}

		// remain on this page, hence do not set response page
	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_DOCTYPE_XLS);

		if(_gc.getGozerFrameExtension().canCSV())
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
		// TODO
		IGozerFrameExtension gfe = getGozerController().getGozerFrameExtension();
		ExcelRenderer renderer = new ExcelRenderer(getObjectTree(gfe), gfe, getApplication(), (IGozerSessionInfo) getSession());
		Workbook wb = renderer.renderTree();

		if(wb == null)
		{
			LOG.warn("Rendered ObjectTree is empty. Cancelling Excel rendering.");
			return;
		}

        ResourceStreamRequestHandler target = createTarget(wb);
		if(target == null)
		{
			// TODO: warn the user!!!
			return;
		}
		else
		{
			target.setFileName(gfe.getCaption().toLowerCase() + ".xls");
            RequestCycle.get().scheduleRequestHandlerAfterCurrent(target);
		}
	}

	@Override
    public void setActionID()
	{
		// not possible in predefined GActions
	}
}
