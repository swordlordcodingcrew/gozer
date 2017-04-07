package com.swordlord.gozer.components.wicket.report;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GActionBox;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.crosstab.GCrossTab;
import com.swordlord.gozer.components.generic.graph.GAreaChart;
import com.swordlord.gozer.components.generic.graph.GGraph;
import com.swordlord.gozer.components.generic.graph.GPieChart;
import com.swordlord.gozer.components.generic.graph.GStackedBarChart;
import com.swordlord.gozer.components.generic.report.GQueries;
import com.swordlord.gozer.components.generic.report.GReportPanel;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.GWActionBox;
import com.swordlord.gozer.components.wicket.crosstab.GWCrossTabPanel;
import com.swordlord.gozer.components.wicket.graph.GWAreaChartPanel;
import com.swordlord.gozer.components.wicket.graph.GWPieChartPanel;
import com.swordlord.gozer.components.wicket.graph.GWStackedBarChartPanel;
import com.swordlord.gozer.components.wicket.list.GWListPanel;


@SuppressWarnings("serial")
public class GWReportPanel extends GWPanel
{
	protected static final Log LOG = LogFactory.getLog(REPLACEME);

	public GWReportPanel(String id, final IModel<?> model, GReportPanel gfReportPanel)
	{
		super(id, model);

		LinkedList<ObjectBase> children = gfReportPanel.getChildren();

		// Create a new without GQueries
		LinkedList<ObjectBase> elemList = new LinkedList<ObjectBase>();
		for (ObjectBase child : children)
		{
			if (!child.getClass().equals(GQueries.class))
			{
				elemList.add(child);
			}
		}

		ListView<ObjectBase> listView = new ListView<ObjectBase>("eachGuiElem", elemList)
		{
			@Override
			protected void populateItem(ListItem<ObjectBase> item)
			{
				ObjectBase ob = item.getModelObject();
				Class<? extends ObjectBase> clazz = ob.getClass();
				
				if (clazz.equals(GActionBox.class))
				{
					GWActionBox panel = new GWActionBox("cell", model, (GActionBox) ob, null);
					panel.setVisible(true);
					item.add(panel);
				}
				else if (clazz.equals(GList.class))
				{
					GWListPanel panel = new GWListPanel("cell", model, (GList) ob, null);
					panel.setVisible(true);
					item.add(panel);
				} 
				else if (clazz.equals(GCrossTab.class))
				{
					GWCrossTabPanel panel = new GWCrossTabPanel("cell", model, (GCrossTab) ob);
					panel.setVisible(true);
					item.add(panel);
				} 
				else if (ob instanceof GGraph)
				{
					if(clazz.equals(GStackedBarChart.class))
					{
						GWStackedBarChartPanel panel = new GWStackedBarChartPanel("cell", model, (GStackedBarChart) ob);
						panel.setVisible(true);
						item.add(panel);
					}
					else if (clazz.equals(GPieChart.class))
					{
						GWPieChartPanel panel = new GWPieChartPanel("cell", model, (GPieChart) ob);
						panel.setVisible(true);
						item.add(panel);
					} 
					else if (clazz.equals(GAreaChart.class))
					{
						GWAreaChartPanel panel = new GWAreaChartPanel("cell", model, (GAreaChart) ob);
						panel.setVisible(true);
						item.add(panel);
					}
					else
					{
						LOG.error("This gozer graph type is not supported in reports.");
					}
				} 
				else
				{
					LOG.error("This gozer type is not supported in reports.");
				}

			}

		};

		//listView.setColumns(1);
		
		listView.setReuseItems(true);

		if (elemList.size() == 0)
		{
			listView.setVisible(false);
		}

		add(listView);
	}
}
