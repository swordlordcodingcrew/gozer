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
 ** $Id: GLinkField.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.field;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.databinding.FrameDataBindingContext;

@SuppressWarnings("serial")
public class GLinkField extends GField
{
	public static String ATTRIBUTE_BINDING_LINK = "DataBindingLink";
	private DataBinding _dataBindingLink;

	public static String getObjectTag()
	{
		return "linkfield";
	}

	public GLinkField(ObjectTree root)
	{
		super(root);
	}

	public DataBinding getDataBindingLink()
	{
		if (_dataBindingLink == null)
		{
			String strBindingMember = getAttribute(ATTRIBUTE_BINDING_LINK);
			DataBindingMember dataBindingMember = new DataBindingMember(strBindingMember);

			FrameDataBindingContext dbc = (FrameDataBindingContext) _root.getDataBindingContext();
			DataBindingManager dataBindingManager = dbc.getDataBindingManager(dataBindingMember);

			_dataBindingLink = new DataBinding(this, dataBindingManager, dataBindingMember);
		}
		return _dataBindingLink;
	}

	@Override
	public void inheritParent(ObjectBase parent)
	{
		super.inheritParent(parent);

		final String attrBindingLink = getAttribute(ATTRIBUTE_BINDING_LINK);

		String strDataBindingParent = parent.getAttribute(ATTRIBUTE_BINDING_MEMBER);

		if (strDataBindingParent != null)
		{
			if (attrBindingLink.charAt(0) == '@')
			{
				putAttribute(ATTRIBUTE_BINDING_LINK, attrBindingLink);
			} else
			{
				strDataBindingParent += "." + attrBindingLink;
				putAttribute(ATTRIBUTE_BINDING_LINK, strDataBindingParent);

			}

		}

	}
}
