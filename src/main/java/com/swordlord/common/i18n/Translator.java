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

package com.swordlord.common.i18n;

import java.text.MessageFormat;

import com.swordlord.common.prefs.IUserPrefs;
import com.swordlord.common.prefs.UserPrefsFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author LordFilu, LordEidi
 *
 */
@SuppressWarnings("serial")
public class Translator implements ITranslator
{
	private static final Log LOG = LogFactory.getLog(Translator.class);
	private static final IUserPrefs PREFS = UserPrefsFactory.getUserPrefs();

	private String _languageCode;

    /**
     * 
     */
    public Translator()
    {
    	// TODO: Theoretically we could get the language code for every user from the session
    	// but since the meta data is in one language only, we consider today not to do so
    	// but we could change that in the future.
    	_languageCode = PREFS.getLanguageCode();
    }

    /**
     * @param key
     * @param classname
     * @return
     */
    public String getString(String key, Class<?> classname)
    {
    	// TODO: add an escaper here, so that key values could be anything...
    	if (key == null)
    	{
    		return key;
    	}

    	StringBuffer combinedKey = new StringBuffer();
    	if (classname != null)
    	{
    		combinedKey.append(classname.getName());
    		combinedKey.append('.');
    	}

    	combinedKey.append(key);

    	return getValue(combinedKey.toString());
    }

    /**
     * @param strPath
     * @param key
     * @param params
     * @return
     */
    public String getString(String strPath, String key, String... params)
    {
    	return MessageFormat.format(getString(strPath, key), params);
    }

    /**
     * @param strPath
     * @param key
     * @return
     */
    public String getString(String strPath, String key)
    {
    	// TODO: add an escaper here, so that key values could be anything...
    	if (key == null)
    	{
    		return key;
    	}

    	StringBuffer combinedKey = new StringBuffer();
    	if (strPath != null)
    	{
    		combinedKey.append(strPath);
    		combinedKey.append('.');
    	}

    	combinedKey.append(key);

    	return getValue(combinedKey.toString());
    }

    private String getValue(String strKey)
    {
    	String result = strKey;
    	
    	return result;
    }
}
