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
 ** $Id: GWStackedBarChartPanel.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.graph;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import org.apache.cayenne.query.Ordering;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.generic.graph.GStackedBarChart;
import com.swordlord.gozer.components.wicket.graph.common.DynamicImageMap;
import com.swordlord.gozer.components.wicket.graph.common.GWChartPanel;
import com.swordlord.gozer.components.wicket.report.GWReport;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.eventhandler.generic.ReportController;
import com.swordlord.jalapeno.OrderingEx;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWStackedBarChartPanel extends GWChartPanel
{
	private int value;
	private GStackedBarChart gchart;
	private HashMap<String, String> _target = new HashMap<String, String>();

	public GWStackedBarChartPanel(String id, IModel<?> model, GStackedBarChart child)
	{
		super(id, model);

		gchart = child;
		DataBindingMember dbMemberRowKey = child.getDataBindingMemberRowKey();
		DataBindingMember dbMemberTargetId = child.getDataBindingMemberTargetId();
		DataBindingMember dbMemberColKey = child.getDataBindingMemberColKey();
		DataBindingMember dbMemberValue = child.getDataBindingMemberValue();
		DataBindingManager dbManager = child.getDataBindingManager();

		DefaultCategoryDataset dcd = new DefaultCategoryDataset();

		List<DataRowBase> rows = dbManager.getRows(dbMemberValue);
		
		// if the graph has some ordering info in the format of "<field> ASCENDING,<field2> DESCENDING"
		if(child.hasOrdering())
		{
			List<Ordering> ordering = child.formatOrdering(child.getOrdering());
			OrderingEx.orderList(rows, ordering);
		}
		
		for (int j = 0; j < rows.size(); j++)
		{
			DataRowBase row = rows.get(j);
			
			String strKey = row.getPropertyAsStringForce(dbMemberRowKey.getRelativePathWithField());
			
			dcd.setValue(row.getPropertyAsInt(dbMemberValue.getRelativePathWithField()), 
					strKey, 
					row.getPropertyAsStringForce(dbMemberColKey.getRelativePathWithField()));
			
			if (dbMemberTargetId != null)
			{
				_target.put(strKey, row.getPropertyAsStringForce(dbMemberTargetId.getDataBindingFieldName()));
			}
		}

		JFreeChart chart = ChartFactory.createStackedBarChart(child.getTitle(), child.getCategoryAxisLabel(), child.getValueAxisLabel(), dcd, child.getOrientation(), child.getLegend(), false, false);
		
		// Do this in a more static way!
		StackedBarRenderer.setDefaultBarPainter(new StandardBarPainter());
		
		//chart.setBackgroundPaint(Color.white);
		if (child.getSubTitle() != null)
		{
			chart.addSubtitle(new TextTitle(child.getSubTitle()));
		}

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDrawingSupplier(child.getDrawingSupplier());

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setVisible(child.getCategoryAxisVisible());

		ValueAxis valueAxis = plot.getRangeAxis();
		valueAxis.setVisible(child.getValueAxisVisible());
		
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

		ChartImage image = new ChartImage("chart", chart, child.getWidth(800), child.getHeight(800));
		String mapName = child.getCaption();
		add(image);

		DynamicImageMap imageMap = constructImageMap(image, mapName);
		if (!child.isClickable())
		{
			imageMap.setVisible(false);
		}
		else
		{
            image.add(new AttributeModifier("usemap", new Model<String>("#" + mapName)));
		}
		add(imageMap);
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	@Override
	protected void onClickCallback(AjaxRequestTarget target, ChartEntity entity)
	{
		if (gchart.isClickable())
		{
			String subTitle = (String) ((CategoryItemEntity) entity).getRowKey();
			String key = _target.get(subTitle);
			
			MarkupContainer parent = this;
			while (!(parent instanceof GWReport)) {
				parent = parent.getParent();
			}
			
			GWReport report = (GWReport) parent;
			((ReportController) getFrameExtension().getGozerController()).replaceDetailsAction(target, report, gchart, key, subTitle);
		}
	}

}
