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
** $Id: DataTableEventDispatcher.java 1290 2011-12-12 17:42:55Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datatable.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class DataTableEventDispatcher implements Serializable
{
	private static final long serialVersionUID = 6757614880822120347L;

    protected transient List<DataTableChangeListener> listeners = new ArrayList<DataTableChangeListener>(1);

	public void dispatch(DispatchableEvent e) 
	{
	    EventListener[] listenersCopy = null;
	    synchronized(this) 
	    {
	    	if (hasListeners()) listenersCopy = listeners.toArray(new EventListener[listeners.size()]);
	    }

	    if (listenersCopy != null) 
	    {
	    	int count = listenersCopy.length;
	    	for (int index = 0; index < count; ++index) 
	    	{
	    		e.dispatch(listenersCopy[index]);
	    	}
	    }
	}

	public synchronized boolean hasListeners() 
	{
	    return !listeners.isEmpty();
	}
	
	public synchronized int getListenerCount() 
	{
		return listeners.size();
	}
	
	public synchronized int find(DataTableChangeListener listener) 
	{
		return listeners.indexOf(listener);
	}
	
	public synchronized void add(DataTableChangeListener listener) 
	{
		if (find(listener) < 0) listeners.add(listener);
	}
	
	public synchronized void remove(DataTableChangeListener listener) 
	{
		listeners.remove(listener);
	}
	
	public synchronized void clear() 
	{
		listeners.clear();
	}

	public static DataTableEventDispatcher add(DataTableEventDispatcher dispatcher, DataTableChangeListener listener) 
	{
		if (dispatcher == null) dispatcher = new DataTableEventDispatcher();
	    
		dispatcher.add(listener);
	
	    return dispatcher;
	}
	
	public final static DataTableEventDispatcher remove(DataTableEventDispatcher dispatcher, DataTableChangeListener listener) 
	{
		if (dispatcher != null) 
		{
	    	dispatcher.remove(listener);
	    	if (!dispatcher.hasListeners()) dispatcher = null;
	    }
	    return dispatcher;
	}
}
