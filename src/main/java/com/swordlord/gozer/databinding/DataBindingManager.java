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
 ** $Id: DataBindingManager.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
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
 ** $Id: DataBindingManager.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.databinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.apache.cayenne.access.ToManyList;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.commons.lang.NullArgumentException;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;
import com.swordlord.jalapeno.datatable.DataTableBase;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.jalapeno.dataview.OrderingParam;
import com.swordlord.jalapeno.dataview.ViewFilter;
import com.swordlord.jalapeno.dataview.ViewFilterBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@SuppressWarnings("serial")
public class DataBindingManager implements Serializable
{
    protected static final Log LOG = LogFactory.getLog(DataBindingManager.class);

    // DataBinding needs to register itself for updates
    protected EventListenerList _listChangedEventHandler;

    protected HashMap<String, DataViewBase> _dataViews;
    protected DataContainer _dc;

    public DataBindingManager(DataContainer dc, DataBindingMember dataBindingMember)
    {
        _listChangedEventHandler = new EventListenerList();

        _dc = dc;
        _dataViews = new HashMap<String, DataViewBase>();

        updatePath(dataBindingMember);

        this.moveFirst(dataBindingMember);
    }

    public DataContainer getDataContainer()
    {
        return _dc;
    }

    public int getCount(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return -1;
        }

        return dvb.size();
    }

    // Returns the row of the root table (needed for colouring the lists on
    // linked tables
    public DataRowBase getCurrentRootRow(DataBindingMember dbm)
    {
        DataViewBase dvb = getRootDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return null;
        }

        return dvb.getCurrentRow();
    }

    public DataRowBase getCurrentRow(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return null;
        }

        return dvb.getCurrentRow();
    }

    public void deleteCurrentRow(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb != null)
        {
            dvb.deleteCurrentRow();
        }
    }

    public DataViewBase getDataView(DataBindingMember dbm)
    {
        return getDataViewFromBindingMember(dbm);
    }

    public DataViewBase getDataViewFromPathName(String strPath)
    {
        return _dataViews.get(strPath);
    }

    private DataViewBase getDataViewFromBindingMember(DataBindingMember dbm)
    {
        DataViewBase dvb = _dataViews.get(dbm.getDataBindingPathName());
        if (dvb == null)
        {
            // lazy update
            updatePath(dbm);
            dvb = _dataViews.get(dbm.getDataBindingPathName());
        }
        return dvb;
    }

    public int getPosition(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return -1;
        }

        return dvb.getPosition();
    }

    // TODO: This is hell of an ugly hack.
    // lets redo this as fast as possible
    public DataRowBase getResolvedRow(DataBindingMember dbm, DataRowBase rowFather)
    {
        LinkedList<DataBindingElement> elements = dbm.getDataBindingElements();
        return getResolvedRow(elements, dbm.getDataBindingRootTableName(), rowFather);
    }

    public DataRowBase getResolvedRow(LinkedList<DataBindingElement> elements, String strTableName, DataRowBase rowFather)
    {
        if ((elements == null) || (elements.size() == 0))
        {
            LOG.error("elements is empty");
        }

        int i = 0;
        boolean propertyFound = true;
        for (DataBindingElement element : elements)
        {
            i++;
            if (!element.isField() && i < elements.size())
            {
                String strPathElement = element.getPathElement();

                // ignore the same level
                if (strPathElement.compareToIgnoreCase(strTableName) != 0)
                {
                    Object property = rowFather.getProperty(strPathElement);

                    if (property == null)
                    {
                        // continuous with next
                        propertyFound = false;
                    }
                    else
                    {
                        propertyFound = true;
                        if (property instanceof DataRowBase)
                        {
                            rowFather = (DataRowBase) property;
                        }
                        else if (property instanceof ToManyList)
                        {
                            ToManyList list = (ToManyList) property;
                            if (list.size() > 0)
                            {
                                rowFather = (DataRowBase) list.get(element.getRowNumber());
                            }
                        }
                    }
                    // this is some nice side effect where we just loop as long
                    // as we
                    // are not at the right level
                }
            }
        }

        if (propertyFound)
            return rowFather;
        else
            return null;

    }

    /**
     * TODO need to merge with getResolvedRow(DataBindingMember dbm, DataRowBase
     * rowFather). Today there is too many risk. We need a better Parser and a
     * lot of JUnit Tests.
     * 
     * @param dbm
     * @param rowFather
     * @return
     */
    public DataRowBase[] getResolvedRows(DataBindingMember dbm, DataRowBase rowFather)
    {
        LinkedList<DataBindingElement> elements = dbm.getDataBindingElements();
        return getResolvedRows(elements, dbm.getDataBindingRootTableName(), rowFather);
    }

    /**
     * TODO need to merge with getResolvedRow(LinkedList<DataBindingElement>
     * elements, String strTableName, DataRowBase rowFather). Today there is too
     * many risk. We need a better Parser and a lot of JUnit Tests.
     * 
     * @param elements
     * @param strTableName
     * @param rowFather
     * @return
     */
    public DataRowBase[] getResolvedRows(LinkedList<DataBindingElement> elements, String strTableName, DataRowBase rowFather)
    {
        if ((elements == null) || (elements.size() == 0))
        {
            LOG.error("elements is empty");
        }

        ArrayList<DataRowBase> dataRowBaseLst = new ArrayList<DataRowBase>();
        boolean rowFound = false;
        for (DataBindingElement element : elements)
        {
            if (!element.isField())
            {
                String strPathElement = element.getPathElement();

                // ignore the same level
                if (strPathElement.compareToIgnoreCase(strTableName) != 0)
                {
                    Object property = rowFather.getProperty(strPathElement);
                    if (property == null)
                    {
                        return new DataRowBase[0];
                    }
                    else
                    {
                        if (property instanceof DataRowBase)
                        {
                            rowFather = (DataRowBase) property;
                            rowFound = true;
                        }
                        else if (property instanceof ToManyList)
                        {
                            ToManyList list = (ToManyList) property;
                            if (list.size() > 0)
                            {
                                if (elements.get(elements.size() - 2).equals(element))
                                { // Last element without field name
                                    if (list.getValueDirectly() == null)
                                    {
                                        try
                                        {
                                            rowFather = (DataRowBase) list.get(element.getRowNumber());
                                            rowFound = true;
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            return new DataRowBase[0];
                                        }
                                        dataRowBaseLst.add(rowFather);
                                        return dataRowBaseLst.toArray(new DataRowBase[dataRowBaseLst.size()]);
                                    }
                                    else
                                    {
                                        List<DataRowBase> lst = (List) list.getValueDirectly();
                                        return lst.toArray(new DataRowBase[lst.size()]);
                                    }
                                }
                                else
                                {
                                    rowFather = (DataRowBase) list.get(element.getRowNumber());
                                    rowFound = true;
                                }
                            }
                        }
                    }
                    // this is some nice side effect where we just loop as long
                    // as we
                    // are not at the right level
                }
            }
        }

        if (rowFound)
        {
            dataRowBaseLst.add(rowFather);
            return dataRowBaseLst.toArray(new DataRowBase[dataRowBaseLst.size()]);
        }
        else
        {
            return new DataRowBase[0];
        }
    }

    // Returns the dataview from the root table
    private DataViewBase getRootDataViewFromBindingMember(DataBindingMember dbm)
    {
        DataViewBase dvb = _dataViews.get(dbm.getDataBindingRootTableName());
        if (dvb == null)
        {
            // lazy update
            updatePath(dbm);
            dvb = _dataViews.get(dbm.getDataBindingRootTableName());
        }
        return dvb;
    }

    public List<DataRowBase> getRows(DataBindingMember dbm)
    {
        return getRows(dbm, null);
    }

    public List<DataRowBase> getRows(DataBindingMember dbm, long fromIndex, long toIndex)
    {
        return getRows(dbm, fromIndex, toIndex, null);
    }

    public List<DataRowBase> getRows(DataBindingMember dbm, long fromIndex, long toIndex, OrderingParam orderingParam)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return null;
        }

        // only replace the ordering when there already is an ordering!
        if (orderingParam != null)
        {
            String sort = orderingParam.getField();
            String path = dbm.getDataBindingPathName();

            path = path.replace(dbm.getDataBindingRootTableName() + ".", "");

            sort = sort.replace(path + ".", "");

            orderingParam = new OrderingParam(sort, orderingParam.isAscending());
        }

        return dvb.getRows(fromIndex, toIndex, orderingParam);
    }

    public List<DataRowBase> getRows(DataBindingMember dbm, OrderingParam orderingParam)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return null;
        }

        return dvb.getRows(orderingParam);
    }

    public void setFilter(DataBindingMember dbm, ViewFilter filter)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb != null)
        {
            dvb.setFilter(filter);
        }
    }

    /**
     * Sets dataview ordering of the passed binding member (if there is one).
     * 
     * @param dbm
     *            The binding member for which to configure the data view
     * @param ordering
     *            The desired ordering
     */
    public void setOrdering(DataBindingMember dbm, OrderingParam ordering)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb != null)
        {
            dvb.setOrdering(ordering);
        }
    }

    public boolean move(DataBindingMember dbm, DataRowKeyBase key)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return false;
        }

        return dvb.move(key);
    }

    public void moveFirst(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return;
        }

        dvb.moveFirst();
    }

    public void moveLast(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return;
        }

        dvb.moveLast();
    }

    public void moveNext(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return;
        }

        dvb.moveNext();
    }

    public void movePrevious(DataBindingMember dbm)
    {
        DataViewBase dvb = getDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return;
        }

        dvb.movePrevious();
    }

    public boolean moveRoot(DataBindingMember dbm, DataRowKeyBase key)
    {
        DataViewBase dvb = getRootDataViewFromBindingMember(dbm);
        if (dvb == null)
        {
            return false;
        }

        return dvb.move(key);
    }

    private void updatePath(DataBindingMember dbm)
    {
        LinkedList<DataBindingElement> elements = dbm.getDataBindingElements();
        if ((elements == null) || (elements.size() == 0))
        {
            throw new NullArgumentException("dataBindingMember");
        }

        ObjEntity oeLastTable = null;
        ObjEntity oeCurrentTable = null;

        String strConcatPath = "";

        EntityResolver er = _dc.getObjectContext().getEntityResolver();

        for (DataBindingElement element : elements)
        {
            String strPathElement = element.getPathElement();

            // Find table from binding info
            if (strPathElement.startsWith("rel_"))
            {
                ObjRelationship rel = (ObjRelationship) oeLastTable.getRelationship(strPathElement);

                if (rel == null)
                {
                    LOG.error("Relation is not known: " + strPathElement);
                    throw new NullArgumentException(strPathElement);
                }

                oeCurrentTable = (ObjEntity) rel.getTargetEntity();
            }
            else
            {
                oeCurrentTable = er.getObjEntity(strPathElement);
            }

            // add the whole path instead of current element only to make sure
            // that binding over the same table multiple times does not confuse
            // the binding
            String strFather = strConcatPath;
            strConcatPath += strConcatPath.length() > 0 ? "." + strPathElement : strPathElement;
            if (!_dataViews.containsKey(strConcatPath) && !element.isField())
            {
                String strTableName = oeCurrentTable.getClassName();

                strTableName = strTableName.replaceAll("DataRow", "DataTable");
                strTableName = strTableName.replaceAll("datarow", "datatable");

                DataTableBase dtb = _dc.getTableByAbsoluteName(strTableName);
                if (dtb == null)
                {
                    LOG.info("Table can not be found: " + strTableName);
                }

                DataViewBase dvFather = null;

                if (strFather.length() > 0)
                {
                    dvFather = _dataViews.get(strFather);
                }

                DataViewBase dvb = new DataViewBase(strConcatPath, strPathElement, dvFather, dtb);
                dvb.setPosition(element.getRowNumber());

                if (element.hasFilter())
                {
                    dvb.setFilter(ViewFilterBuilder.createSimple(element.getFilter()));
                }

                // Store the dataviewbase under the name of the path element so
                // that no
                // further lookup is needed to find dataview from path element
                // during binding
                _dataViews.put(strConcatPath, dvb);
            }

            // set last table so that next path element can load correct
            // objentity.
            oeLastTable = oeCurrentTable;
        }
    }

    /**
     * Search the parent DataViewBase from the given DataViewBase
     * 
     * @param dataViewBase
     *            The Child DataViewBase
     * @return
     */
    public DataViewBase getParentDataViewBase(String dataViewName)
    {
        String dvbNameParent = dataViewName.substring(0, dataViewName.lastIndexOf("."));

        return _dataViews.get(dvbNameParent);
    }

    /**
     * Refresh the parent
     * 
     * @param dbMember
     */
    public void refreshParent(DataBindingMember dbMember)
    {
        DataViewBase dv = getDataView(dbMember);
        DataViewBase dvParent = getParentDataViewBase(dv.getAbsoluteDataBindingPathName());

        dvParent.fireCurrentRowChangedEvent();
    }
}
