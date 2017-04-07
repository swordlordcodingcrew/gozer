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
** $Id: DataBindingElement.java 1295 2011-12-15 15:41:02Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.databinding;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataBindingElement implements Serializable
{
	// table{filter=1,filter=3}[1] -> is one element
	// table 				= dataBindingPathElement
	// filter=1,filter=3 	= filter
	// 1					= row number, starting at 0 (zero)
	// -> if contains no row number, expected to be field (with no filter and no positioning.

	protected String _strPathElement;
	protected String _strFilter;
	protected int _nRowNumber;
	protected boolean _field;

	public DataBindingElement()
	{
		_nRowNumber = 0;
	}

	public String getFilter()
	{
		return _strFilter;
	}

	public String getPathElement()
	{
		return _strPathElement;
	}

	public int getRowNumber()
	{
		return _nRowNumber;
	}

	public boolean hasFilter()
	{
		return (_strFilter != null) && (_strFilter.length() > 0);
	}

	public boolean isField()
	{
		return _field;
	}

	public void setField(boolean field)
	{
		_field = field;
	}

	public void setFilter(String strFilter)
	{
		_strFilter = strFilter;
	}

	public void setPathElement(String strPathElement)
	{
		_strPathElement = strPathElement;
	}

	public void setRowNumber(int nRowNumber)
	{
		_nRowNumber = nRowNumber;
	}
}
