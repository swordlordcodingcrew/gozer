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
** $Id: FeedbackLabel.java 1363 2012-10-19 15:22:22Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.utils;

import java.util.Iterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Label displaying feedback messages for FormComponents on GozerPanel.
 * <p>
 * You can use this Label to show the error message near the actual FormComponent, instead of in the FeedbackPanel
 */
@SuppressWarnings("serial")
public class FeedbackLabel extends Label 
{
    /** Field component holds a reference to the {@link Component} this FeedbackLabel belongs to */
    private FormComponent _component;
    
    /** Field text holds a model of the text to be shown in the FeedbackLabel */
    private IModel<String> _caption;

    /**
     * Call this constructor if you just want to display the FeedbackMessage of the component
     * @param id The non-null id of this component
     * @param component The {@link FormComponent} to show the FeedbackMessage for.
     */
    public FeedbackLabel(String id, FormComponent component) 
    {
        super(id);
        
        _component = component;
    }

    /**
     * Call this constructor if you want to display a custom text
     * @param id The non-null id of this component
     * @param component The {@link FormComponent} to show the custom text for.
     * @param text The custom text to show when the FormComponent has a FeedbackMessage
     */
    public FeedbackLabel(String id, FormComponent component, String caption) 
    {
        this(id, component, new Model<String>(caption));
    }

    /**
     * Call this constructor if you want to display a custom model (for easy i18n)
     * @param id The non-null id of this component
     * @param component The {@link FormComponent} to show the custom model for.
     * @param iModel The custom model to show when the {@link FormComponent} has a FeedbackMessage
     */
    public FeedbackLabel(String id, FormComponent component, IModel<String> iModel) 
    {
        super(id);
        
        _component = component;
        _caption = iModel;
    }

    /**
     * Set the content of this FeedbackLabel, depending on if the component has a FeedbackMessage.
     *
     * The HTML class attribute will be filled with the error level of the feedback message. That way, you can easily
     * style different messages differently. Examples:
     *
     * class = "feedback INFO"
     * class = "feedback ERROR"
     * class = "feedback DEBUG"
     * class = "feedback FATAL"
     *
     * @see org.apache.wicket.Component#onBeforeRender()
     */
    @Override
    protected void onBeforeRender() 
    {
        super.onBeforeRender();

        setDefaultModel(null);
        
        if (_component.getFeedbackMessages() != null)
        {
            String strMessages = "";

            FeedbackMessages fm = _component.getFeedbackMessages();
            Iterator<FeedbackMessage> it = fm.iterator();
            while (it.hasNext())
            {
                strMessages += it.next().toString();
            }

            if(_caption != null) 
            {
                this.setDefaultModel(_caption);
            } 
            else 
            {
                this.setDefaultModel(new Model<String>(strMessages));
            }

            this.add(AttributeModifier.append("class", new Model<String>("feedback" + strMessages)));
        } 
        else 
        {
            this.setDefaultModel(null);
        }
    }
}
