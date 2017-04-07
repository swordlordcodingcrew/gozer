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
 ** $Id: GWChartPanel.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.graph.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.wicket.GWPanel;

@SuppressWarnings("serial")
public abstract class GWChartPanel extends GWPanel
{
	
	public GWChartPanel(String id, IModel<?> model)
	{
		super(id, model);
	}

	/**
	 * The callback method that is called when a specific image map entity is
	 * clicked on.
	 * 
	 * @param target
	 * @param entity
	 */
	protected abstract void onClickCallback(AjaxRequestTarget target, ChartEntity entity);

	protected DynamicImageMap constructImageMap(ChartImage image, String mapName)
	{
		DynamicImageMap imageMap = new DynamicImageMap("imageMap", mapName);
		EntityCollection entities = image.getRenderingInfo().getEntityCollection();
		if (entities != null)
		{
			int count = entities.getEntityCount();
			for (int i = count - 1; i >= 0; i--)
			{
				final ChartEntity entity = entities.getEntity(i);
				imageMap.addArea(entity.getShapeType(), entity.getShapeCoords(), entity.getToolTipText(), new AjaxLink<Object>("link")
				{
					private static final long serialVersionUID = -7982198051678987986L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						onClickCallback(target, entity);
					}
				});
			}
		}
		return imageMap;
	}

}
