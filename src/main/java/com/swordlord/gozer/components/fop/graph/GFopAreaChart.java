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
 ** $Id: GFopAreaChart.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.fop.graph;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import com.swordlord.gozer.components.fop.GFopObjectBase;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.generic.graph.GAreaChart;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

import sun.misc.BASE64Encoder;

@SuppressWarnings("serial")
public class GFopAreaChart extends GFopObjectBase
{
	private ChartImage _image;

	public GFopAreaChart(IGozerFrameExtension gfe, GAreaChart chart)
	{
		super(gfe);

		DataBindingMember dbMemberKey = chart.getDataBindingMemberRowKey();
		DataBindingMember dbMemberValue = chart.getDataBindingMemberValue();
		DataBindingManager dbManager = chart.getDataBindingManager();

		DefaultCategoryDataset dcd = new DefaultCategoryDataset();

		List<DataRowBase> keyRows = dbManager.getRows(dbMemberKey);
		List<DataRowBase> valueRows = dbManager.getRows(dbMemberValue);

		String[] codes = new String[keyRows.size()];
		int[] results = new int[keyRows.size()];

		int i = 0;
		for (DataRowBase row : keyRows)
		{
			codes[i] = row.getPropertyAsString(dbMemberKey.getDataBindingFieldName());
			i++;
		}

		i = 0;
		for (DataRowBase row : valueRows)
		{
			results[i] = row.getPropertyAsInt(dbMemberValue.getDataBindingFieldName());
			i++;
		}

		for (Integer j = 0; j < keyRows.size(); j++)
		{
			dcd.setValue(results[j], codes[j], codes[j]);
		}

		JFreeChart fc = ChartFactory.createAreaChart(chart.getTitle(), chart.getCategoryAxisLabel(), chart.getValueAxisLabel(), dcd, chart.getOrientation(), chart.getLegend(), false, false);

		fc.setBackgroundPaint(Color.white);
		
		CategoryPlot plot = (CategoryPlot) fc.getPlot();
		plot.setDrawingSupplier(chart.getDrawingSupplier());

		_image = new ChartImage("chart", fc, chart.getWidth(400), chart.getHeight(400));
	}

	@Override
	public String toString()
	{
		try
		{
			StringBuilder sb = new StringBuilder();
	
			sb.append("<fo:block text-align=\"center\"><fo:external-graphic content-width=\"600\" src=\"data:image/png;base64,");
			
			byte[] arr = EncoderUtil.encode(_image.createBufferedImage(), ImageFormat.PNG, 1, true);
			
			sb.append(new BASE64Encoder().encode(arr));
			sb.append("\" /></fo:block>");
	
			return sb.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			return "";
		}
	}
}
