package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class FloatTotalMetric extends Metric
{
	private float f_total = 0;
	private int float_decimal_places = -1;  // -1 not set, 0 same as int VIA TRUNCATE; otherwise any positive number

	public FloatTotalMetric ()
	{
		this.name = "Total";
	}

	public void add (String in)
	{
		if (in != null)
			f_total = f_total + Float.parseFloat(in);
	}

	public String get ()
	{
		Float ff = new Float(f_total);
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

	public FloatTotalMetric copy ()
	{
		FloatTotalMetric ftm = new FloatTotalMetric();
		ftm.float_decimal_places = float_decimal_places;

		return ftm;
	}
}
