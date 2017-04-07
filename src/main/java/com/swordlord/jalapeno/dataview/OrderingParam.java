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
 ** $Id: OrderingParam.java 1291 2011-12-12 19:25:11Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.dataview;

import java.io.Serializable;
import java.text.MessageFormat;

import org.apache.cayenne.query.SortOrder;

/**
 * Row ordering parameter.
 */
@SuppressWarnings("serial")
public class OrderingParam implements Serializable
{
    private String _strField;
    private boolean _bAscending;

    /** Flag indicating if the current row selection should be kept or not. */
    private boolean _keepSelection = true;

    /**
     * @param strField
     *            sort property
     * @param bAscending
     *            sort direction
     */
    public OrderingParam(String strField, boolean bAscending)
    {
        _strField = strField;
        _bAscending = bAscending;
    }

    /**
     * @param strField
     *            sort property
     * @param bAscending
     *            sort direction
     * @param keepSelection
     *            True to keep, false to dismiss the current selection
     */
    public OrderingParam(String strField, boolean bAscending, boolean keepSelection)
    {
        _strField = strField;
        _bAscending = bAscending;
        _keepSelection = keepSelection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = prime + (isAscending() ? 1231 : 1237);
        result = prime * result + getField().hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object rhs)
    {
        if (rhs instanceof OrderingParam)
        {
            OrderingParam param = (OrderingParam) rhs;
            return getField().equals(param.getField()) && (isAscending() == param.isAscending());
        }
        return false;
    }

    /**
     * @return sort field
     */
    public String getField()
    {
        return _strField;
    }

    /**
     * @return true if sort direction is ascending, false otherwise
     */
    public boolean isAscending()
    {
        return _bAscending;
    }

    /**
     * Returns wherever the current row selection should be kept or not.
     * 
     * @return True to keep, false to dismiss the current selection
     */
    public boolean isKeepSelection()
    {
        return _keepSelection;
    }

    /**
     * Sets wherever the current row selection should be kept or not.
     * 
     * @param keepSelection
     *            True to keep, false to dismiss the current selection
     */
    public void setKeepSelection(boolean keepSelection)
    {
        _keepSelection = keepSelection;
    }

    /**
     * @return org.apache.cayenne.query.SortOrder#ASCENDING if sort direction is
     *         ascending, org.apache.cayenne.query.SortOrder#DESCENDING
     *         otherwise
     */
    public SortOrder getSortOrder()
    {
        if (_bAscending)
        {
            return SortOrder.ASCENDING;
        }
        return SortOrder.DESCENDING;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return MessageFormat.format("[SortParam property={0},ascending={1}]", getField(), isAscending());
    }
}
