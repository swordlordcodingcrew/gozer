package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class IntegerMaxMetric extends Metric
{
	private Integer i = null;

	public IntegerMaxMetric ()
	{
		this.name = "Max";	
	}

	public void add (String in)
	{
		if (in == null) return;

		//TODO: look for commas (formatting) or decimals(round to float?)

		if (i == null)
		{
			i = new Integer(in);
		}

		if (Integer.parseInt(in) > i.intValue())
			i = new Integer(in);
	}

	public String get ()
	{
		return Integer.toString(i);
	}

	public IntegerMaxMetric copy ()
	{
		return new IntegerMaxMetric();
	}
}
