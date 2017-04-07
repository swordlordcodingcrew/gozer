package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class FloatAverageMetric extends Metric
{
	private float f_total = 0;
	private float f_count = 0;
	private int float_decimal_places = -1;  // -1 not set, 0 same as int VIA TRUNCATE; otherwise any positive number

	public FloatAverageMetric ()
	{
		this.name = "Average";
	}

	public void add (String in)
	{
		f_count++;
		if (in == null) return;

		f_total = f_total + Float.parseFloat(in);
	}

	public String get ()
	{
		float fOut = f_total / f_count;

		Float ff = new Float(fOut);

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

	public FloatAverageMetric copy ()
	{
		FloatAverageMetric ftm = new FloatAverageMetric();
		ftm.setFloatDecimalPlaces(float_decimal_places);

		return ftm;
	}
}
