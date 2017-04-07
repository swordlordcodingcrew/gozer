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
** $Id: DispatchableEvent.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datatable.event;

import java.util.EventListener;
import java.util.EventObject;

public abstract class DispatchableEvent extends EventObject 
{
	public DispatchableEvent(Object source) 
	{
		super(source);
	}

	public abstract void dispatch(EventListener listener);

	public String toString() 
	{
		String cn = getClass().getName();
		return String.format("{0} [{1}]", cn.substring(cn.lastIndexOf('.')+1), paramString());
	}

	protected String paramString() 
	{
		return "";
	}
}
