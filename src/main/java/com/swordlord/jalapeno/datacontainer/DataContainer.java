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
** $Id: DataContainer.java 1314 2011-12-19 17:27:08Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.datacontainer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.DeleteDenyException;
import org.apache.cayenne.FaultFailureException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.ObjectStore;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.BeanValidationFailure;
import org.apache.cayenne.validation.ValidationException;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.DBConnection;
import com.swordlord.jalapeno.OrderingEx;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datatable.DataTableBase;
import com.swordlord.jalapeno.dataview.OrderingParam;
import com.swordlord.jalapeno.dataview.ViewFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The Class DataContainer.
 */
@SuppressWarnings("serial")
public class DataContainer implements Serializable
{
	/** The Constant LOG. */
	protected static final Log LOG = LogFactory.getLog(DataContainer.class);

	/** The _data tables. */
	private Hashtable<String, DataTableBase> _dataTables;
	
	/** The _oc. */
	private ObjectContext _oc;
	
	/** The _session info. */
	private IGozerSessionInfo _sessionInfo;

	private DataBindingContext _dbContext;
	
	/** The _errors. */
	private LinkedList<String> _errors;
	
	// Disables the validation mechanism during the persist process if set to true
	private Boolean _bDisableValidation;
	
	/**
	 * Instantiates a new data container.
	 */
	public DataContainer()
	{
		_dataTables = new Hashtable<String, DataTableBase>();

		clearErrors();
	}

	/**
	 * Instantiates a new data container.
	 * 
	 * @param sessionInfo the session info
	 */
	public DataContainer(IGozerSessionInfo sessionInfo, DataBindingContext dbContext)
	{
		_dataTables = new Hashtable<String, DataTableBase>();
		
		_sessionInfo = sessionInfo;
		_dbContext = dbContext;

		clearErrors();
	}
	
	public DataContainer(IGozerSessionInfo sessionInfo)
	{
		_dataTables = new Hashtable<String, DataTableBase>();
		
		_sessionInfo = sessionInfo;

		clearErrors();
	}
	
	/**
	 * Adds the error.
	 * 
	 * @param strError the str error
	 */
	public void addError(String strError)
	{
		_errors.add(strError);
	}

	/**
	 * Adds the error.
	 * 
	 * @param strField the str field
	 * @param strError the str error
	 */
	public void addError(String strField, String strError)
	{
		// Field is not yet supported, but interface has it for compatibility reasons
		_errors.add(strError);
	}

	/**
	 * Adds the table.
	 * 
	 * @param dt the dt
	 */
	public void addTable(DataTableBase dt)
	{
		// TODO WARNING: This is an ugly hack and should be changed!
		if(!_dataTables.containsKey(dt.getAbsoluteTableName()))
		{
			_dataTables.put(dt.getAbsoluteTableName(), dt);
		}
	}
	
	
	/**
	 * Delete a DataRow.
	 * 
	 * @param row the DataRow
	 * 
	 * @return true, if successful
	 */
	public boolean deleteDataRow(DataRowBase row)
	{
		if(row == null) return false;
		
		ObjectContext oc = getObjectContext();
		
		try
		{
			oc.deleteObject(row);
		}
		catch(DeleteDenyException e)
		{
			LOG.error(e.getMessage());
			return false;
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage());
			return false;
		}
		
		return true;
	}

	/**
	 * Clear errors.
	 */
	public void clearErrors()
	{
		_errors = new LinkedList<String>();
	}

	/**
	 * Dump content to file.
	 */
	public void dumpContentToFile()
	{
		try
		{
			FileWriter fileWriter = new FileWriter("dump.txt");

			BufferedWriter buffWriter = new BufferedWriter(fileWriter);

			Enumeration<DataTableBase> list = _dataTables.elements();

			while(list.hasMoreElements())
			{
				DataTableBase dt = list.nextElement();
				buffWriter.write(dt.toHTML());
			}

			buffWriter.close();
		}
		catch(IOException e)
		{
			System.out.println("Exception "+ e);
		}
	}

	/**
	 * Dump content to string.
	 * 
	 * @return the string
	 */
	public String dumpContentToString()
	{
		StringBuilder sb = new StringBuilder();

		Enumeration<DataTableBase> list = _dataTables.elements();

		while(list.hasMoreElements())
		{
			DataTableBase dt = list.nextElement();
			sb.append(dt.toHTML() + "<br/>");
		}

		return sb.toString();
	}


	/**
	 * Gets the data rows.
	 * 
	 * @param dataRow the data row
	 * 
	 * @return the data rows
	 */
    public List<DataRowBase> getDataRows(Class<DataRowBase> dataRow)
	{
		ArrayList<DataRowBase> rows = new ArrayList<DataRowBase>();

		Iterator<DataRowBase> it = getObjectStore().getObjectIterator();
        while (it.hasNext())
        {
        	DataRowBase row = it.next();

        	if(row.getPersistenceState() != PersistenceState.DELETED)
        	{
	        	if(dataRow == row.getClass())
	        	{
	        		rows.add(row);
	        	}
        	}
        }

        return rows;
	}

	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * 
	 * @return the data rows
	 */
    public List<DataRowBase> getDataRows(DataTableBase dt)
	{
        return getDataRows(dt, null, (OrderingParam) null);
	}
	
	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * @param clazz the clazz
	 * 
	 * @return the data rows
	 */
    public <T> List<T> getDataRows(DataTableBase dt, Class<T> clazz)
	{
		return getDataRows(dt, null, null, clazz);
	}

	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * @param ordering the ordering
	 * @param clazz the clazz
	 * 
	 * @return the data rows
	 */
    public <T> List<T> getDataRows(DataTableBase dt, OrderingParam ordering, Class<T> clazz)
	{
		return getDataRows(dt, null, ordering, clazz);
	}
	
	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * @param ordering the ordering
	 * 
	 * @return the data rows
	 */
    public List<DataRowBase> getDataRows(DataTableBase dt, OrderingParam ordering)
	{
		return getDataRows(dt, null, ordering);
	}

	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * @param filter the str filter
	 * 
	 * @return the data rows
	 */
    public List<DataRowBase> getDataRows(DataTableBase dt, ViewFilter filter)
	{
		return getDataRows(dt, filter, null);
	}
	
	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * @param filter the str filter
	 * @param orderingParam the ordering param
	 * 
	 * @return the data rows
	 */
    public List<DataRowBase> getDataRows(DataTableBase dt, ViewFilter filter, OrderingParam orderingParam)
	{
		return getDataRows(dt, filter, orderingParam, DataRowBase.class);
	}
	
	/**
	 * Gets the data rows.
	 * 
	 * @param dt the dt
	 * @param filter the filter
	 * @param orderingParam the ordering param
	 * @param clazz the clazz
	 * 
	 * @return the data rows
	 */
    public <T> List<T> getDataRows(DataTableBase dt, ViewFilter filter, OrderingParam orderingParam, Class<T> clazz)
	{
		String strDataRowClassName = dt.getAbsoluteDataRowName();
		
		ArrayList<T> rows = new ArrayList<T>();
		
		Iterator<DataRowBase> it = getObjectStore().getObjectIterator();
        while (it.hasNext())
        {
        	DataRowBase row = it.next();
        	
        	// is this enough or do we have to filter TRANSIENT as well?
			// && row.getPersistenceState() != PersistenceState.TRANSIENT 
        	if(row.getPersistenceState() != PersistenceState.DELETED)
        	{
	        	if(strDataRowClassName.compareTo(row.getClass().getName()) == 0)
	        	{
	        		rows.add((T) row);
	        	}
        	}
        }

        return DataContainer.filterAndOrderDataRows(rows, filter, orderingParam, getDataBindingContext(), dt);
	}
	
    /**
     * Filters and orders the given rows according to the passed arguments.
     * 
     * @param <T>
     *            The row type
     * @param rows
     *            The rows to process
     * @param filter
     *            The filter or null
     * @param orderingParam
     *            The ordering or null
     * @param context
     *            The binding context or null
     * @param dt
     *            The table to which the rows belong
     * @return The processed rows
     */
    public static <T> List<T> filterAndOrderDataRows(List<T> rows, ViewFilter filter, OrderingParam orderingParam, DataBindingContext context, DataTableBase dt)
    {
        final List<T> filtered = filter(rows, filter, context, dt);
        if (orderingParam != null)
        {
            OrderingEx ordering = new OrderingEx(dt, orderingParam.getField(), orderingParam.getSortOrder());
            ordering.orderList(filtered);
        }
        return filtered;
    }

    /**
     * Filters the given rows.
     * 
     * @param <T>
     *            The row type
     * @param rows
     *            The rows to process
     * @param filter
     *            The filter or null
     * @param context
     *            The binding context or null
     * @param dt
     *            The table to which the rows belong
     * @return The filtered rows
     */
    private static <T> List<T> filter(List<T> rows, ViewFilter filter, DataBindingContext context, DataTableBase dt)
    {
        if (rows.isEmpty() || filter == null)
        {
            return rows;
        }
        final String strFilter = filter.getFilter();
        // Complex filters need to be created "manually"
        if (strFilter.charAt(0) == '?')
        {
            return complexFilter(rows, context, dt, strFilter);
        }
        // Trivial filters are done directly by Cayenne
        final Expression exp = Expression.fromString(strFilter);
        // expression without parameters?
        final Map<String, Object> args = filter.getArguments();
        if (args.isEmpty())
        {
            return new ArrayList<T>(exp.filterObjects(rows));
        }
        return new ArrayList<T>(exp.expWithParameters(args, false).filterObjects(rows));
    }

    /**
     * Filters the given rows using an complex filter.
     * 
     * @param <T>
     *            The row type
     * @param rows
     *            The rows to process
     * @param filter
     *            The complex filter
     * @param context
     *            The binding context or null
     * @param dt
     *            The table to which the rows belong
     * @return The filtered rows
     */
    private static <T> List<T> complexFilter(List<T> rows, DataBindingContext context, DataTableBase dt, String filter)
    {
        // Example of a filter: ?NotIn(threatId,@ActiveThreat[0]:threatId)
        final Pattern pattern = Pattern.compile("^(\\?)?([a-z]*)(\\(){1,1}([a-z]*)?(,){1,1}([(\\?)?a-z_0-9,:='%\\s@\\[\\]\\(\\)]*)?(\\)){1,1}$",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(filter);

        /*
         * The groups are as follows 1 - ! 2 - NotIn 3 - ( 4 - threatId 5 -
         * , 6 - @ActiveThreat[0]:threatId 7 - )
         */

        if (!matcher.find())
        {
            LOG.info("Parser can't parse filter: " + filter);
            return rows;
        }

        if (matcher.groupCount() != 7)
        {
            LOG.error("Wrong group count during parsing of filter: " + filter);
            return rows;
        }

        final String strCommand = matcher.group(2);
        final String strBoundField = matcher.group(4).replace(":", ".");
        final String strBindingMember = matcher.group(6).replace(":", ".");

        final DataBindingMember bm = new DataBindingMember(strBindingMember);

        // re-use the DataBindingContext when there is one
        // this is needed so that all currentRow informations are correct
        // within a filter
        final DataBindingManager dbm;
        if (context != null)
        {
            dbm = context.getDataBindingManager(bm);
        }
        else
        {
            dbm = new DataBindingManager(dt.getDataContainer(), bm);
        }

        // get all to-be-filtered records as field because the expression
        // filters on one single field and does no lookup
        final List<String> fieldsFilter = new ArrayList<String>();
        for (DataRowBase row : dbm.getRows(bm))
        {
            if (row.getPersistenceState() != PersistenceState.DELETED)
            {
                final String strFieldName = bm.getDataBindingFieldName();
                if (strFieldName == null || strFieldName.length() == 0)
                {
                    LOG.error("There must be something wrong with your filter. Field is empty: " + filter);
                }
                else
                {
                    fieldsFilter.add(row.getPropertyAsStringForce(strFieldName));
                }
            }
        }

        // Create the expression according to the binding information
        if (strCommand.equalsIgnoreCase("in"))
        {
            final Expression exp = ExpressionFactory.inExp(strBoundField, fieldsFilter);
            return new ArrayList<T>(exp.filterObjects(rows));
        }
        else if (strCommand.equalsIgnoreCase("notin"))
        {
            final Expression exp = ExpressionFactory.notInExp(strBoundField, fieldsFilter);
            return new ArrayList<T>(exp.filterObjects(rows));
        }
        else
        {
            LOG.warn("Unknown filter command: " + strCommand);
            return rows;
        }
    }

	/**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
    public List<String> getErrors()
	{
		return _errors;
	}

	/**
	 * Gets the object context.
	 * 
	 * @return the object context
	 */
	public ObjectContext getObjectContext()
	{
		if(_oc == null)
		{
			_oc = DBConnection.instance().getObjectContext(this);
		}
		return _oc;
	}

	/**
	 * Gets the object store.
	 * 
	 * @return the object store
	 */
	public ObjectStore getObjectStore()
	{
		DataContext dataContext = (DataContext)getObjectContext();
		if(dataContext == null)
		{
			return null;
		}

		return dataContext.getObjectStore();
	}

	/**
	 * Gets the session info.
	 * 
	 * @return the session info
	 */
	public IGozerSessionInfo getSessionInfo()
	{
		return _sessionInfo;
	}
	
	public DataBindingContext getDataBindingContext()
	{
		return _dbContext;
	}

	/**
	 * Gets the table.
	 * 
	 * @param clazz the clazz
	 * 
	 * @return the table
	 */
	public DataTableBase getTable(Class<? extends DataTableBase> clazz)
	{
		if(clazz == null)
		{
			throw new InternalError("Table is null (clazz)");
		}

		String strTableId = null;

		try
		{
			Constructor<? extends DataTableBase> ct = clazz.getConstructor(DataContainer.class);
			DataTableBase tab = ct.newInstance(this);
			strTableId = tab.getAbsoluteTableName();
		}
		catch(InstantiationException ex)
		{
			LOG.error(ex);
		}
		catch(IllegalAccessException ex)
		{
			LOG.error(ex);
		}
		catch(NoSuchMethodException ex)
		{
			LOG.error(ex);
		}
		catch(InvocationTargetException ex)
		{
			LOG.error(ex);
		}

		return getTableByAbsoluteName(strTableId);
	}

	/**
	 * Gets the table by absolute name.
	 * 
	 * @param strId the str id
	 * 
	 * @return the table by absolute name
	 */
	@SuppressWarnings("unchecked")
	public DataTableBase getTableByAbsoluteName(String strId)
	{
		if(strId == null)
		{
			throw new InternalError("Tablename is null (strId)");
		}
		
		if(!_dataTables.contains(strId))
		{
			// if not exists, create!
			try
			{
				Class<DataTableBase> clazz = (Class<DataTableBase>) Class.forName(strId);
				Constructor<DataTableBase> ct = clazz.getConstructor(DataContainer.class);
				DataTableBase tab = ct.newInstance(this);
				
				if(tab != null)
				{
					addTable(tab);
				}
			}
			catch(ClassNotFoundException ex)
			{
				LOG.error(ex);
			}
			catch(InstantiationException ex)
			{
				LOG.error(ex);
			}
			catch(IllegalAccessException ex)
			{
				LOG.error(ex);
			}
			catch(NoSuchMethodException ex)
			{
				LOG.error(ex);
			}
			catch(InvocationTargetException ex)
			{
				LOG.error(ex);
			}
		}

		return _dataTables.get(strId);
	}
	
	/**
	 * Gets the table from row.
	 * 
	 * @param clazz the clazz
	 * 
	 * @return the table from row
	 */
	public DataTableBase getTableFromRow(Class<? extends DataRowBase> clazz)
	{
		if(clazz == null)
		{
			throw new InternalError("DataRow is null (clazz)");
		}

		String strClassName = clazz.getName();

    	strClassName = strClassName.replaceAll("DataRow", "DataTable");
    	strClassName = strClassName.replaceAll("datarow", "datatable");

    	return getTableByAbsoluteName(strClassName);
	}

	/**
	 * Checks for errors.
	 * 
	 * @return true, if successful
	 */
	public boolean hasErrors()
	{
		return _errors.size() > 0;
	}

	/**
	 * Checks for session info.
	 * 
	 * @return true, if successful
	 */
	public boolean hasSessionInfo()
	{
		return _sessionInfo != null;
	}
	
	public boolean hasDataBindingContext()
	{
		return _dbContext != null;
	}
	
	public void setDisableValidation(boolean bDisabled)
	{
		_bDisableValidation = bDisabled;
	}
	
	public boolean getDisableValidation()
	{
		if(_bDisableValidation == null)
		{
			setDisableValidation(false);
		}
		
		return _bDisableValidation;
	}

	/**
	 * Persist.
	 * 
	 * @return true, if successful
	 */
	public boolean persist()
	{
		// iterate through all rows and clear errors
		clearErrors();
		return persistObjectContext(getObjectContext());
	}

	/**
	 * Persist object context.
	 * 
	 * @param oc the oc
	 * 
	 * @return true, if successful
	 */
	private boolean persistObjectContext(ObjectContext oc)
	{
		try
		{
			// disabling the context validation should only be used when you know what you do!
			if(getDisableValidation())
			{
				DataContext context = (DataContext) oc;
				context.setValidatingObjectsOnCommit(false);
			}
			oc.commitChanges();
		}
		catch(ValidationException vex)
		{
			String strMessage = "";

			ValidationResult vr = vex.getValidationResult();
			if(vr.hasFailures())
			{
				List<ValidationFailure> failures = vr.getFailures();
				if(failures.size() > 0)
				{
					Iterator<ValidationFailure> it = failures.iterator();
					while(it.hasNext())
					{
						ValidationFailure failure = it.next();
						if(failure != null)
						{
							addError(failure.getSource().toString(), failure.getDescription());

							LOG.info(failure.getSource() + " - " + failure.getDescription());
						}

						if(failure instanceof BeanValidationFailure)
						{
							BeanValidationFailure bvf = (BeanValidationFailure) failure;
							strMessage += bvf.getSource() + " - " + bvf.getProperty() + " - " + bvf.getDescription() + ".\n\r";
							LOG.error(bvf.getSource() + " - " + bvf.getProperty() + " - " + bvf.getDescription());
						}
					}
				}
			}

			String strError = "Persist crashed: " + vex.getMessage() + ": " + vex.getCause() + "\n\r" + strMessage;
			LOG.info(strError);

			strMessage = vex.getLocalizedMessage() + " - " + strMessage;

			Throwable cause = vex.getCause();
			if(cause != null)
			{
				strMessage += " - " + cause.getMessage();
			}

			addError("", strMessage);

			//ErrorDialog.reportError("validation: " + strMessage);
			return false;
		}
		catch(DeleteDenyException dde)
		{
			String strError = "Persist crashed: " + dde.getLocalizedMessage() + ": " + dde.getCause();
			LOG.error(strError);

			String strMessage = dde.getLocalizedMessage();

			addError("", strMessage);

			return false;
		}
		catch(FaultFailureException ffe)
		{
			String strError = "Persist crashed: " + ffe.getLocalizedMessage() + ": " + ffe.getCause();
			LOG.error(strError);

			String strMessage = ffe.getLocalizedMessage();

			addError("", strMessage);

			return false;
		}
		catch(CayenneRuntimeException cex)
		{
			String strError = "Persist crashed: " + cex.getMessage() + ": " + cex.getCause();
			LOG.error(strError);

			String strMessage = cex.getLocalizedMessage();

			addError("", strMessage);

			return false;
		}
		catch(NullPointerException e)
		{
			String strError = "Persist crashed: NullPointerException";
			LOG.error(strError);
			
			addError("", strError);
			LOG.debug(e.getMessage());

			return false;
		}
		catch(Exception e)
		{
			String strError = "Persist crashed: " + e.getLocalizedMessage() + ": " + e.getCause();
			LOG.error(strError);

			String strMessage = e.getLocalizedMessage();

			addError("", strMessage);

			return false;
		}

		return true;
	}

	/**
	 * Rollback.
	 */
	public void rollback()
	{
		rollbackObjectContext((DataContext)getObjectContext());
	}

	/**
	 * Rollback object context.
	 * 
	 * @param context the context
	 */
	private void rollbackObjectContext(DataContext context)
	{
		if(context.hasChanges())
		{
			context.rollbackChanges();
		}
	}

	/**
	 * Sets the session info.
	 * 
	 * @param sessionInfo the new session info
	 */
	public void setSessionInfo(IGozerSessionInfo sessionInfo)
	{
		_sessionInfo = sessionInfo;
	}
}
