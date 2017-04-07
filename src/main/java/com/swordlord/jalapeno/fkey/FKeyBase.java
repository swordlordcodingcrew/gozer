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
** $Id: FKeyBase.java 1314 2011-12-19 17:27:08Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.fkey;

import java.io.Serializable;

import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;

/**
 *
 * @since 2
 * @author LordEidi
 */
@SuppressWarnings("serial")
public abstract class FKeyBase implements Serializable
{
	// Empty constructor
    public FKeyBase() { }

    // Field which is displayed
    protected abstract String getDisplayMember();
    
    protected abstract String getDataTableId();
    
	public DataBinding getDisplayDataBinding(ObjEntity objEntity, ObjectBase objBase, DataBindingManager dataBindingManager)
	{
        return new DataBinding(objBase, dataBindingManager, getDisplayDataBindingMember(objEntity, objBase));
	}

    /**
     * Returns the {@link DataBindingMember} referencing this FK attributes
     * display property.
     * 
     * @param objEntity
     *            The entity owning this FK
     * @param objBase
     *            A gozer node referencing this FK (the one for which this
     *            binding should be resolved)
     * @return The binding definition or null if it could not be resolved
     */
    public DataBindingMember getDisplayDataBindingMember(ObjEntity objEntity, ObjectBase objBase)
    {
        // NOT objBase.getDataBindingMember().getDataBindingTableName();, which is SOURCE
        final String strTargetName = getDataTableId();
		for (ObjRelationship rel: objEntity.getRelationships())
		{
            if (rel.getTargetEntityName().equals(strTargetName))
			{
                final String path = objBase.getDataBindingMember().getDataBindingMemberWOField() + "." + rel.getName() + "[0]." + getDisplayMember();
                return new DataBindingMember(path);
			}
		}
        return null;
    }


    // Getting FrameExtension and .Gozer file indirectly
    public abstract IGozerFrameExtension getFrameExtension(IGozerSessionInfo session);
}
