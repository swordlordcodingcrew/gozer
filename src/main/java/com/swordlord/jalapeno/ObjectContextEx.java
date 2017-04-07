package com.swordlord.jalapeno;

import org.apache.cayenne.ObjectContext;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.swordlord.jalapeno.datacontainer.DataContainer;


public abstract interface ObjectContextEx extends ObjectContext
{
	public abstract DataContainer getDataContainer();
	
    public abstract DataTable<?, String> getDataTable();
	
}
