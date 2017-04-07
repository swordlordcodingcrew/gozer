package com.swordlord.gozer.crosstab;

import java.util.*;

import com.swordlord.jalapeno.datarow.DataRowBase;

/**

*/
public class Accumulator
{
	private Vector<Measure> measures = new Vector<Measure>();

	public Accumulator(AccumulatorDefinition ad)
	{
		for (int i = 0; i < ad.getMeasureCount(); i++)
		{
			MeasureDefinition md = ad.get(i);

			Measure measure = new Measure(md.getMetrics());

			measures.add(measure);
		}
	}

	public Measure getMeasure(int i)
	{
		return measures.get(i);
	}

	public Measure get(int i)
	{
		return measures.get(i);
	}

	public void add(DataRowBase row, AccumulatorDefinition ad)
	{
		for (int i = 0; i < measures.size(); i++)
		{
			Measure measure = measures.get(i);
			MeasureDefinition md = ad.get(i);
			measure.add(row.getPropertyAsStringForce(md.getDataRowsColumnName()));
		}
	}
}
