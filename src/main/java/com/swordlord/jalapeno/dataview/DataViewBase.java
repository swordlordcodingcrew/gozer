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
 ** $Id: DataViewBase.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.dataview;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.swordlord.jalapeno.DBGenerator;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.ToManyList;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.IDataViewChangedListener;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;
import com.swordlord.jalapeno.datatable.DataTableBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * In memory view of an {@link DataTableBase} or of dependent rows from a parent
 * {@link DataViewBase}.
 */
@SuppressWarnings("serial")
public class DataViewBase implements Serializable, Iterable<DataRowBase>, IDataViewChangedListener
{
    protected static final Log LOG = LogFactory.getLog(DataViewBase.class);

    // DataBinding needs to register itself for updates
    private final EventListenerList _dataViewChangedEventHandler = new EventListenerList();

    /**
     * List containing the rows to use as replacement of {@link #_dataTable} or
     * null if not in use. This list is intended to be updated only through
     * {@link #currentRowChanged(DataRowBase)}.
     */
    private List<DataRowBase> _virtualTableContent;

    /** The underlying data table or null. */
    private final DataTableBase _dataTable;

    /**
     * The 'parent' view or null. If set then the data show in this view depends
     * on the current row of this view.
     */
    private final DataViewBase _dvFather;

    /**
     * binding path details. the whole binding path (which is also the name
     * under which this dv is stored in theDataBindingManager)
     */
    private final String _strFullBindingPath;

    /**
     * the last path segment (which is basically the path between parent dv and
     * this dv)
     */
    private final String _strPathSegment;

    /** The filter for this view or null. */
    private ViewFilter _filter;

    /** The view ordering or null. */
    private OrderingParam _orderingParam;

    /**
     * The 'last' known position of the current row - to get the real current
     * row we must use the {@link #_currentRowKey}.
     */
    private int _currentRowPosition = 0;

    /** The key of the current row or null as this field gets lazy initialized. */
    private DataRowKeyBase _currentRowKey;

    public DataViewBase(String strFullBindingPath, String strPathSegment, DataViewBase dvFather, DataTableBase dataTable)
    {
        _strFullBindingPath = strFullBindingPath;
        _strPathSegment = strPathSegment;
        _dataTable = dataTable;
        _dvFather = dvFather;

        // initialize dependent view data layer
        if (_dvFather != null)
        {
            _dvFather.registerDataViewChangedListener(this);
            currentRowChanged(_dvFather.getCurrentRow());
        }
    }

    /**
     * Looks up the index of the row with the given key.
     * 
     * @param key
     *            The row key
     * @return The index of the given row or 0 if the row was not found
     */
    public int findPosition(DataRowKeyBase key)
    {
        return lookup(getRowList(), key, 0);
    }

    /**
     * Inform all {@link IDataViewChangedListener} that the current row may have
     * changed.
     */
    public void fireCurrentRowChangedEvent()
    {
        if (_dataViewChangedEventHandler.getListenerCount() == 0)
        {
            return;
        }
        final DataRowBase row = getCurrentRow();
        for (IDataViewChangedListener listener : _dataViewChangedEventHandler.getListeners(IDataViewChangedListener.class))
        {
            listener.currentRowChanged(row);
        }
    }

    /**
     * Returns the currently 'selected' row.
     * 
     * @return The current row or null
     */
    public DataRowBase getCurrentRow()
    {
        final List<DataRowBase> rows = getRowList();
        if (rows.isEmpty())
        {
            return null;
        }
        return rows.get(_currentRowPosition);
    }

    /**
     * Recomputes the {@link #_currentRowPosition} according to the
     * {@link #_currentRowKey}.<br>
     * Call this method after every operation which potentially changes the
     * order/content of the views rows.
     */
    private void recomputeCurrentRowPosition()
    {
        if (_currentRowKey != null)
        {
            // simply trigger a load - the load will update the current row
            // position
            getRows();
        }
    }

    /**
     * Updates/initializes the {@link #_currentRowKey} and
     * {@link #_currentRowPosition} according to the supplied rows.
     * 
     * @param rows
     *            The current row data
     */
    private void updateCurrentRowPointer(List<DataRowBase> rows)
    {
        // update row position if the key of the current row is known
        if (_currentRowKey != null)
        {
            updateRowPosition(rows);
        }
        // lazy initialization of the current row key
        else if (_currentRowPosition < rows.size())
        {
            _currentRowKey = rows.get(_currentRowPosition).getKey();
        }
        // the current row position is bigger than the row count - reset it to 0
        else
        {
            resetPosition();
        }
    }

    /**
     * Updates the {@link #_currentRowPosition} so that it points again on the
     * row belonging to the key {@link #_currentRowKey} (if any).
     * 
     * @param rows
     *            The current row data
     */
    private void updateRowPosition(List<DataRowBase> rows)
    {
        // are we able to update the index and is it necessary?
        if (_currentRowKey == null || _currentRowPosition < rows.size() && _currentRowKey.equals(rows.get(_currentRowPosition).getKey()))
        {
            return;
        }
        final int index = lookup(rows, _currentRowKey, -1);
        // key does not exist - select the first row
        if (index == -1)
        {
            resetPosition();
        }
        else
        {
            _currentRowPosition = index;
        }
    }

    /**
     * Resets the {@link #_currentRowPosition} and the {@link #_currentRowKey}.
     */
    private void resetPosition()
    {
        _currentRowPosition = 0;
        _currentRowKey = null;
        fireCurrentRowChangedEvent();
    }

    /**
     * Looks up the index of the given key inside the passed row list.
     * 
     * @param rows
     *            The rows
     * @param key
     *            The key to lookup or null
     * @param fallback
     *            The fallback value to return if no matching entry was found
     * @return The index of the specified key it its found or the passed
     *         fallback value
     */
    private int lookup(List<DataRowBase> rows, DataRowKeyBase key, int fallback)
    {
        if (key == null)
        {
            return fallback;
        }
        // try to find the row with the specified row key
        for (int i = 0, cnt = rows.size(); i < cnt; i++)
        {
            if (rows.get(i).getKey().equals(key))
            {
                return i;
            }
        }
        return fallback;
    }

    /**
     * Deletes the current row, if any.
     */
    public void deleteCurrentRow()
    {
        final List<DataRowBase> rows = getRowList();
        if (rows.size() > 0)
        {
            DataRowBase row = rows.get(_currentRowPosition);
            getDataTable().getDataContainer().deleteDataRow(row);

            // remove cached data so that the UI reflects the deletion
            _virtualTableContent = null;
        }

        moveFirst();
        // reload data for dependent views
        if (_dvFather != null)
        {
            currentRowChanged(_dvFather.getCurrentRow());
        }
    }

    /**
     * Returns the data table on which this view is based.
     * 
     * @return The underlying table or null
     */
    public DataTableBase getDataTable()
    {
        return _dataTable;
    }

    /**
     * Returns the index of the current row inside {@link #getRows()}.
     * 
     * @return The index of the current row
     */
    public int getPosition()
    {
        return _currentRowPosition;
    }

    /**
     * Null safe version of {@link #getRows()} - instead of null this method
     * will return an empty list.
     * 
     * @return The rows
     */
    private List<DataRowBase> getRowList()
    {
        final List<DataRowBase> rows = getRows();
        return rows != null ? rows : Collections.<DataRowBase> emptyList();
    }

    /**
     * Returns the data rows (filtered and ordered).
     * 
     * @return The rows or null if no data could be retrieved
     */
    public List<DataRowBase> getRows()
    {
        List<DataRowBase> rows;
        if (_virtualTableContent != null)
        {
            rows = _virtualTableContent;
        }
        else if (_dataTable == null)
        {
            return null;
        }
        else
        {
            rows = _dataTable.getDataRows();
        }

        // Apply filter and/or sorting
        if (rows.size() > 0 && (_filter != null || (_orderingParam != null)))
        {
            final DataTableBase dt = getDataTable();
            final DataBindingContext context = dt.getDataContainer().getDataBindingContext();
            rows = DataContainer.filterAndOrderDataRows(rows, _filter, _orderingParam, context, dt);
        }

        updateCurrentRowPointer(rows);
        return rows;
    }

    public List<DataRowBase> getRows(long fromIndex, long toIndex, OrderingParam orderingParam)
    {
        if (size() > 0)
        {
            return getRows(orderingParam).subList((int) fromIndex, (int) toIndex);
        }
        return null;
    }

    public List<DataRowBase> getRows(OrderingParam orderingParam)
    {
        if (orderingParam != null)
        {
            setOrdering(orderingParam);
        }
        return getRows();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataRowBase> iterator()
    {
        return getRows().iterator();
    }

    /**
     * Sets the specified row as new current row.
     * 
     * @param key
     *            The key of the new current row
     * @return True if the position could have changed
     */
    public boolean move(DataRowKeyBase key)
    {
        return setPosition(findPosition(key));
    }

    /**
     * Sets the first row as current row.
     */
    public void moveFirst()
    {
        setPosition(0, false, true);
    }

    /**
     * Sets the last row as current row.
     */
    public void moveLast()
    {
        setPosition(size() - 1, false, true);
    }

    /**
     * Sets the next row as current row (i.e. {@link #getPosition()}+1).
     */
    public void moveNext()
    {
        setPosition(_currentRowPosition + 1, true, true);
    }

    /**
     * Sets the previous row as current row (i.e. {@link #getPosition()}-1).
     */
    public void movePrevious()
    {
        setPosition(_currentRowPosition - 1, true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void currentRowChanged(DataRowBase rowFather)
    {
        if (rowFather == null)
        {
            _virtualTableContent = new ArrayList<DataRowBase>();
            moveFirst();
            return;
        }

        String strMessage = "PositionChanged on DV {0} on table {1} with father {2}";
        LOG.info(MessageFormat.format(strMessage, _strFullBindingPath, _dataTable.getTableId(), rowFather.getTableName()));

        // how to refresh the father
        // first I thought this is necessary, but it seems not. If it becomes
        // necessary, here is how to do it!
        // ObjectIdQuery query = new ObjectIdQuery(rowFather.getObjectId(),
        // false, ObjectIdQuery.CACHE_NOREFRESH);
        // DataRowBase fatherRefreshed = (DataRowBase)
        // DataObjectUtils.objectForQuery(rowFather.getObjectContext(), query);

        // if(fatherRefreshed != null)
        // {
        // rowFather = fatherRefreshed;
        // }

        // check if the property is set.
        // if not, this relation is not set yet (probably new record),
        // so we just add zero rows to clear any old data
        Object property = rowFather.getProperty(_strPathSegment);
        if (property == null)
        {
            _virtualTableContent = new ArrayList<DataRowBase>();
            moveFirst();
            return;
        }

        // Otherwise we try to set the data we received from the property
        // this is either a list of datarows (to-many) or a single datarow
        // (to-one).
        if (property instanceof ToManyList)
        {
            _virtualTableContent = new ArrayList<DataRowBase>();
            // filter deleted
            for (DataRowBase row : (Collection<DataRowBase>) ((ToManyList) property))
            {
                final int state = row.getPersistenceState();
                if (state != PersistenceState.DELETED && state != PersistenceState.TRANSIENT)
                {
                    _virtualTableContent.add(row);
                }
            }
            moveFirst();
        }
        else if (property instanceof DataRowBase)
        {
            _virtualTableContent = new ArrayList<DataRowBase>();
            _virtualTableContent.add((DataRowBase) property);
            moveFirst();
        }
        else
        {
            try
            {
                LOG.error(MessageFormat.format("Property: {0} Name: {1} Type: {2}.", property, _strPathSegment, property.getClass().getCanonicalName()));
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage() + " - " + e.getCause());
            }
        }
    }

    private void registerDataViewChangedListener(IDataViewChangedListener listener)
    {
        LOG.info("Registering PDataViewChangedListener: " + listener.getClass().getCanonicalName() + " in: " + _dataTable.getAbsoluteTableName());
        _dataViewChangedEventHandler.add(IDataViewChangedListener.class, listener);
    }

    /**
     * Sets the current view filter.
     * 
     * @param filter
     *            The filter or null
     */
    public void setFilter(ViewFilter filter)
    {
        if (_filter != null && _filter.equals(filter))
        {
            return;
        }
        _filter = filter;
        recomputeCurrentRowPosition();
    }

    /**
     * Returns the currently applied filter.
     * 
     * @return The filter or null
     */
    public ViewFilter getFilter()
    {
        return _filter;
    }

    /**
     * Sets the ordering of the table.
     * 
     * @param orderingParam
     *            The ordering to apply
     */
    public void setOrdering(OrderingParam orderingParam)
    {
        _orderingParam = orderingParam;
        if (orderingParam.isKeepSelection())
        {
            recomputeCurrentRowPosition();
        }
        else
        {
            moveFirst();
        }
    }

    /**
     * Returns the currently applied ordering.
     * 
     * @return The ordering or null
     */
    public OrderingParam getOrderingParam()
    {
        return _orderingParam;
    }

    /**
     * Sets the row at the specified index as new current row.
     * 
     * @param nPosition
     *            The index of the new current row
     * @return True if the position could be changed
     */
    public boolean setPosition(int nPosition)
    {
        return setPosition(nPosition, false, false);
    }

    /**
     * Changes the current position ({@link #_currentRowPosition}) and the
     * corresponding key ({@link #_currentRowKey}).<br>
     * If a (forced) change occurred {@link #fireCurrentRowChangedEvent()} will
     * be called.
     * 
     * @param pos
     *            The position to set
     * @param adjust
     *            True if the position should be adjusted in case of
     *            over-/underflow
     * @param force
     *            Forces the update - thus ignore the case if
     *            {@link #_currentRowPosition} has already the desired value
     * @return True if the current position was changed, false otherwise
     */
    private boolean setPosition(int pos, boolean adjust, boolean force)
    {
        // shortcut evaluation if the position stays the same and no data was
        // yet loaded
        if (_currentRowPosition == pos && _currentRowKey == null && !force)
        {
            return false;
        }
        // only reset the current row key if there are no rows
        final List<DataRowBase> rows = getRowList();
        if (rows.isEmpty())
        {
            _currentRowKey = null;
            return false;
        }
        // honor boundaries
        if (adjust)
        {
            pos = Math.max(0, Math.min(pos, rows.size() - 1));
        }
        else if (pos < 0 || pos >= rows.size())
        {
            return false;
        }
        _currentRowPosition = pos;
        final DataRowKeyBase key = rows.get(pos).getKey();
        // fire only on change or if forced
        if (force || !key.equals(_currentRowKey))
        {
            _currentRowKey = key;
            fireCurrentRowChangedEvent();
        }
        return true;
    }

    /**
     * Returns the number of rows in this view.
     * 
     * @return The number of rows
     */
    public int size()
    {
        return getRowList().size();
    }

    public void unregisterPositionChangedListener(IDataViewChangedListener listener)
    {
        _dataViewChangedEventHandler.remove(IDataViewChangedListener.class, listener);
    }

    public String getAbsoluteDataBindingPathName()
    {
        return _strFullBindingPath;
    }

    /**
     * Empty the cache
     */
    public void resetCache()
    {
        _virtualTableContent = new ArrayList<DataRowBase>();
        fireResetCacheEvent();

        if (_dvFather != null)
            currentRowChanged(_dvFather.getCurrentRow());

    }

    /**
     * Reset the cache for all children.
     */
    private void fireResetCacheEvent()
    {
        if (_dataViewChangedEventHandler.getListenerCount() == 0)
        {
            return;
        }

        final DataRowBase currentRow = getCurrentRow();
        for (IDataViewChangedListener listener : _dataViewChangedEventHandler.getListeners(IDataViewChangedListener.class))
        {
            listener.resetCache(currentRow);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetCache(DataRowBase rowFather)
    {
        _virtualTableContent = new ArrayList<DataRowBase>();
    }
}
