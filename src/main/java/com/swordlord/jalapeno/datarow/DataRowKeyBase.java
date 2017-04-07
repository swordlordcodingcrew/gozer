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
** $Id: DataRowKeyBase.java 1312 2011-12-16 19:38:15Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datarow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.UUID;


@SuppressWarnings("serial")
public class DataRowKeyBase implements Serializable
{
	protected static final Log LOG = LogFactory.getLog(DataRowKeyBase.class);

	public static DataRowKeyBase fromString(String strKey)
	{
		return new DataRowKeyBase(UUID.fromString(strKey));
	}

	protected UUID _key;

	public DataRowKeyBase() { super(); }

	public DataRowKeyBase(UUID key)
	{
		super();
		_key = key;
	}

	public int compareTo(DataRowKeyBase key)
	{
		if(key == null) 
		{
			LOG.error("key must not be null!");
			return -1;
		}
		
		return _key.toString().compareToIgnoreCase(key.toString());
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return 31 + ((_key == null) ? 0 : _key.hashCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        // we do not check the key type - as per definition a UUID must be
        // unique...
        if (!(obj instanceof DataRowKeyBase))
        {
            return false;
        }
        final DataRowKeyBase other = (DataRowKeyBase) obj;
        if (_key == other._key)
        {
            return true;
        }
        return _key != null && _key.equals(other._key);
    }

    public UUID toUUID()
	{
		return _key;
	}

	@Override
	public String toString()
	{
		return _key.toString();
	}
}
