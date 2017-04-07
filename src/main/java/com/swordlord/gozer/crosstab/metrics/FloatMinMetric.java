package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class FloatMinMetric extends Metric
{
	private Float aFloat = null;
	private int float_decimal_places = -1;  // -1 not set, 0 same as int VIA TRUNCATE; otherwise any positive number

	public FloatMinMetric ()
	{
		this.name = "Min";
	}

	public void add (String in)
	{
		if (in == null) return;

		if (aFloat == null)
		{
			aFloat = new Float(in);
		}

		if (Float.parseFloat(in) < aFloat.floatValue())
			aFloat = new Float(in);
	}

	public String get ()
	{
		Float ff = new Float(aFloat);
		if (float_decimal_places == -1)
			return ff.toString();
		else if (float_decimal_places == 0)
			return ff.toString().substring(0, ff.toString().indexOf('.'));
		else
			return (ff.toString() + "000" ).substring(0, ff.toString().indexOf('.') + 1 + float_decimal_places);
	}

	public void setFloatDecimalPlaces (int n)
	{
		float_decimal_places = n;
	}

	public FloatMinMetric copy ()
	{
		FloatMinMetric ftm = new FloatMinMetric();
		ftm.float_decimal_places = float_decimal_places;

		return ftm;
	}
}
