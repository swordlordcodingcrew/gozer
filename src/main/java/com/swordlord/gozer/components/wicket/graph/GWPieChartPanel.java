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
 ** $Id: GWPieChartPanel.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.graph;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import com.swordlord.gozer.components.generic.graph.ChartImage;
import com.swordlord.gozer.components.generic.graph.GPieChart;
import com.swordlord.gozer.components.wicket.graph.common.GWChartPanel;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWPieChartPanel extends GWChartPanel
{
	private int value;

	public GWPieChartPanel(String id, IModel<?> model, GPieChart child)
	{
		super(id, model);

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

		ChartImage image = new ChartImage("chart", chart, child.getWidth(200), child.getHeight(200));
		add(image);

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
	
	}
}
