package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class IntegerMinMetric extends Metric
{
	private Integer i = null;

	public IntegerMinMetric ()
	{
		this.name = "Min";
	}

	public void add (String in)
	{
		if (in == null) return;

		if (i == null)
		{
			i = new Integer(in);
		}

		if (Integer.parseInt(in) < i.intValue())
			i = new Integer(in);
	}

	public String get ()
	{
		return Integer.toString(i);
	}

	public IntegerMinMetric copy ()
	{
		return new IntegerMinMetric();
	}
}
