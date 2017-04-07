package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class IntegerTotalMetric extends Metric
{
	private int i_total = 0;

	public IntegerTotalMetric ()
	{
		this.name = "Total";
	}

	public void add (String in)
	{
		if (in != null)
			i_total = i_total + Integer.parseInt(in);
	}

	public String get ()
	{
		return Integer.toString(i_total);
	}

	public IntegerTotalMetric copy ()
	{
		return new IntegerTotalMetric();
	}
}
