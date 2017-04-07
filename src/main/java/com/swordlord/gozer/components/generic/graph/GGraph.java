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
 ** $Id: GGraph.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.generic.graph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;

@SuppressWarnings("serial")
public class GGraph extends ObjectBase
{
	private static Log _logger = LogFactory.getLog(GGraph.class);

	public static String ATTRIBUTE_BINDING_MEMBER_ROWKEY = "DataBinding_rowkey";
	public static String ATTRIBUTE_BINDING_MEMBER_TARGETID = "DataBinding_targetid";
	public static String ATTRIBUTE_BINDING_MEMBER_VALUE = "DataBinding_value";
	public static String ATTRIBUTE_BINDING_MEMBER_COLKEY = "DataBinding_colkey";
	public static String ATTRIBUTE_WIDTH = "width";
	public static String ATTRIBUTE_HEIGHT = "height";
	public static String ATTRIBUTE_TITLE = "title";
	public static String ATTRIBUTE_SUBTITLE = "subtitle";
	public static String ATTRIBUTE_LEGEND = "legend";
	private static String ATTRIBUTE_CLICKABLE = "clickable";
	private static String ATTRIBUTE_TARGET_CHART = "target_chart";
	public static String ATTRIBUTE_ORDERING = "ordering";
	
	
	public static String getObjectTag()
	{
		return "graph";
	}

	public GGraph(ObjectTree root)
	{
		super(root);
	}
	
	public String getOrdering()
	{
		return getAttribute(ATTRIBUTE_ORDERING);
	}
	
	public boolean hasOrdering()
	{
		return getAttribute(ATTRIBUTE_ORDERING) != null;
	}

	// TODO: make the drawing supplier configurable from the .gozer file
	public DefaultDrawingSupplier getDrawingSupplier()
	{
		return new BLAFDrawingSupplier();
	}

	public int getWidth(int nDefault)
	{
		return getAttributeAsInt(ATTRIBUTE_WIDTH, nDefault);
	}

	public int getHeight(int nDefault)
	{
		return getAttributeAsInt(ATTRIBUTE_HEIGHT, nDefault);
	}

	public PlotOrientation getAttributeAsPlotOrientation(String strAttribute, PlotOrientation nDefault)
	{
		String attributes = getAttribute(strAttribute);
		if (attributes == null)
		{
			return nDefault;
		} else
		{
			if (attributes.equals("horizontal"))
			{
				return PlotOrientation.HORIZONTAL;
			} else if (attributes.equals("vertical"))
			{
				return PlotOrientation.VERTICAL;
			} else
			{
				_logger.error("PlotOrientation not allowed, only the following values are allowed : horizontal, vertical");
				return null;
			}
		}
	}

	public DataBindingMember getDataBindingMemberRowKey()
	{
		String strBindingMember = getAttribute(ATTRIBUTE_BINDING_MEMBER_ROWKEY);

		DataBindingMember dbm = new DataBindingMember(strBindingMember);
		return dbm;
	}

	public DataBindingMember getDataBindingMemberTargetId()
	{
		String strBindingMember = getAttribute(ATTRIBUTE_BINDING_MEMBER_TARGETID);

		if (strBindingMember == null)
		{
			return null;
		} else
		{
			DataBindingMember dbm = new DataBindingMember(strBindingMember);
			return dbm;
		}
	}

	public DataBindingMember getDataBindingMemberColKey()
	{
		String strBindingMember = getAttribute(ATTRIBUTE_BINDING_MEMBER_COLKEY);

		DataBindingMember dbm = new DataBindingMember(strBindingMember);
		return dbm;
	}

	public DataBindingMember getDataBindingMemberValue()
	{
		String strBindingMember = getAttribute(ATTRIBUTE_BINDING_MEMBER_VALUE);

		DataBindingMember dbm = new DataBindingMember(strBindingMember);
		return dbm;
	}

	public DataBindingManager getDataBindingManager()
	{
		DataBindingMember dataBindingMember = getDataBindingMemberRowKey();
		DataBindingContext dbc = _root.getDataBindingContext();
		return dbc.getDataBindingManager(dataBindingMember);
	}

	/**
	 * The chart title (null permitted).
	 * 
	 * @return the chart title.
	 */
	public String getTitle()
	{
		return getAttribute(ATTRIBUTE_TITLE);
	}

	/**
	 * The chart sub-title (null permitted).
	 * 
	 * @return the chart sub-title.
	 */
	public String getSubTitle()
	{
		return getAttribute(ATTRIBUTE_SUBTITLE);
	}

	/**
	 * A flag specifying whether or not a legend is required.
	 * 
	 * @return a flag specifying whether or not a legend is required
	 */
	public boolean getLegend()
	{
		return getAttributeAsBoolean(ATTRIBUTE_LEGEND, true);
	}

	/**
	 * Is the chart clickable ? If yes, then you can call a target chart
	 * 
	 * @return
	 */
	public boolean isClickable()
	{
		return this.getAttributeAsBoolean(ATTRIBUTE_CLICKABLE, false);
	}

	public String getTargetChart()
	{
		return getAttribute(ATTRIBUTE_TARGET_CHART);
	}

}
