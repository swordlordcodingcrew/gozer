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
 ** $Id: GFopStackedBarChart.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.fop.graph;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import com.swordlord.gozer.components.fop.GFopObjectBase;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.generic.graph.GStackedBarChart;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

import sun.misc.BASE64Encoder;

@SuppressWarnings("serial")
public class GFopStackedBarChart extends GFopObjectBase
{
	private ChartImage _image;

	public GFopStackedBarChart(IGozerFrameExtension gfe, GStackedBarChart chart)
	{
		super(gfe);

		DataBindingMember dbMemberRowKey = chart.getDataBindingMemberRowKey();
		DataBindingMember dbMemberColKey = chart.getDataBindingMemberColKey();
		DataBindingMember dbMemberValue = chart.getDataBindingMemberValue();
		DataBindingManager dbManager = chart.getDataBindingManager();

		DefaultCategoryDataset dcd = new DefaultCategoryDataset();

		List<DataRowBase> rowTests = dbManager.getRows(dbMemberValue);
		for (int j = 0; j < rowTests.size(); j++)
		{
			DataRowBase row = rowTests.get(j);
			
			String strKey = row.getPropertyAsStringForce(dbMemberRowKey.getRelativePathWithField());
			
			dcd.setValue(row.getPropertyAsInt(dbMemberValue.getRelativePathWithField()), 
					strKey, 
					row.getPropertyAsStringForce(dbMemberColKey.getRelativePathWithField()));
		}

		JFreeChart fc = ChartFactory.createStackedBarChart(chart.getTitle(), chart.getCategoryAxisLabel(), chart.getValueAxisLabel(), dcd, chart.getOrientation(), chart.getLegend(), false, false);
		
		// Do this in a more static way!
		StackedBarRenderer.setDefaultBarPainter(new StandardBarPainter());
		
		//chart.setBackgroundPaint(Color.white);
		if (chart.getSubTitle() != null)
		{
			fc.addSubtitle(new TextTitle(chart.getSubTitle()));
		}

		CategoryPlot plot = fc.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDrawingSupplier(chart.getDrawingSupplier());

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setVisible(chart.getCategoryAxisVisible());

		ValueAxis valueAxis = plot.getRangeAxis();
		valueAxis.setVisible(chart.getValueAxisVisible());
		
		/*
		//CategoryItemRenderer renderer = (CategoryItemRenderer) plot.getRenderer();
		for (int j = 0; j < rowKey.length; j++)
		{
			renderer.setSeriesItemLabelGenerator(j, new LabelGenerator(j, rowKey[j]));
			renderer.setSeriesItemLabelsVisible(j, true);
		}
		*/
		StackedBarRenderer renderer = new StackedBarRenderer();
		
		for (int j = 0; j < dcd.getRowCount(); j++)
		{
			renderer.setSeriesItemLabelGenerator(j, new StandardCategoryItemLabelGenerator());
			renderer.setSeriesItemLabelsVisible(j, true);
		}
		
		//renderer.setLegendItemLabelGenerator(new LabelGenerator());
		renderer.setShadowVisible(false);
		
		plot.setRenderer(renderer);

		_image = new ChartImage("chart", fc, chart.getWidth(800), chart.getHeight(800));
	}

	@Override
	public String toString()
	{
		try
		{
			StringBuilder sb = new StringBuilder();
	
			sb.append("<fo:block text-align=\"center\"><fo:external-graphic content-width=\"200\" src=\"data:image/png;base64,");
			
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
