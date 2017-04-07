package com.swordlord.gozer.crosstab;

import java.util.*;

import com.swordlord.gozer.crosstab.metrics.IntegerTotalMetric;

/**
 * A Measure represents a single (normally numeric) column in a ResultSet object
 * such as 'units' or 'sales'. This class is used to specify the measures before
 * the JCrosstabResultSet object is built. This class is passed to the Measures
 * to tell them how to set themselves up.
 */
public class MeasureDefinition
{
	private String name = null; // This is like a label for the measure, to
								// allow different names than the database
								// column name.

	private String data_rows_column_name = null;
	// The third column in the resultset is the default.

	private Vector<Metric> metrics = new Vector<Metric>();

	// --------------------------------------------------------------------------
	// -- Constructors
	// ----------------------------------------------------------
	// --------------------------------------------------------------------------

	public MeasureDefinition()
	{
		metrics.add(new IntegerTotalMetric());
	}

	public MeasureDefinition(boolean createDefaultIntegerTotal)
	{
		if (createDefaultIntegerTotal)
			metrics.add(new IntegerTotalMetric());
	}

	// -----------------------------------------------------------------------------
	// -- setters
	// ------------------------------------------------------------------
	// -----------------------------------------------------------------------------

	/**
	 * For this measure, define it by the given column name (versus column
	 * index). Alias to setDataRowsColumnByName(String n).
	 */
	public void setDataRowsColumn(String n)
	{
		setDataRowsColumnByName(n);
	}

	/**
	 * For this measure, define it by the given column name (versus column
	 * index).
	 */
	private void setDataRowsColumnByName(String n)
	{
		data_rows_column_name = n;
		if (name == null)
			name = n;
	}

	public void setName(String n)
	{
		name = n;
	}

	// -----------------------------------------------------------------------------
	// -- getters
	// ------------------------------------------------------------------
	// -----------------------------------------------------------------------------

	public String getName()
	{
		return name;
	}

	public String getDataRowsColumnName()
	{
		return data_rows_column_name;
	}

	// --------------------------------------------------------------------------
	// -- Pass thru to Metric class methods
	// -------------------------------------
	// --------------------------------------------------------------------------

	public int getMetricShowCount()
	{
		return metrics.size();
	}

	public int getMetricCount()
	{
		return getMetricShowCount();
	}

	public Metric getMetric(int metricIndex)
	{
		return metrics.get(metricIndex);
	}

	public Vector<Metric> getMetrics()
	{
		return metrics;
	}

	public void add(Metric m)
	{
		metrics.add(m);
	}
}
