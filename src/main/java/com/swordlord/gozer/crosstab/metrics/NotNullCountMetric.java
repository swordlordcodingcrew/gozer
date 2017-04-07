package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class NotNullCountMetric extends Metric
{
	private int not_null_hit_count = 0; //Number of times the database value was null, can affect averages for example

	public NotNullCountMetric ()
	{
		this.name = "Not Null Count";
	}

	public void add (String s)
	{
		if (s != null)
			not_null_hit_count++;
	}

	public String get ()
	{
		return Integer.toString(not_null_hit_count);
	}

	public NotNullCountMetric copy ()
	{
		return new NotNullCountMetric();
	}
}
