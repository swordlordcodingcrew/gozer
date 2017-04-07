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
** $Id: LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.eventhandler.generic;

import org.apache.wicket.ajax.AjaxRequestTarget;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.databinding.DataBinding;

@SuppressWarnings("serial")
public class GozerActionEvent extends GozerEvent
{
    private AjaxRequestTarget target;
    
    public GozerActionEvent(IGozerAction source)
	{
		super(source);
	}

    public String getActionID()
    {
    	return getSource().getActionID();
    }

    public DataBinding getDataBinding()
    {
    	return getSource().getDataBinding();
    }

    @Override
    public IGozerAction getSource()
    {
    	return (IGozerAction) this.source;
    }

    /**
     * Gets the value of the target property
     * 
     * @return possible object is {@link AjaxRequestTarget}
     */
    public AjaxRequestTarget getTarget()
    {
        return target;
    }

    /**
     * Sets the value of the target property
     * 
     * @param target
     *            allowed object is {@link AjaxRequestTarget}
     */
    public void setTarget(AjaxRequestTarget target)
    {
        this.target = target;
    }
    
    
}
