/*-----------------------------------------------------------------------------
**
** -Gozer is not Zuul-
**
** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
** and individual authors
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
** $Id: DataTypeFormat.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.datatypeformat;

import java.io.Serializable;
import java.text.Format;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("serial")
public abstract class DataTypeFormat implements Serializable
{
	protected Format _fieldFormat;

	/**
	 * Factory method to get the correct DataTypeFormat class
	 * 
	 * @param dataType
	 * @param strFormat
	 * @return
	 */
	public static DataTypeFormat getDataTypeFormat(Class<?> dataType, String strFormat)
	{
		if(dataType.equals(Date.class))
		{
			return new DateTypeFormat(strFormat);
		}
		else if(dataType.equals(Boolean.class))
		{
			return new BooleanTypeFormat(strFormat);
		}
		else if(dataType.equals(boolean.class) || 
				dataType.equals(Integer.class) || 
				dataType.equals(Short.class) || 
				dataType.equals(Long.class) || 
				dataType.equals(Float.class) || 
				dataType.equals(Double.class))
		{
			return new NumberTypeFormat(strFormat);
		}
		else
		{
			if((strFormat != null) && (strFormat.length() > 0))
			{
				return new TextTypeFormat(strFormat);
			}
			else
			{
				return new EmptyTypeFormat(strFormat);

			}
		}
	}
	
	public Format getFieldFormat()
	{
		return _fieldFormat;
	}

	protected Locale getLocale()
	{
		return Locale.getDefault();
	}
}
