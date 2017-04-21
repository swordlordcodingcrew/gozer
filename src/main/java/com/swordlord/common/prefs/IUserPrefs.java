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

public interface IUserPrefs {

    String resolveTargetUI();
    String getCayenneConfig();

    String getAppConf(String strVar, String strDefault);
    String getAppConf(String strVar, String strDefault, String strSection);

    String getLanguageCode();
    int getLanguageCodeAsInt();

    String getProperty(String strVar, String strDefault, Class oClass);
    String getProperty(String strVar, String strDefault, String strSection);

    boolean getPropertyAsBoolean(String strVar, boolean bDefault, Class oClass);
    int getPropertyAsInt(String strVar, int nDefault, Class oClass);
    int getPropertyAsInt(String strVar, int nDefault, String strSection);
    String getVersion();
    boolean isDebugMode();
    void save();
    void setLanguageCode(String strLanguageCode);
    void setProperty(String strVar, boolean b, Class oClass);
    void setProperty(String strVar, String strDefault, Class oClass);
    void setProperty(String strVar, String strDefault, String strSection);
    void setPropertyAsInt(String strVar, int nDefault, Class oClass);
    void setPropertyAsInt(String strVar, int nDefault, String strSection);
}
