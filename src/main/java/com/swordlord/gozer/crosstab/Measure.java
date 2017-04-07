package com.swordlord.gozer.crosstab;

import java.util.*;

/**
 * A measure is analogous to a numeric database column which is summarized to
 * display.
 */
public class Measure
{
	private Vector<Metric> metrics = new Vector<Metric>();

	public Measure(Vector<Metric> m)
	{
		for (int i = 0; i < m.size(); i++)
		{
			metrics.add(m.get(i).copy());
		}
	}

	/**
	 * Add a new value to this measure for accumulation.
	 */
	public void add(String s) // add DATA
	{
		for (int m = 0; m < metrics.size(); m++)
		{
			Metric met = metrics.get(m);
			met.add(s);
		}
	}

	/**
	 * Add a new Metric to the list, appended to the end.
	 */
	public void add(Metric m) // add a METRIC
	{
		metrics.add(m);
	}

	/**
	 * For the given metric index, return the (numeric) value contained inside.
	 */
	public String getMetricValue(int metricIndex)
	{
		Metric measure = metrics.get(metricIndex);
		return measure.get();
	}

	/**
	 * Return the metric indicated by the index.
	 */
	public Metric getMetric(int metricIndex)
	{
		return metrics.get(metricIndex);
	}

	/**
	 * Return the metric indicated by the index. Alias to getMetric(idx).
	 */
	public Metric get(int metricIndex)
	{
		return getMetric(metricIndex);
	}

	/**
	 * Return the number of metrics for this Measure.
	 */
	public int getMetricCount()
	{
		return metrics.size();
	}

}
