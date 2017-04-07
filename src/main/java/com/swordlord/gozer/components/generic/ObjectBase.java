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
 ** $Id: ObjectBase.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SortOrder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;

@SuppressWarnings("serial")
public abstract class ObjectBase implements Serializable
{
	public static String ATTRIBUTE_BINDING_MEMBER = "DataBinding";
	public static String ATTRIBUTE_BINDING_MEMBER_TWO = "DataBindingTwo";
	public static String ATTRIBUTE_ORIGINAL_BINDING_MEMBER = "OriginalDataBinding";
	public static String ATTRIBUTE_CAPTION = "caption";
	public static String ATTRIBUTE_FORMAT = "format";
	public static String ATTRIBUTE_PAGESIZE = "PageSize";
	public static String ATTRIBUTE_MAX_LENGTH = "MaxLength";
	public static String ATTRIBUTE_MAX_EDIT_MODE = "MaxEditMode";

	// just some generic ID which can be set so when - as example - there are multiple 
	// commands of the same type, we still can separate these buttons in code.
	public static String ATTRIBUTE_ID = "identifier";

	public static String EDIT_MODE_READ = "READ";

	public static String getObjectTag()
	{
		return "undefined";
	}

	private String _content;

	private LinkedList<ObjectBase> _children;

	private Hashtable<String, String> _attributes;

	private DataBinding _dataBinding;
	private DataBinding _dataBindingTwo;
	
	protected ObjectTree _root;
	protected ObjectBase _parent;

	public ObjectBase(ObjectTree root)
	{
		_root = root;

		_children = new LinkedList<ObjectBase>();
		_attributes = new Hashtable<String, String>();
	}

	public String getAttribute(String strAttribute)
	{
		return getAttribute(strAttribute, null);
	}
	
	public String getAttribute(String strAttribute, String strDefault)
	{
		if (!_attributes.containsKey(strAttribute))
		{
			return strDefault;
		} 
		else
		{
			return _attributes.get(strAttribute);
		}
	}

	public int getAttributeAsInt(String strAttribute, int nDefault)
	{
		if (!_attributes.containsKey(strAttribute))
		{
			return nDefault;
		} else
		{
			return Integer.parseInt(_attributes.get(strAttribute)); 
		}
	}

	public boolean getAttributeAsBoolean(String strAttribute, boolean nDefault)
	{
		if (!_attributes.containsKey(strAttribute))
		{
			return nDefault;
		} else
		{
			return new Boolean(_attributes.get(strAttribute));
		}
	}

	public Hashtable<String, String> getAttributes()
	{
		return _attributes;
	}

	public String getCaption()
	{
		return getAttribute(ATTRIBUTE_CAPTION);
	}

	public LinkedList<ObjectBase> getChildren()
	{
		return _children;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<ObjectBase> getChildrenCloned()
	{
		return (LinkedList<ObjectBase>) _children.clone();
	}

	public DataBinding getDataBinding()
	{
		if (_dataBinding == null)
		{
			_dataBinding = new DataBinding(this);
		}
		return _dataBinding;
	}
	
	public DataBinding getDataBindingTwo()
	{
		if (_dataBindingTwo == null)
		{
			_dataBindingTwo = new DataBinding(this, getDataBindingManagerTwo(), getDataBindingMemberTwo());
		}
		return _dataBindingTwo;
	}

	public DataBindingManager getDataBindingManager()
	{
		DataBindingMember dataBindingMember = getDataBindingMember();
		DataBindingContext dbc = _root.getDataBindingContext();
		return dbc.getDataBindingManager(dataBindingMember);
	}
	
	public DataBindingManager getDataBindingManagerTwo()
	{
		DataBindingMember dataBindingMember = getDataBindingMemberTwo();
		DataBindingContext dbc = _root.getDataBindingContext();
		return dbc.getDataBindingManager(dataBindingMember);
	}

	public DataBindingMember getDataBindingMember()
	{
		String strBindingMember = getAttribute(ATTRIBUTE_BINDING_MEMBER);

		DataBindingMember dbm = new DataBindingMember(strBindingMember);
		return dbm;
	}
	
	public DataBindingMember getDataBindingMemberTwo()
	{
		String strBindingMember = getAttribute(ATTRIBUTE_BINDING_MEMBER_TWO);

		DataBindingMember dbm = new DataBindingMember(strBindingMember);
		return dbm;
	}

	public String getMaxEditMode()
	{
		String maxEditMode = getAttribute(ATTRIBUTE_MAX_EDIT_MODE);
		return maxEditMode == null ? null : maxEditMode.toUpperCase();
	}

	public ObjectTree getObjectTree()
	{
		return _root;
	}

	public int getPageSize(int nDefault)
	{
		return getAttributeAsInt(ATTRIBUTE_PAGESIZE, nDefault);
	}
	
	public String getIdentifier()
	{
		return getAttribute(ATTRIBUTE_ID);
	}

	public void inheritParent(ObjectBase parent)
	{
		_parent = parent;
		
		String strDataBindingParent = parent.getAttribute(ATTRIBUTE_BINDING_MEMBER);
		if (strDataBindingParent != null)
		{
			String strBindingPath = strDataBindingParent;
			if (_attributes.containsKey(ATTRIBUTE_BINDING_MEMBER))
			{
				// Only do the inheritance if it does not stop from the root
				// again
				String strDataBindingChild = _attributes.get(ATTRIBUTE_BINDING_MEMBER);
				if (strDataBindingChild.charAt(0) == '@')
				{
					strBindingPath = strDataBindingChild;
				} else
				{
					strBindingPath += "." + strDataBindingChild;
				}
				
				// back up original databinding before overwriting with complete hierarchy
				putAttribute(ATTRIBUTE_ORIGINAL_BINDING_MEMBER, strDataBindingChild);
			}

			putAttribute(ATTRIBUTE_BINDING_MEMBER, strBindingPath);

			// TODO: consistency check if databinding is correct (syntactically)

			strBindingPath = strDataBindingParent;
			if (_attributes.containsKey("LookupBinding"))
			{
				strBindingPath += "." + _attributes.get("LookupBinding");
			}

			putAttribute("LookupBinding", strBindingPath);

			// TODO: consistency check if lookupbinding is correct
			// (syntactically)
		}
	}

	public boolean isReadOnly()
	{
		String strMaxEditMode = getAttribute(ATTRIBUTE_MAX_EDIT_MODE);
		return (strMaxEditMode != null) && (strMaxEditMode.compareToIgnoreCase(EDIT_MODE_READ) == 0);
	}

	public void putAttribute(String strAttribute, String strValue)
	{
		_attributes.put(strAttribute, strValue);
	}

	public void putChild(ObjectBase ob)
	{
		_children.add(ob);
	}
	
	public boolean hasParent()
	{
		return _parent != null;
	}
	
	public ObjectBase getParent()
	{
		return _parent;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(this.getClass().getSimpleName().toString() + "\n");

		for (String key : _attributes.keySet())
		{
			sb.append(MessageFormat.format("Key: {0}, Value: {1}\n", key, _attributes.get(key)));
		}

		for (ObjectBase ob : _children)
		{
			sb.append(ob.toString());
		}

		return sb.toString();
	}

	public String getContent()
	{
		return _content;
	}

	public void setContent(String content)
	{
		this._content = content;
	}
	
	public List<Ordering> formatOrdering(String strOrdering)
	{
		ArrayList<String> alOrderings = new ArrayList<String>();
		
		if(strOrdering.contains(","))
		{
			alOrderings.addAll(Arrays.asList(strOrdering.split(",")));			
		}
		else
		{
			alOrderings.add(strOrdering);
		}
		
		List<Ordering> orderings = new ArrayList<Ordering>();

		for(String ordering : alOrderings)
		{
			ordering = ordering.trim();
			
			SortOrder sortOrder = SortOrder.ASCENDING; // default
			String strField = "";
			
			// which means there is some SortOrder definition in there
			if(ordering.contains(" "))
			{
				String[] elements = ordering.split(" ");
				strField = elements[0].trim();
				
				sortOrder = SortOrder.valueOf(elements[1].trim());
			}
			else
			{
				strField = ordering;				
			}
			
			orderings.add(new Ordering(strField, sortOrder));			
		}

		return orderings;
	}
}
