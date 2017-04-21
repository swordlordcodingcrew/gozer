/*-----------------------------------------------------------------------------
**
** -Gozer is not Zuul-
**
** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
** and individual authors
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
** $Id: $
**
-----------------------------------------------------------------------------*/

package com.swordlord.common.prefs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author LordEidi
 *
 */
@SuppressWarnings("serial")
public class UserPrefsFactory
{
	private static final Log LOG = LogFactory.getLog(UserPrefsFactory.class);

    private static UserPrefs _userPref_instance;

	/**
     *
     */
	// TODO: Implementation
    public static IUserPrefs getUserPrefs()
    {
        if (_userPref_instance == null)
        {
            _userPref_instance = new UserPrefs("orico");
        }

        return _userPref_instance;
    }
}
