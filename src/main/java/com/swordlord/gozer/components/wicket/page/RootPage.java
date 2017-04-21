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
package com.swordlord.gozer.components.wicket.page;

import java.util.List;

import com.swordlord.gozer.session.IGozerSessionInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The root Page
 * 
 * @author ...
 * 
 */
public abstract class RootPage extends WebPage
{
    /**
     * Logging
     */
    protected static final Log LOG = LogFactory.getLog(RootPage.class);

    /**
     * Remain the last page called
     */
    protected Page _lastPage;

    /**
     * Constructor.
     * 
     * @param params
     */
    public RootPage(PageParameters params)
    {
        super(params);
        initialize();
    }

    /**
     * Constructor.
     */
    public RootPage()
    {
        super();
        initialize();
    }

    private void initialize()
    {
        add(new Label("pageTitle", new ResourceModel("title.slogan")));

        add(new BookmarkablePageLink<RootPage>("homeLink", RootPage.class));

    }

    public IGozerSessionInfo getGozerSession()
    {
        return (IGozerSessionInfo) getSession();
    }

    public Page getLastPage()
    {
        return _lastPage;
    }

    public boolean hasLastPage()
    {
        return _lastPage != null;
    }

    public void setLastPage(Page lastPage)
    {
        _lastPage = lastPage;
    }
}
