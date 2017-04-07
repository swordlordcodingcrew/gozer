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
 ** $Id: DataBindingContext.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.databinding;

import java.io.Serializable;
import java.util.HashMap;

import com.swordlord.gozer.frame.IGozerFrameExtension;

@SuppressWarnings("serial")
public abstract class DataBindingContext implements Serializable
{
	private IGozerFrameExtension _gfe;
	private HashMap<String, DataBindingManager> _hmDataBindingManagers;

	protected DataBindingContext(HashMap<String, DataBindingManager> dataBindingManagers, IGozerFrameExtension gfe)
	{
		_hmDataBindingManagers = dataBindingManagers;
		_gfe = gfe;
	}
	
	protected HashMap<String, DataBindingManager> getDataBindingManagers()
	{
		return _hmDataBindingManagers;
	}

	public DataBindingManager getDataBindingManager(DataBindingMember dataBindingMember)
	{
		return getDataBindingManager(dataBindingMember, true);
	}

	public DataBindingManager getDataBindingManager(DataBindingMember dataBindingMember, boolean createNewDataBindingManager)
	{
		String strKey = dataBindingMember.getDataBindingRootTableName();

		if (getDataBindingManagers().containsKey(strKey))
		{
			return getDataBindingManagers().get(strKey);
		}

		// TODO
		// Migration to Java 6 -> strKey.. isEmpty()
		if (createNewDataBindingManager && (strKey != null && !strKey.equals("")))
		{
			// else create new one
			DataBindingManager dbm = new DataBindingManager(_gfe.getDataContainer(), dataBindingMember);
			getDataBindingManagers().put(strKey, dbm);
			return dbm;
		} 
		else
		{
			return null;
		}

	}

	public IGozerFrameExtension getGozerFrameExtension()
	{
		return _gfe;
	}
}