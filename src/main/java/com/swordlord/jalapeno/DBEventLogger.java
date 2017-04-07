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
** $Id: DBGenerator.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno;

import org.apache.cayenne.access.translator.DbAttributeBinding;
import org.apache.cayenne.conn.DataSourceInfo;
import org.apache.cayenne.log.JdbcEventLogger;
import org.apache.cayenne.map.DbAttribute;

import java.util.List;

public class DBEventLogger implements JdbcEventLogger {

    @Override
    public void log(String s) {

    }

    /**
     * @param s
     * @deprecated
     */
    @Override
    public void logConnect(String s) {

    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @deprecated
     */
    @Override
    public void logConnect(String s, String s1, String s2) {

    }

    /**
     * @param dataSourceInfo
     * @deprecated
     */
    @Override
    public void logPoolCreated(DataSourceInfo dataSourceInfo) {

    }

    /**
     * @deprecated
     */
    @Override
    public void logConnectSuccess() {

    }

    /**
     * @param throwable
     * @deprecated
     */
    @Override
    public void logConnectFailure(Throwable throwable) {

    }

    @Override
    public void logGeneratedKey(DbAttribute dbAttribute, Object o) {

    }

    /**
     * @param s
     * @param list
     * @deprecated
     */
    @Override
    public void logQuery(String s, List<?> list) {

    }

    /**
     * @param s
     * @param list
     * @param list1
     * @param l
     * @deprecated
     */
    @Override
    public void logQuery(String s, List<DbAttribute> list, List<?> list1, long l) {

    }

    @Override
    public void logQuery(String s, DbAttributeBinding[] dbAttributeBindings, long l) {

    }

    @Override
    public void logQueryParameters(String s, DbAttributeBinding[] dbAttributeBindings) {

    }

    /**
     * @param s
     * @param list
     * @param list1
     * @param b
     * @deprecated
     */
    @Override
    public void logQueryParameters(String s, List<DbAttribute> list, List<Object> list1, boolean b) {

    }

    @Override
    public void logSelectCount(int i, long l) {

    }

    @Override
    public void logSelectCount(int i, long l, String s) {

    }

    @Override
    public void logUpdateCount(int i) {

    }

    @Override
    public void logBeginTransaction(String s) {

    }

    @Override
    public void logCommitTransaction(String s) {

    }

    @Override
    public void logRollbackTransaction(String s) {

    }

    @Override
    public void logQueryError(Throwable throwable) {

    }

    @Override
    public boolean isLoggable() {
        return false;
    }
}
