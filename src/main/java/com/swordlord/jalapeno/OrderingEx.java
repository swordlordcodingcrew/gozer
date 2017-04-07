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
** $Id: LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno;

import java.text.MessageFormat;
import java.util.List;

import org.apache.cayenne.access.ToManyList;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SortOrder;
import org.apache.cayenne.util.ConversionUtil;
import org.apache.log4j.Logger;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datatable.DataTableBase;


@SuppressWarnings("serial")
public class OrderingEx extends Ordering
{
	protected static final Log LOG = LogFactory.getLog(REPLACEME);
	protected DataTableBase _dt;
	protected String _strField;

	public OrderingEx() { }
	
    public OrderingEx(DataTableBase dt, String sortPathSpec, SortOrder sortOrder) 
    {
    	super(sortPathSpec, sortOrder);
        
        setDataTable(dt);
    }
    
    @Deprecated
    public OrderingEx(DataTableBase dt, String sortPathSpec, boolean ascending) 
    {
        super(sortPathSpec, ascending);
        
        setDataTable(dt);
    }

    @Deprecated
    public OrderingEx(DataTableBase dt, String sortPathSpec, boolean ascending, boolean caseInsensitive) 
    {
        super(sortPathSpec, ascending, caseInsensitive);
        
        setDataTable(dt);
    }

    @Deprecated
    public OrderingEx(DataTableBase dt, Expression sortExpression, boolean ascending) 
    {
        super(sortExpression, ascending);
        
    	setDataTable(dt);
    }

    @Deprecated
    public OrderingEx(DataTableBase dt, Expression sortExpression, boolean ascending, boolean caseInsensitive) 
    {
    	super(sortExpression, ascending, caseInsensitive);
    	
    	setDataTable(dt);
    }
    
    public void setDataTable(DataTableBase dt) 
    {
    	Expression sortSpec = getSortSpec();
    	if(sortSpec == null) return;
    	
    	String strExpression = sortSpec.toString();
    	int iPosition = strExpression.lastIndexOf(".");
    	
    	DataContainer dc = dt.getDataContainer();
    	
    	// no second table, ask the source
    	if(iPosition < 0)
    	{
    		if(dt.FieldHasOwnComparator(strExpression))
        	{
        		_dt = dt;
        		_strField = strExpression;
        	}
    		return;
    	}
    	
    	String strPath = strExpression.substring(0, iPosition);
    	
    	Object obj = null;
    	
    	try
    	{
    		Expression exp = Expression.fromString(strPath);
    		obj = exp.evaluate(dt.getDataRow(0));
    	}
    	catch(Exception e)
    	{
    		LOG.error(MessageFormat.format("Parsing of expression {0} crashed with reason {1}.", strPath, e.getMessage()));
    	}
    	
    	// TODO: we should probably rewrite this so that we ask Cayenne Meta Information
    	// -> problem is that this won't work as long as there are no rows!
    	if(obj != null)
    	{
    		DataTableBase dtTarget;
    		
    		if(obj instanceof DataRowBase)
    		{
    			DataRowBase row = (DataRowBase)obj;
    	    	dtTarget = dc.getTableFromRow(row.getClass());
    		}
    		else if(obj instanceof ToManyList)
    		{
    			ToManyList list = (ToManyList)obj;
    			DataRowBase row = (DataRowBase) list.get(0);
    	    	dtTarget = dc.getTableFromRow(row.getClass());
    		}
    		else
    		{
    			LOG.error("obj is of wrong type!");
    			return;
    		}
	    	
	    	String strField = strExpression.substring(iPosition + 1);
	    	if(dtTarget.FieldHasOwnComparator(strField))
	    	{
	    		_dt = dtTarget;
	    		_strField = strField;
	    	}
    	}
    }
	
	@Override
	public int compare(Object o1, Object o2) 
	{
		Expression exp = getSortSpec();
        Object value1 = exp.evaluate(o1);
        Object value2 = exp.evaluate(o2);
        
        if(value1 instanceof List)
        {
        	List list = (List)value1;
        	if(list.size() > 0)
        	{
        		value1 = list.get(0);
        	}
        }
        if(value2 instanceof List)
        {
        	List list = (List)value2;
        	if(list.size() > 0)
        	{
        		value2 = list.get(0);
        	}
        }
        
        // nulls first policy... maybe make this configurable as some DB do
        if (value1 == null) {
            return (value2 == null) ? 0 : -1;
        }
        else if (value2 == null) {
            return 1;
        }

        if (this.isCaseInsensitive()) 
        {
            // TODO: to upper case should probably be defined as a separate expression
            // type
            value1 = ConversionUtil.toUpperCase(value1);
            value2 = ConversionUtil.toUpperCase(value2);
        }

		if(_dt != null)
		{
			return _dt.compare(isAscending(), _strField, value1, value2);
		}
		else
		{	
	        int compareResult = ConversionUtil.toComparable(value1).compareTo(ConversionUtil.toComparable(value2));
	        return (isAscending()) ? compareResult : -compareResult;
		}
    }
	
	@Override
    public Expression getSortSpec()
    {
        if (sortSpecString == null)
        {
            return null;
        }

        // compile on demand
        if (sortSpec == null)
        {
            sortSpec = Expression.fromString(sortSpecString.replace(":", "."));
        }

        return sortSpec;
    }
}
