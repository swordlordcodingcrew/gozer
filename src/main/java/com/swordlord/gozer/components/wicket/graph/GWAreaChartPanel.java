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
 ** $Id: GWAreaChartPanel.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.graph;

import java.awt.Color;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.generic.graph.GAreaChart;
import com.swordlord.gozer.components.wicket.graph.common.GWChartPanel;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWAreaChartPanel extends GWChartPanel
{
	private int value;

	public GWAreaChartPanel(String id, IModel<?> model, GAreaChart child)
	{
		super(id, model);

		DataBindingMember dbMemberKey = child.getDataBindingMemberRowKey();
		DataBindingMember dbMemberValue = child.getDataBindingMemberValue();
		DataBindingManager dbManager = child.getDataBindingManager();

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

		JFreeChart chart = ChartFactory.createAreaChart(child.getTitle(), child.getCategoryAxisLabel(), child.getValueAxisLabel(), dcd, child.getOrientation(), child.getLegend(), false, false);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDrawingSupplier(child.getDrawingSupplier());
		
		add(new ChartImage("chart", chart, child.getWidth(200), child.getHeight(200)));
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
		// TODO Auto-generated method stub
		
	}

}
