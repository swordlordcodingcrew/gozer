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
** $Id: NumberTypeFormat.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.datatypeformat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@SuppressWarnings("serial")
public class NumberTypeFormat extends DataTypeFormat
{
	public NumberTypeFormat(String strPattern)
	{
		DecimalFormatSymbols sym = new DecimalFormatSymbols(getLocale());
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(sym);

		if ((strPattern != null) && (strPattern.length() > 0))
		{
			format.applyPattern(strPattern);
		}

		_fieldFormat = format;
	}
}
