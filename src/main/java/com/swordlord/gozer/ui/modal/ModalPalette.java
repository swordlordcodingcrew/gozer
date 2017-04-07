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
package com.swordlord.gozer.ui.modal;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.markup.html.form.palette.component.Recorder;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * @param <T>
 */
@SuppressWarnings("serial")
public class ModalPalette<T> extends Palette<T>
{
	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.form.palette.Palette#getCSS()
	 */
	// TODO Fix this?
    protected PackageResourceReference getCSS()
	{
        return new PackageResourceReference(Palette.class, "gozerpalette.css");
	}

	public ModalPalette(String id, IModel<List<T>> model,
			IModel<? extends Collection<? extends T>> choicesModel, IChoiceRenderer<T> choiceRenderer,
					int rows, boolean allowOrder)
	{
		super(id, model, choicesModel, choiceRenderer, rows, allowOrder);
	}
	
	@Override
	protected Recorder<T> newRecorderComponent()
	{
		Recorder<T> recorder = super.newRecorderComponent();    
		
		recorder.add(new AjaxFormComponentUpdatingBehavior("onchange") 
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				// This event can be null, since we are only interested in the updated model!!!
				//System.out.println(target);
			}
		});
		return recorder;
	}
}
