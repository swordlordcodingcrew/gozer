package com.swordlord.jalapeno.access;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.ObjectStore;
import com.swordlord.jalapeno.datacontainer.DataContainer;

/**
 *
 */
public class DataContextEx extends DataContext
{

	private DataContainer _dc;
	
	public DataContextEx(DataDomain dataDomain, ObjectStore objectStore, DataContainer dc)
	{
		super(dataDomain, objectStore);
		this._dc = dc;
	}

	public DataContainer getDataContainer()
	{
		return _dc;
	}

	
}
