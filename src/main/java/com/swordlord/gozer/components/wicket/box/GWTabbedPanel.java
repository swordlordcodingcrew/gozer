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
 ** $Id: GWTabbedPanel.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.box;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GTabbedPanel;

/**
 * Implements gozer element 'tabbed_panel'. Supports ....
 */
@SuppressWarnings("serial")
public class GWTabbedPanel extends GWAbstractBox
{
	public GWTabbedPanel(String id, final IModel<?> model, GTabbedPanel gfTabbedPanel, final Form<?> form)
	{
		super(id, model);

		List<ITab> tabs = new ArrayList<ITab>();
		
		LinkedList<ObjectBase> children = gfTabbedPanel.getChildren();
		for (final ObjectBase child : children)
		{
			tabs.add(new AbstractTab(new Model<String>(child.getCaption()))
			{
				@Override
				public Panel getPanel(String panelId)
				{
					return getPanelFromObjectBase(panelId, child, model, form);
				}
			});
		}

		add(new AjaxTabbedPanel("tabs", tabs));
	}
}
