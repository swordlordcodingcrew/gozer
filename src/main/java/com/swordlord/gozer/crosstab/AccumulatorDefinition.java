package com.swordlord.gozer.crosstab;

import java.util.*;

/**
 * This class is set before the jCrosstabResultSet object is built, it defines
 * what will go in the data rows. The accumulator definition is a wrapper for
 * the measure definitions (MeasureDefinition object). It is generally not
 * accessed directly.
 */
class AccumulatorDefinition
{
	private Vector<MeasureDefinition> measure_definitions = new Vector<MeasureDefinition>();

	/**
	 * Return the total number of metrics across all measures.
	 */
	public int getMeasureMetricColumnCount()
	{
		int metricCount = 0;
		for (int m = 0; m < measure_definitions.size(); m++)
		{
			MeasureDefinition md = measure_definitions.get(m);
			metricCount = metricCount + md.getMetricShowCount();
		}
		return metricCount;
	}

	/**
	 * Return the number of Measures.
	 */
	public int getMeasureCount()
	{
		return measure_definitions.size();
	}

	/**
	 * Reset the MeasureDefinition vector to a blank state (new Vector).
	 */
	public void setNewMeasureDefintions()
	{
		measure_definitions = new Vector<MeasureDefinition>();
	}

	/**
	 * Add a new MeasureDefinition.
	 */
	public void add(MeasureDefinition md)
	{
		measure_definitions.add(md);
	}

	/**
	 * Return the MeasureDefinition indicated by the index.
	 */
	public MeasureDefinition get(int i)
	{
		return measure_definitions.get(i);
	}

	public Vector<MeasureDefinition> getMeasureDefinitions()
	{
		return measure_definitions;
	}

	public boolean isMultiMeasure()
	{
		return measure_definitions.size() > 1;
	}

	public boolean isMultiMetric()
	{
		for (int measure = 0; measure < measure_definitions.size(); measure++)
		{
			MeasureDefinition md = measure_definitions.get(measure);
			if (md.getMetricCount() > 1)
				return true;
		}

		return false;
	}

}
