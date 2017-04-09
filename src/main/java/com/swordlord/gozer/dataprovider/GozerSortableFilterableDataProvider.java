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
** $Id: $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.dataprovider;

import java.util.Iterator;

import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

/**
 * {@link GozerSortableDataProvider} with filter support.<br>
 * The currently used filter state implementation is {@link FilterState}.
 */
@SuppressWarnings("serial")
public class GozerSortableFilterableDataProvider extends GozerSortableDataProvider implements IFilterStateLocator
{
    protected static final Log LOG = LogFactory.getLog(GozerSortableFilterableDataProvider.class);

    /** The current filter state or null. */
    private FilterState _filterState;

    /**
     * @param fe
     * @param form
     */
    public GozerSortableFilterableDataProvider(IGozerFrameExtension fe, ObjectBase form)
    {
        this(fe, form, null);
    }

    /**
     * @param fe
     * @param form
     * @param sp
     */
    public GozerSortableFilterableDataProvider(IGozerFrameExtension fe, ObjectBase form, SortParam sp)
    {
        super(fe, form, sp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilterState getFilterState()
    {
        if (_filterState == null)
        {
            _filterState = new FilterState();
        }
        return _filterState;
    }

    /**
     * Returns wherever {@link #_filterState} is set or not.
     * 
     * @return True if there is a filter
     */
    private boolean filterStateIsInitialised()
    {
        return _filterState != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilterState(Object state)
    {
        if (state == null || state instanceof FilterState)
        {
            _filterState = (FilterState) state;
        }
        else
        {
            throw new IllegalArgumentException("Not expected argument type: " + FilterState.class.getName() + " expected but got " + state.getClass().getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataRowBase> iterator(long first, long count)
    {
        if (filterStateIsInitialised())
        {
            getDBManager().setFilter(getDBMember(), _filterState.getFilter());
        }

        return super.iterator(first, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size()
    {
        if (filterStateIsInitialised())
        {
            getDBManager().setFilter(getDBMember(), _filterState.getFilter());
        }
        return super.size();
    }
}
