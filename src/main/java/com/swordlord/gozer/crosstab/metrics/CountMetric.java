package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

/**  This metric records how many input rows were used, including null values.
*/
public class CountMetric extends Metric
{
	private int total_hit_count = 0; //Total number of times into

	public CountMetric ()
	{
		this.name = "Count";
	}

	public void add (String s)
	{
		total_hit_count++;
	}

	public String get ()
	{
		return Integer.toString(total_hit_count);		
	}

	public CountMetric copy ()
	{
		return new CountMetric();
	}
}
