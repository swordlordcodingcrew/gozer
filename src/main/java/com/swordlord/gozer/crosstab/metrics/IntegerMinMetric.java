/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 **
 ** This program is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Affero General Public License as published by the Free
 ** Software Foundation, either version 3 of the License, or (at your option)
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful, but WITHOUT
 ** ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 ** FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for
 ** more details.
 **
 ** You should have received a copy of the GNU Affero General Public License along
 ** with this program. If not, see <http://www.gnu.org/licenses/>.
 **
 **-----------------------------------------------------------------------------
 **
 ** $Id: $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.crosstab.metrics;

import com.swordlord.gozer.crosstab.Metric;

public class IntegerMinMetric extends Metric
{
	private Integer i = null;

	public IntegerMinMetric ()
	{
		this.name = "Min";
	}

	public void add (String in)
	{
		if (in == null) return;

		if (i == null)
		{
			i = new Integer(in);
		}

		if (Integer.parseInt(in) < i.intValue())
			i = new Integer(in);
	}

	public String get ()
	{
		return Integer.toString(i);
	}

	public IntegerMinMetric copy ()
	{
		return new IntegerMinMetric();
	}
}
