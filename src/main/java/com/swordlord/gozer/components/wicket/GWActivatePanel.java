/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 ** and individual authors
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
 ** $Id:  $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket;

import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.wicket.action.button.list.GWActivateButton;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWActivatePanel extends GWPanel
{
	public GWActivatePanel(String id, IModel<?> model, DataBinding dataBinding, GActionBase actionBase, IGozerFrameExtension fe)
	{
		super(id, model);
        add(new GWActivateButton("action", fe.getGozerController(), actionBase, getRow().getKey()));
	}

	public DataRowBase getRow()
	{
		return (DataRowBase) getModelObject();
	}
}
