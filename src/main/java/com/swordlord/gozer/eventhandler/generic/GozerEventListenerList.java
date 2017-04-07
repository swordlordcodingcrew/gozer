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
** $Id: GozerEventListenerList.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.eventhandler.generic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;

import org.apache.commons.lang.NullArgumentException;

/**
 * Based on the EventListenerList from the javax.swing.event package
 * Redone to avoid Swing references in Wicket code.
 */
@SuppressWarnings("serial")
public class GozerEventListenerList implements Serializable
{
	// A null array to be shared by all empty listener lists
	private final static Object[] NULL_ARRAY = new Object[0];
	
	// The list of ListenerType - Listener pairs */
	protected transient Object[] listenerList = NULL_ARRAY;

	public Object[] getListenerList() 
	{
		return listenerList;
	}

	@SuppressWarnings("unchecked")
	public <T extends GozerEventListener> T[] getListeners(Class<T> t) 
	{
		Object[] lList = listenerList;
		int nbr = getListenerCount(lList, t);
		T[] result = (T[])Array.newInstance(t, nbr);
		int j = 0;

		for (int i = lList.length - 2; i >= 0; i-=2) 
		{
			if (lList[i].equals(t)) 
			{
				result[j++] = (T)lList[i+1];
			}
		}
	    return result;
	}

	public int getListenerCount() 
	{
		return listenerList.length / 2;
	}

	public int getListenerCount(Class<?> t) 
	{
		Object[] lList = listenerList;
		
		return getListenerCount(lList, t);
	}
	
	private int getListenerCount(Object[] list, Class<?> t) 
	{
		int count = 0;
		for (int i = 0; i < list.length; i+=2) 
		{
			if (t == (Class<?>)list[i])
			{
		         count++;
			}
		}
		return count;
	}
	
	 /**
	  * Adds the listener as a listener of the specified type.
	  * @param t the type of the listener to be added
	  * @param l the listener to be added
	  */
	public synchronized <T extends GozerEventListener> void add(Class<T> t, T l) 
	{
		if (l == null) 
	    {
			throw new NullArgumentException("Listener is null");
	    }
		
		if (!t.isInstance(l)) 
		{
			throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
		}
			     
		if (listenerList == NULL_ARRAY) 
		{
			// if this is the first listener added,
			// initialize the lists
			listenerList = new Object[] { t, l };
		} 
		else 
		{
			// Otherwise copy the array and add the new listener
			int i = listenerList.length;
			Object[] tmp = new Object[i+2];
			System.arraycopy(listenerList, 0, tmp, 0, i);
			
			tmp[i] = t;
			tmp[i+1] = l;

			listenerList = tmp;
		}
	}
	
	/**
	  * Removes the listener as a listener of the specified type.
	  * @param t the type of the listener to be removed
	  * @param l the listener to be removed
	 */
	public synchronized <T extends GozerEventListener> void remove(Class<T> t, T l) 
	{
		if (l ==null) 
		{
			throw new NullArgumentException("Listener is null");
		}
		
		if (!t.isInstance(l)) 
		{
		    throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
		}
		
		// Is l on the list?
		int index = -1;
		for (int i = listenerList.length-2; i>=0; i-=2) 
		{
		    if ((listenerList[i]==t) && (listenerList[i+1].equals(l))) 
		    {
		        index = i;
		        break;
		    }
		}
		 
		// If so, remove it
		if (index != -1) 
		{
			Object[] tmp = new Object[listenerList.length-2];
			// Copy the list up to index
			
			System.arraycopy(listenerList, 0, tmp, 0, index);
			// Copy from two past the index, up to
			// the end of tmp (which is two elements
			// shorter than the old list)
			if (index < tmp.length)
			{
			         System.arraycopy(listenerList, index+2, tmp, index, tmp.length - index);
			}
			
			// set the listener array to the new array or null
			listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
		}
	}
	
	     // Serialization support.
	private void writeObject(ObjectOutputStream s) throws IOException 
	{
		Object[] lList = listenerList;
	    s.defaultWriteObject();
	     
		// Save the non-null event listeners:
		for (int i = 0; i < lList.length; i+=2) 
		{
		 	Class<?> t = (Class<?>)lList[i];
		    GozerEventListener l = (GozerEventListener)lList[i+1];
		    if ((l!=null) && (l instanceof Serializable)) 
		    {
			    s.writeObject(t.getName());
			    s.writeObject(l);
			}
		}
	     
		s.writeObject(null);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException 
	{
		listenerList = NULL_ARRAY;
	    s.defaultReadObject();
	    Object listenerTypeOrNull;
	     
		while (null != (listenerTypeOrNull = s.readObject())) 
		{
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
		    GozerEventListener l = (GozerEventListener)s.readObject();
		    
		    add((Class<GozerEventListener>)Class.forName((String)listenerTypeOrNull, true, cl), l);
		}
	}
	
	/**
	* Returns a string representation of the EventListenerList.
	*/
	public String toString() 
	{
		Object[] lList = listenerList;
	    String s = "GozerEventListenerList: ";
	    s += lList.length/2 + " listeners: ";
	    for (int i = 0 ; i <= lList.length-2 ; i+=2) 
	    {
	    	s += " type " + ((Class<?>)lList[i]).getName();
	        s += " listener " + lList[i+1];
	    }
	    return s;
	}
}
