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
 ** $Id: GWVBoxPanel.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.box;

import java.util.LinkedList;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GVBox;

@SuppressWarnings("serial")
public class GWVBoxPanel extends GWAbstractBox
{
	public GWVBoxPanel(String id, final IModel<?> model, GVBox detail, final Form<?> form)
	{
		super(id, model);
		
		LinkedList<ObjectBase> childs = detail.getChildrenCloned();
		
		ListView<ObjectBase> listView = new ListView<ObjectBase>("eachGuiElem", childs)
		{
			@Override
			protected void populateItem(ListItem<ObjectBase> item)
			{
				ObjectBase ob = item.getModelObject();
				
				item.add(getPanelFromObjectBase("cell", ob, model, form));
			}
		};

		listView.setReuseItems(true);
		add(listView);
	}

	public void updateComponents()
	{
		//this.renderComponent();
	}
}
