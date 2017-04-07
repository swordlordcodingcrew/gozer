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
** $Id: IDataViewChangedListener.java 1333 2011-12-23 14:29:57Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.databinding;

import java.util.EventListener;

import com.swordlord.jalapeno.datarow.DataRowBase;

public interface IDataViewChangedListener extends EventListener
{
    /**
     * Method called as the current row may have changed.
     * 
     * @param row
     *            The new current row which may be null
     */
	public void currentRowChanged(DataRowBase row);
	public void resetCache(DataRowBase row);
}
