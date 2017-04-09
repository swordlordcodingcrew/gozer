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
import java.util.List;

import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.OrderingParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * TODO JavaDoc for GozerSortableDataProvider.java
 * 
 * @author
 * 
 */
@SuppressWarnings("serial")
public class GozerSortableDataProvider extends SortableDataProvider<DataRowBase, String>
{
    protected static final Log LOG = LogFactory.getLog(GozerSortableDataProvider.class);

    private IGozerFrameExtension _gfe;
    private DataBindingManager _dbManager;
    private DataBindingMember _dbMember;

    /**
     * @param fe
     * @param form
     */
    public GozerSortableDataProvider(IGozerFrameExtension fe, ObjectBase form)
    {
        this(fe, form, null);
    }

    /**
     * @param fe
     * @param form
     * @param sp
     */
    public GozerSortableDataProvider(IGozerFrameExtension fe, ObjectBase form, SortParam<String> sp)
    {
        this._gfe = fe;

        _dbMember = form.getDataBindingMember();
        DataBindingContext dbc = _gfe.getDataBindingContext();
        _dbManager = dbc.getDataBindingManager(_dbMember);
        if (sp != null)
        {
            setSort(sp);
            _dbManager.setOrdering(_dbMember, new OrderingParam(sp.getProperty(), sp.isAscending(), false));
        }
    }

    /**
     * @return
     */
    protected DataBindingManager getDBManager()
    {
        return _dbManager;
    }

    /**
     * @return
     */
    protected DataBindingMember getDBMember()
    {
        return _dbMember;
    }

    @Override
    public Iterator<DataRowBase> iterator(long first, long count)
    {
        List<DataRowBase> subList = null;

        SortParam<String> sortParam = getSort();
        if (sortParam != null)
        {
            OrderingParam orderingParam = new OrderingParam(sortParam.getProperty(), sortParam.isAscending());
            subList = _dbManager.getRows(_dbMember, first, first + count, orderingParam);
        }
        else
        {
            subList = _dbManager.getRows(_dbMember, first, first + count);
        }

        if (subList == null)
        {
            return null;
        }

        return subList.iterator();
    }

    @Override
    public IModel<DataRowBase> model(DataRowBase object)
    {
        DataRowBase row = object;
        return new Model<DataRowBase>(row);
    }

    @Override
    public long size()
    {
        if (_dbManager == null)
        {
            LOG.error("_dbManager is null!");
            return 0;
        }

        return _dbManager.getCount(_dbMember);
    }
}
