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
 ** $Id: $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.form;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;

/**
 * Form that uses a random uuid token in order to prevent nasty people from
 * messing around (also known as cross site request forgery) ;)
 * 
 * @author LordEidi
 * @param <T>
 */

@SuppressWarnings("serial")
public class SecureForm<T> extends Form<T> implements IFormValidator
{
	protected static final Log LOG = LogFactory.getLog(SecureForm.class);

	private static final String TOKEN_PREFIX = "SECURE_FORM_TOKEN";

	private String _strToken;
	
    /**
     * @param id
     */
	public SecureForm(final String id)
	{
		super(id);
		
		_strToken = UUID.randomUUID().toString();

        add(this);
	}

	@Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		super.onComponentTagBody(markupStream, openTag);
		
		// token is set, protection to not reset it again
		// otherwise this would count as a tampering attempt!
		getResponse().write("<input type='hidden' name='" + SecureForm.TOKEN_PREFIX + "' value='" + _strToken + "'/>");
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public FormComponent<?>[] getDependentFormComponents()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Form<?> form)
    {
        // if (!_strToken.equalsIgnoreCase(form.get getRequest()
        // getParameter(SecureForm.TOKEN_PREFIX)))
        // {
        // The form was tampered with, log out and show the access denied page
        // LOG.warn("The current form was tampered with. User would be logged out now.");

        // TODO Fix this: Using Modal Windows somehow changes the token!?

        // SecuredWebSession session = SecuredWebSession.get();
        // session.signOut();

        // throw new RestartResponseException(AccessDeniedPage.class);
        // }

        // super.validate(formComponent);
    }
}
