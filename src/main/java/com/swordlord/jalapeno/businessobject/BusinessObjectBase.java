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

package com.swordlord.jalapeno.businessobject;

import java.io.Serializable;

import com.swordlord.jalapeno.datacontainer.DataContainer;

@SuppressWarnings("serial")
public abstract class BusinessObjectBase implements Serializable
{
	protected DataContainer _dc;

	public BusinessObjectBase(DataContainer dc)
	{
		super();
		_dc = dc;
	}

	public DataContainer getDC()
	{
		return _dc;
	}

	public abstract void loadDC();

	public void persistDC()
	{
		_dc.persist();
	}

	public void rollbackDC()
	{
		_dc.rollback();
	}
}
