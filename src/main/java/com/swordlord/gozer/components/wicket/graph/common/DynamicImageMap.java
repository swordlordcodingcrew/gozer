package com.swordlord.gozer.components.wicket.graph.common;

import org.apache.wicket.ajax.markup.html.IAjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Produces markup for an image map HTML element with repeating mapped areas.
 * 
 */
public class DynamicImageMap extends WebMarkupContainer
{
	private static final long serialVersionUID = 8859550289557897390L;
	private String mapName;
	private RepeatingView areas;
	private int areaCounter = 0;

	public DynamicImageMap(final String id, String mapName)
	{
		super(id);
		this.mapName = mapName;
		areas = new RepeatingView("areas");
		add(areas);
	}

	public void addArea(String shape, String coords, String tooltipText, IAjaxLink linkCallback)
	{
		MapArea area = new MapArea(Integer.toString(areaCounter++), shape, coords, tooltipText, linkCallback);
		areas.add(area);
	}

	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		assert tag.getName().equals("map");
		tag.put("name", mapName);
	}
}