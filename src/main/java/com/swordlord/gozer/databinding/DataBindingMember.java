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
** $Id: DataBindingMember.java 1295 2011-12-15 15:41:02Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.databinding;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.swordlord.jalapeno.DBConnection;


@SuppressWarnings("serial")
public class DataBindingMember implements Serializable
{
	protected static final Log LOG = LogFactory.getLog(DataBindingMember.class);

	protected LinkedList<DataBindingElement> _dataBindingElements;
	protected String _strDataBindingMember;
	protected String _strBindingPathName;
	protected String _strTableName;

	public DataBindingMember(String strDataMember)
	{
		_strDataBindingMember = strDataMember;

		_dataBindingElements = new LinkedList<DataBindingElement>();

		parseBindingMember();
	}
	
	public String getDataBindingMemberWOField() 
	{
		int position = _strDataBindingMember.lastIndexOf(".");
		return _strDataBindingMember.substring(0, position >= 0 ? position : _strDataBindingMember.length());
	}
	
	public LinkedList<DataBindingElement> getDataBindingElements()
	{
		return _dataBindingElements;
	}

	public String getDataBindingFieldName()
	{
		if(_dataBindingElements.size() == 0) return null;
		
		DataBindingElement element = _dataBindingElements.getLast();

		if(element.isField())
		{
			return element.getPathElement();
		}
		else
		{
			return "";
		}
	}

	public String getDataBindingPathName()
	{
		if(_strBindingPathName == null)
		{
			_strBindingPathName = "";

			for(DataBindingElement element : _dataBindingElements)
			{
				if(!element.isField())
				{
					if(_strBindingPathName.length() > 0)
					{
						_strBindingPathName += ".";
					}

					_strBindingPathName += element.getPathElement();
				}
			}
		}

		return _strBindingPathName;
	}
	
	public String getDataBindingTableName()
	{
		if(_strTableName == null) 
		{
			_strTableName =  resolveTableName(getDataBindingElements(), getDataBindingRootTableName());
		}
		
		return _strTableName;
	}
	
	private String resolveTableName(LinkedList<DataBindingElement> elements, String strTableName)
	{
		// if there are no elements or only one, then this element must be the table name
		if((elements == null) || (elements.size() <= 1))
		{
			return strTableName;
		}

		// otherwise walk the references to find the table
		
		ObjEntity parentTable = DBConnection.instance().getObjectContext().getEntityResolver().getObjEntity(strTableName);
		
		for(DataBindingElement element : elements)
		{
			if(!element.isField())
			{
				String strPathElement = element.getPathElement();
				LOG.debug("TableName/PatheElement=" + strTableName + "/" + strPathElement);

				// ignore the same level
				if(strPathElement.compareToIgnoreCase(strTableName) != 0)
				{
					ObjRelationship rel = (ObjRelationship) parentTable.getRelationship(strPathElement);
					parentTable = (ObjEntity) rel.getTargetEntity();
				}
			}
		}

		return parentTable.getName();
	}
	

	public String getDataBindingRootTableName()
	{
		if(_dataBindingElements.size() > 0)
		{
			DataBindingElement element = _dataBindingElements.getFirst();
			return element.getPathElement();
		}
		else
		{
			return "";
		}
	}

	public String getRelativePathWithField()
	{
		String strPath = getDataBindingPathName();
		if((strPath != null) && (strPath.length() > 0))
		{
			strPath += ".";

			String strTableName = getDataBindingRootTableName() + ".";
			strPath = strPath.replace(strTableName, "");
		}

		strPath += getDataBindingFieldName();

		return strPath;
	}

	// @Table{Filter}[Position].Reference{Filter}[Position].Field
	private void parseBindingMember()
	{
		// probably throw error in this case? -> no. DataBinding can be NULL!
		if(_strDataBindingMember == null)
		{
			return;
		}

		Pattern pattern = Pattern.compile("^(@)?([a-z_0-9]*)(\\{[(\\?)?a-z_0-9,:='%\\s@\\[\\]\\(\\)]*\\})?(\\[[0-9]*\\])?$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

		String[] bmElements = _strDataBindingMember.split("\\.");
		if(bmElements.length == 0)
		{
			LOG.error("Split of binding member return 0 elements: " + _strDataBindingMember);
			return;
		}

		// reset this, is no problem since works with lazy initialisation
		_strBindingPathName = null;
		int iElement = 0;

		for(String strBindingMemberElement : bmElements)
		{
			Matcher matcher = pattern.matcher(strBindingMemberElement);

			if(!matcher.find())
			{
				LOG.error("DataBinding parser can't parse binding member: " + strBindingMemberElement);
				return;
			}

			if(matcher.groupCount() != 4)
			{
				LOG.error("Wrong group count during parsing of binding member element: " + strBindingMemberElement + " in binding member: " + _strDataBindingMember);
				return;
			}

			DataBindingElement element = new DataBindingElement();

			element.setPathElement(matcher.group(2));

			String strFilter = matcher.group(3);
			if((strFilter != null) && (strFilter.length() > 2))
			{
				try
				{
					element.setFilter(strFilter.substring(1, strFilter.length() - 1));
				}
				catch(IndexOutOfBoundsException e)
				{
					LOG.error("strFilter is defect: " + strFilter);
					LOG.debug(e.getMessage());
					return;
				}
			}

			String strRowNumber = matcher.group(4);
			if((strRowNumber != null) && (strRowNumber.length() > 2))
			{
				try
				{
					element.setRowNumber(Integer.parseInt(strRowNumber.substring(1, strRowNumber.length() - 1)));
				}
				catch(NumberFormatException e)
				{
					LOG.error("RowNumber is not of type integer: " + strRowNumber);
					LOG.debug(e.getMessage());
					return;
				}
				catch(IndexOutOfBoundsException e)
				{
					LOG.error("RowNumber is defect: " + strRowNumber);
					LOG.debug(e.getMessage());
					return;
				}
			}
			else
			{
				if(iElement < bmElements.length - 1)
				{
					// wrong syntax, add 0 on our own
					element.setRowNumber(0);
					//element.isField(true);
				}
				else
				{
					element.setField(true);
				}
			}

			_dataBindingElements.add(element);

			iElement++;
		}
	}

	@Override
	public String toString()
	{
		return _strDataBindingMember;
	}
}
