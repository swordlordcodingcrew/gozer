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
 ** $Id: TextFilterEx.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.filter;

import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.AbstractFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.wicket.utils.FeedbackLabel;

/**
 * Filter for {@link Date} fields.
 * 
 * @author LordEidi
 */
@SuppressWarnings("serial")
public class DateFilter extends AbstractFilter
{
    /**
     * Constructor.
     * 
     * @param id
     *            component id
     * @param model
     *            model for the underlying form component
     * @param form
     *            filter form this filter will be added to
     * @param autoSubmit
     *            True to trigger a submit on focus lost
     */
    public DateFilter(String id, IModel<Date> model, FilterForm form, boolean autoSubmit)
    {
        super(id, form);
        final DateTextField filter = new DateTextField("filter", model);

        if (autoSubmit)
        {
            filter.add(new AttributeModifier("onblur", new Model<String>("this.form.submit();")));
        }
        enableFocusTracking(filter);
        // configure error message/feedback
        final FeedbackLabel feedbackLabel = new FeedbackLabel("feedback", filter);
        feedbackLabel.setOutputMarkupId(true);
        // TODO error handling
        // filter.add(new ErrorBehaviour("onblur", feedbackLabel));
        add(feedbackLabel);
        add(filter);
    }
}