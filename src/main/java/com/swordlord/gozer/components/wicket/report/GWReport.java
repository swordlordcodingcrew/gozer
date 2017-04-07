/*-----------------------------------------------------------------------------
 **
 ** - Open Risk and Compliance Tool -
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 ** and individual contributors
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
 ** $Id: GWReport.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.report;

import com.swordlord.gozer.ui.gozerframe.WicketGozerReportPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.report.GReport;
import com.swordlord.gozer.components.wicket.GWAjaxLazyLoadPanel;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.repository.gozerframe.common.DefaultReportPanelExtension;
import com.swordlord.sobf.wicket.security.SecuredWebSession;

/**
 * TODO JavaDoc for GWReport.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWReport extends GWAjaxLazyLoadPanel
{
    private GReport _gReport;
    private DataBindingContext _parentContext;
    	
	@Override
	public Component getLazyLoadComponent(String id)
	{		
		IGozerSessionInfo sessionInfo = (SecuredWebSession) getSession();
				 
		DefaultReportPanelExtension gre = new DefaultReportPanelExtension(sessionInfo, _gReport.getAttribute(GReport.ATTRIBUTE_DEFINITION), _parentContext);
		WicketGozerReportPanel wicketGozerReportPanel = new WicketGozerReportPanel(id, gre);
		
		return wicketGozerReportPanel;
	}
	
    /**
     * @param id
     * @param model
     * @param gfReport
     * @param parentContext
     */
	public GWReport(String id, IModel<?> model, GReport gfReport, DataBindingContext parentContext)
	{
		super(id, model);
		this._gReport = gfReport;
		this._parentContext = parentContext;
	}
	
	@Override
	protected void onBeforeRender()
	{		
		if(state == 2)
		{
			// make sure that after first successful render, we change to three
			// at next render request, we re-render the whole thingy.
			setState((byte)3);
		}
		else if (state > 2)
		{
			reload();
		}
		super.onBeforeRender();
	}
	
	/**
	 * Reloads the graph.
	 */
	private void reload()
	{
		// replace panel contents with loading icon
        Component loadingComponent = getLoadingComponent(LAZY_LOAD_COMPONENT_ID);

        this.replace(loadingComponent);

        // add ajax behaviour to install call back
        loadingComponent.add(new ReportAjaxBehavior(this));
        
        setState((byte)1);
	}
	
	/**
     * Causes the {@link AjaxLazyLoadPanel} to re-display the loading indicator,
     * then in a seperate ajax request, get it's contents.
     *
     * @param target
     */
    public void restart(AjaxRequestTarget target) 
    {
        target.add(this);

        reload();
    } 
}
