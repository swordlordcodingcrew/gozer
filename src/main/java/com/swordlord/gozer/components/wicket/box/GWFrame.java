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
 ** $Id: GWFrame.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.box;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.sobf.wicket.tools.SecureForm;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

/**
 * Implements root element 'frame' in gozer templates. Supports GListAndDetail
 * and GActionBox.
 */
@SuppressWarnings("serial")
public class GWFrame extends GWAbstractBox
{
    /**
     * @param id
     * @param model
     * @param gozerForm
     */
	public GWFrame(String id, final IModel<?> model, ObjectBase gozerForm)
	{
		super(id, model);

		GWContext context = getGWContext();
		context.setFormPanel(this);
			

		final SecureForm<Object> form = new SecureForm<Object>("gozerForm");
		add(form);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		form.add(feedback);
		// filteredErrorLevels will not be shown in the FeedbackPanel
        // int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
		// feedback.setFilter(new ErrorLevelFeedbackMessageFilter(filteredErrorLevels));

		ObjectBase formRoot = gozerForm;
		
		ListView<ObjectBase> listView = new ListView<ObjectBase>("eachGuiElem", formRoot.getChildren())
		{
			@Override
			protected void populateItem(ListItem<ObjectBase> item)
			{
				ObjectBase ob = item.getModelObject();
				
				item.add(getPanelFromObjectBase("cell", ob, model, form));
			}
		};

		listView.setReuseItems(true);

		if (formRoot.getChildren().size() == 0)
		{
			listView.setVisible(false);
		}

		form.add(listView);
	}
}
