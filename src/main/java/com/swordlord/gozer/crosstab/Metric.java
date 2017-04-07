package com.swordlord.gozer.crosstab;

/**
 * A Metric is an aspect of a Measure to display. For example, if the 'measure'
 * is a database column named 'units sold', then the metric would normally be
 * total, but could also be count, min, max, average or any calculation. This
 * class was designed to allow the user to create his or her own metrics.
 */
public abstract class Metric
{
	protected String name = "Metric";

	public String getName()
	{
		return name;
	}

	public void setName(String n)
	{
		this.name = n;
	}

	public abstract Metric copy();

	public abstract void add(String s);

	public abstract String get();
}
