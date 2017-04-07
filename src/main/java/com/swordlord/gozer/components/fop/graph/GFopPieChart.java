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
 ** $Id: GFopPieChart.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.fop.graph;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import com.swordlord.gozer.components.fop.GFopObjectBase;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.generic.graph.GPieChart;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

import sun.misc.BASE64Encoder;

@SuppressWarnings("serial")
public class GFopPieChart extends GFopObjectBase
{
	private ChartImage _image;

	public GFopPieChart(IGozerFrameExtension gfe, GPieChart child)
	{
		super(gfe);

		DataBindingMember dbMemberRowKey = child.getDataBindingMemberRowKey();
		DataBindingMember dbMemberValue = child.getDataBindingMemberValue();
		DataBindingManager dbManager = child.getDataBindingManager();

		DefaultPieDataset dpd = new DefaultPieDataset();

		List<DataRowBase> rowKeys = dbManager.getRows(dbMemberRowKey);
		List<DataRowBase> values = dbManager.getRows(dbMemberValue);

		String[] codes = new String[rowKeys.size()];
		int[] results = new int[rowKeys.size()];

		int i = 0;
		for (DataRowBase row : rowKeys)
		{
			codes[i] = row.getPropertyAsString(dbMemberRowKey.getDataBindingFieldName());
			i++;
		}

		i = 0;
		for (DataRowBase row : values)
		{
			results[i] = row.getPropertyAsInt(dbMemberValue.getDataBindingFieldName());
			i++;
		}

		for (Integer j = 0; j < rowKeys.size(); j++)
		{
			dpd.setValue(MessageFormat.format("{0}: {1}", codes[j], results[j]), results[j]);
		}

		JFreeChart chart = ChartFactory.createPieChart(child.getTitle(), dpd, child.getLegend(), false, false);

		chart.setBackgroundPaint(Color.white);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setDrawingSupplier(child.getDrawingSupplier());

		_image = new ChartImage("chart", chart, child.getWidth(800), child.getHeight(800));
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
