package com.swordlord.jalapeno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.ObjEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;


public class PredefinedListHelper
{
	public static final String APPLICATION_TABLES = "applicationTables";
	public static final String ATTRIBUTE_TYPE = "attributeType";

	protected static final Log LOG = LogFactory.getLog(PredefinedListHelper.class);

	private static PredefinedListHelper _instance;
	private Hashtable<String, List<String>> predefinedList;

	public static PredefinedListHelper instance()
	{
		if (_instance == null)
		{
			_instance = new PredefinedListHelper();
			_instance.predefinedList = new Hashtable<String, List<String>>();
		}

		return _instance;
	}

	public List<String> getPredefinedList(String listName)
	{

		if (predefinedList.containsKey(listName))
			return predefinedList.get(listName);
		else
		{
			if (listName.equals(APPLICATION_TABLES))
			{
				DataContext dc = DataContext.createDataContext(DBConnection.DATA_DOMAIN_REPOSITORY);
				DataMap dataMap = dc.getEntityResolver().getDataMap(DBConnection.DATA_MAP_REPOSITORY);
				List<String> list = new ArrayList<String>();
				for (ObjEntity objEntity : dataMap.getObjEntities())
				{
					list.add(objEntity.getName());
				}

				Collections.sort(list);
				predefinedList.put(APPLICATION_TABLES, list);
			} else if (listName.equals(ATTRIBUTE_TYPE))
			{
				List<String> list = new ArrayList<String>();
				list.add("java.util.Date");
				list.add("java.lang.String");
				list.add("java.lang.Float");

				Collections.sort(list);
				predefinedList.put(ATTRIBUTE_TYPE, list);
			}
			return predefinedList.get(listName);
		}

	}

}
