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
 ** $Id: GWAjaxLazyLoadPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import com.swordlord.gozer.frame.GozerReportExtension;
import com.swordlord.sobf.wicket.ui.gozerframe.GWReportContext;

/**
 * A panel where you can lazy load another panel. Usually used for reports and
 * such.
 * 
 * Based on code by jcompagner
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWAjaxLazyLoadPanel extends Panel
{
    /**
	 * 
	 */
	public static final String LAZY_LOAD_COMPONENT_ID = "content";

    /**
     * state, 0:add loading component 1:loading component added, waiting for
     * ajax replace 2:ajax replacement completed
     */
	protected byte state = 0;
	
    /**
     * @param id
     * @param model
     */
	public GWAjaxLazyLoadPanel(final String id, final IModel<?> model)
	{
		super(id, model);

		setOutputMarkupId(true);

		add(new AbstractDefaultAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target)
			{
                if (state < 2)
                {
                    Component component = getLazyLoadComponent(LAZY_LOAD_COMPONENT_ID);
                    GWAjaxLazyLoadPanel.this.replace(component);
                    setState((byte) 2);
                }
                target.add(GWAjaxLazyLoadPanel.this);
			}

			@Override
            public void renderHead(final Component component, final IHeaderResponse response)
            {
                super.renderHead(component, response);
                if (state < 2)
                {
                    handleCallbackScript(response, getCallbackScript().toString());
                }
            }

            /*
             * @Override public boolean isEnabled(Component component) { return
             * state < 2; }
             */
		});
	}

	        /**
     * Gets the value of the state property
     * 
     * @return possible object is {@link byte}
     */
    public byte getState()
    {
        return state;
    }

    /**
     * Allows subclasses to change the callback script if needed.
     * 
     * @param response
     * @param callbackScript
     */
    protected void handleCallbackScript(final IHeaderResponse response, final String callbackScript)
    {
        // response.renderOnDomReadyJavaScript(callbackScript);
        response.render(OnDomReadyHeaderItem.forScript(callbackScript));
    }

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		if (state == 0)
		{
			add(getLoadingComponent(LAZY_LOAD_COMPONENT_ID));
			setState((byte)1);
		}
		super.onBeforeRender();
	}

	/**
	 * 
	 * @param state
	 */
    public void setState(final byte state)
    {
        this.state = state;
        getPage().dirty();
    }
	
	/**
	 * @param markupId
	 *            The components markupid.
	 * @return The component to show while the real component is being created.
	 */
	public Component getLoadingComponent(final String markupId)
	{
        IRequestHandler handler = new ResourceReferenceRequestHandler(AbstractDefaultAjaxBehavior.INDICATOR);

        return new Label(markupId, "<img alt=\"Loading...\" src=\"" + RequestCycle.get().urlFor(handler) + "\"/>").setEscapeModelStrings(false);
	}

	protected GozerReportExtension getReportExtension()
	{
		return (GozerReportExtension) getGWReportContext().getFrameExtension();
	}

	public GWReportContext getGWReportContext()
	{
		return (GWReportContext) getModelObject();
	}

	protected Object getModelObject()
	{
		return getDefaultModelObject();
	}

	protected IModel<?> getModel()
	{
		return getDefaultModel();
	}

	protected void setModel(IModel<?> model)
	{
		setDefaultModel(model);
	}

	/**
	 * 
	 * @param markupId
	 *            The components markupid.
	 * @return The component that must be lazy created. You may call setRenderBodyOnly(true) on this
	 *         component if you need the body only.
	 */
	public Component getLazyLoadComponent(String arg0)
	{
		return null;
	}

}
