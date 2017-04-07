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
** $Id: DataTableChangeEvent.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datatable.event;

import java.util.EventListener;

@SuppressWarnings("serial")
public class DataTableChangeEvent extends DispatchableEvent 
{
	public static final int DATAROW_ADDED         = 1;
	public static final int DATAROW_REMOVED       = 2;
	public static final int DATAROW_CHANGED       = 3;
	public static final int DATAROWS_CHANGED      = 4;

	private int id;
	private int affectedDataRowIndex;

	public DataTableChangeEvent(Object source, int id) 
	{
		this(source, id, -1);
	}

	public DataTableChangeEvent(Object source, int id, int affectedDataRowIndex) 
	{
		super(source);
		this.id = id;
		this.affectedDataRowIndex = affectedDataRowIndex;
	}

	public void dispatch(EventListener listener) 
	{
		((DataTableChangeListener)listener).dataChanged(this);
	}
	
	public boolean isMultiObjectChange() 
	{
	    return affectedDataRowIndex == -1;
	}
	
	public int getAffectedDataObjectIndex() 
	{
	    return affectedDataRowIndex;
	}
	
	public final int getId() 
	{
	    return id;
	}
	
	public String toString() 
	{
	    return String.format("{0} {1}", super.toString(), id);
	}
}
