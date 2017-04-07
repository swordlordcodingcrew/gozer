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
** $Id: ErrorBehaviour.java 1371 2013-05-26 15:48:00Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.utils;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;

/**
 * Behavior that checks if a {@link FormComponent} is valid. Valid {@link FormComponent} objects get the CSS class
 * 'formcomponent valid' and invalid {@link FormComponent} objects get the CSS class 'formcomponent invalid'.
 *
 * See {@link AjaxFormComponentUpdatingBehavior} for more details over the parent class.
 */
@SuppressWarnings("serial")
public class ErrorBehaviour extends AjaxFormComponentUpdatingBehavior 
{
    /** Field updateComponent holds the component that must be updated when validation is done.*/
    private Component _updateComponent;

    /**
     * Constructor.
     *
     * @param event of type {@link String} (for example 'onblur', 'onkeyup', etc.)
     * @param updateComponent is the {@link Component} that must be updated (for example the {@link FeedbackLabel}
     *        containing the error message for this {@link FormComponent})  
     */
    public ErrorBehaviour(String event, Component updateComponent) 
    {
        super(event);
    
        _updateComponent = updateComponent;
    }

    /**
     * Listener invoked on the ajax request. This listener is invoked after the {@link Component}'s model has been
     * updated. Handles the change of a css class when an error has occurred.
     *
     * @param ajaxRequestTarget of type AjaxRequestTarget
     * @param e of type RuntimeException
     */
    @Override
    protected void onError(AjaxRequestTarget ajaxRequestTarget, RuntimeException e) 
    {
        changeCssClass(ajaxRequestTarget, false, "invalid");
    }

    /**
     * Listener invoked on the ajax request. This listener is invoked after the {@link Component}'s model has been
     * updated. Handles the change of a css class when validation was succesful.
     *
     * @param ajaxRequestTarget of type AjaxRequestTarget
     */
    @Override
    protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) 
    {
        changeCssClass(ajaxRequestTarget, true, "valid");
    }

    /**
     * Changes the CSS class of the linked {@link FormComponent} via AJAX.
     *
     * @param ajaxRequestTarget of type AjaxRequestTarget
     * @param valid Was the validation succesful?
     * @param cssClass The CSS class that must be set on the linked {@link FormComponent}
     */
    private void changeCssClass(AjaxRequestTarget ajaxRequestTarget, boolean valid, String cssClass) 
    {
        FormComponent formComponent = getFormComponent();

        if(formComponent.isValid() == valid)
        {
            formComponent.add(new AttributeModifier("class", new Model<String>("gozercomponent " + cssClass)));
            
            ajaxRequestTarget.add(formComponent);
        }

        if(_updateComponent != null)
        {
            ajaxRequestTarget.add(_updateComponent);
        }
    }
}

