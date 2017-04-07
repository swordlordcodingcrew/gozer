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
 ** $Id: GStackedBarChart.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.graph;

import org.jfree.chart.plot.PlotOrientation;
import com.swordlord.gozer.builder.ObjectTree;

@SuppressWarnings("serial")
public class GStackedBarChart extends GGraph
{
	private static String ATTRIBUTE_CATEGORY_AXIS_LABEL = "categoryAxisLabel";
	private static String ATTRIBUTE_VALUE_AXIS_LABEL = "valueAxisLabel";
	private static String ATTRIBUTE_ORIENTATION = "orientation";
	private static String ATTRIBUTE_CATEGORY_AXIS_VISIBLE = "categoryAxisVisible";
	private static String ATTRIBUTE_VALUEAXIS_VISIBLE = "valueAxisVisible";


	public static String getObjectTag()
	{
		return "graph_stacked_bar_chart";
	}

	public GStackedBarChart(ObjectTree root)
	{
		super(root);
	}

	/**
	 * The label for the category axis (null permitted).
	 * 
	 * @return the label for the category axis.
	 */
	public String getCategoryAxisLabel()
	{
		return getAttribute(ATTRIBUTE_CATEGORY_AXIS_LABEL);
	}

	/**
	 * the label for the value axis (null permitted).
	 * 
	 * @return the label for the value axis.
	 */
	public String getValueAxisLabel()
	{
		return getAttribute(ATTRIBUTE_VALUE_AXIS_LABEL);
	}

	/**
	 * The plot orientation (null not permitted).
	 * 
	 * @return the plot orientation.
	 */
	public PlotOrientation getOrientation()
	{
		return this.getAttributeAsPlotOrientation(ATTRIBUTE_ORIENTATION, PlotOrientation.HORIZONTAL);
	}
	
	public boolean getCategoryAxisVisible()
	{
		return this.getAttributeAsBoolean(ATTRIBUTE_CATEGORY_AXIS_VISIBLE, false);
	}
	
	public boolean getValueAxisVisible()
	{
		return this.getAttributeAsBoolean(ATTRIBUTE_VALUEAXIS_VISIBLE, false);
	}
}
