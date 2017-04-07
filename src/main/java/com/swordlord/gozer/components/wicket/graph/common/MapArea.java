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
 ** $Id: MapArea.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.graph.common;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.IAjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * A mapped area segment of an image map that adds an Ajax link to the area as
 * well as a regular tooltip. Based on source by Jonny Wray.
 * 
 * @author LordEidi
 * 
 */
public class MapArea extends WebMarkupContainer
{
	private static final long serialVersionUID = -135521429660733572L;

	private String shape;
	private String coords;
	private String tooltipText;

	/**
	 * Construct the map area
	 * 
	 * @param id
	 *            Component identifier
	 * @param model
	 *            Model
	 * @param shape
	 *            The specific area shape
	 * @param coords
	 *            The coordinates of the area as a comma separated list
	 * @param tooltipText
	 *            The tooltip text, or null to not include it
	 * @param linkCallback
	 *            The link callback function called when the area is click, or
	 *            null to have no link functionality
	 */
	public MapArea(String id, IModel<?> model, String shape, String coords, String tooltipText, final IAjaxLink linkCallback)
	{
		super(id, model);
		this.shape = shape;
		this.coords = coords;
		this.tooltipText = tooltipText;
		if (linkCallback != null)
		{
			add(new AjaxEventBehavior("onclick")
			{
				private static final long serialVersionUID = 2615093257359874075L;

				@Override
				protected void onEvent(AjaxRequestTarget target)
				{
					linkCallback.onClick(target);
				}

                /*
                 * protected IAjaxCallDecorator getAjaxCallDecorator() { return
                 * new CancelEventIfNoAjaxDecorator(); }
                 */
			});
		}
		setOutputMarkupId(true);
	}

	/**
	 * Construct the map area
	 * 
	 * @param id
	 *            Component identifier
	 * @param shape
	 *            The specific area shape
	 * @param coords
	 *            The coordinates of the area as a comma separated list
	 * @param tooltipText
	 *            The tooltip text, or null to not include it
	 * @param linkCallback
	 *            The link callback function called when the area is click, or
	 *            null to have no link functionality
	 */
	public MapArea(String id, String shape, String coords, String tooltipText, final IAjaxLink linkCallback)
	{
		this(id, null, shape, coords, tooltipText, linkCallback);
	}

	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		assert tag.getName().equals("area");
		tag.put("shape", shape);
		tag.put("coords", coords);
		tag.put("href", "#");
		if (tooltipText != null && !tooltipText.isEmpty())
		{
			tag.put("title", tooltipText);
		}
	}
}
