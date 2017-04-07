package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class NullCountMetric extends Metric
{
	private int null_hit_count = 0; //Number of times the database value was null, can affect averages for example

	public NullCountMetric ()
	{
		this.name = "Null Count";
	}

	public void add (String s)
	{
		if (s == null)
			null_hit_count++;
	}

	public String get ()
	{
		return Integer.toString(null_hit_count);
	}

	public NullCountMetric copy ()
	{
		return new NullCountMetric();
	}
}
