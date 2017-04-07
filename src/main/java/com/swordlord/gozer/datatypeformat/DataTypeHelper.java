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
 ** $Id: DataTypeHelper.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.datatypeformat;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.swordlord.jalapeno.DBGenerator;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;


/**
 * @author LordEidi
 *
 */
public class DataTypeHelper
{
	/**
	 * Return compatible class for typedValue based on untypedValueClass 
	 * 
	 * @param untypedValueClass
	 * @param typedValue
	 * @return
	 */
	public static Object fromDataType(Class<?> untypedValueClass, Object typedValue)
	{
		Logger LOG = LoggerFactory.getLogger(DataTypeHelper.class);

		if (typedValue == null)
		{
			return null;
		}
		
		if (untypedValueClass == null)
		{
			return typedValue;
		}

		if (ClassUtils.isAssignable(typedValue.getClass(), untypedValueClass))
		{
			return typedValue;
		}

		String strTypedValue = null;
		boolean isStringTypedValue = typedValue instanceof String;
		
		Number numTypedValue = null;
		boolean isNumberTypedValue = typedValue instanceof Number;
		
		Boolean boolTypedValue = null;
		boolean isBooleanTypedValue = typedValue instanceof Boolean;
		
		Date dateTypedValue = null;
		boolean isDateTypedValue = typedValue instanceof Date;

		if (isStringTypedValue)
		{
			strTypedValue = (String) typedValue;
		}
		if (isNumberTypedValue)
		{
			numTypedValue = (Number) typedValue;
		}
		if (isBooleanTypedValue)
		{
			boolTypedValue = (Boolean) typedValue;
		}
		if (isDateTypedValue)
		{
			dateTypedValue = (Date) typedValue;
		}

		Object v = null;
		if (String.class.equals(untypedValueClass))
		{
			v = ObjectUtils.toString(typedValue);
		}
		else if (BigDecimal.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = NumberUtils.createBigDecimal(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new BigDecimal(numTypedValue.doubleValue());
			}
			else if (isBooleanTypedValue)
			{
				v = new BigDecimal(BooleanUtils.toInteger(boolTypedValue.booleanValue()));
			}
			else if (isDateTypedValue)
			{
				v = new BigDecimal(dateTypedValue.getTime());
			}
		}
		else if (Boolean.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = BooleanUtils.toBooleanObject(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = BooleanUtils.toBooleanObject(numTypedValue.intValue());
			}
			else if (isDateTypedValue)
			{
				v = BooleanUtils.toBooleanObject((int) dateTypedValue.getTime());
			}
		}
		else if (Byte.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = Byte.valueOf(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new Byte(numTypedValue.byteValue());
			}
			else if (isBooleanTypedValue)
			{
				v = new Byte((byte) BooleanUtils.toInteger(boolTypedValue.booleanValue()));
			}
			else if (isDateTypedValue)
			{
				v = new Byte((byte) dateTypedValue.getTime());
			}
		}
		else if (byte[].class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = strTypedValue.getBytes();
			}
		}
		else if (Double.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = NumberUtils.createDouble(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new Double(numTypedValue.doubleValue());
			}
			else if (isBooleanTypedValue)
			{
				v = new Double(BooleanUtils.toInteger(boolTypedValue.booleanValue()));
			}
			else if (isDateTypedValue)
			{
				v = new Double(dateTypedValue.getTime());
			}
		}
		else if (Float.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = NumberUtils.createFloat(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new Float(numTypedValue.floatValue());
			}
			else if (isBooleanTypedValue)
			{
				v = new Float(BooleanUtils.toInteger(boolTypedValue.booleanValue()));
			}
			else if (isDateTypedValue)
			{
				v = new Float(dateTypedValue.getTime());
			}
		}
		else if (Short.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = NumberUtils.createInteger(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new Integer(numTypedValue.intValue());
			}
			else if (isBooleanTypedValue)
			{
				v = BooleanUtils.toIntegerObject(boolTypedValue.booleanValue());
			}
			else if (isDateTypedValue)
			{
				v = new Integer((int) dateTypedValue.getTime());
			}
		}
		else if (Integer.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = NumberUtils.createInteger(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new Integer(numTypedValue.intValue());
			}
			else if (isBooleanTypedValue)
			{
				v = BooleanUtils.toIntegerObject(boolTypedValue.booleanValue());
			}
			else if (isDateTypedValue)
			{
				v = new Integer((int) dateTypedValue.getTime());
			}
		}
		else if (Long.class.equals(untypedValueClass))
		{
			if (isStringTypedValue)
			{
				v = NumberUtils.createLong(strTypedValue);
			}
			else if (isNumberTypedValue)
			{
				v = new Long(numTypedValue.longValue());
			}
			else if (isBooleanTypedValue)
			{
				v = new Long(BooleanUtils.toInteger(boolTypedValue.booleanValue()));
			}
			else if (isDateTypedValue)
			{
				v = new Long(dateTypedValue.getTime());
			}
		}
		else if (java.sql.Date.class.equals(untypedValueClass))
		{
			if (isNumberTypedValue)
			{
				v = new java.sql.Date(numTypedValue.longValue());
			}
			else if (isDateTypedValue)
			{
				v = new java.sql.Date(dateTypedValue.getTime());
			}
		}
		else if (java.sql.Time.class.equals(untypedValueClass))
		{
			if (isNumberTypedValue)
			{
				v = new java.sql.Time(numTypedValue.longValue());
			}
			else if (isDateTypedValue)
			{
				v = new java.sql.Time(dateTypedValue.getTime());
			}
		}
		else if (java.sql.Timestamp.class.equals(untypedValueClass))
		{
			if (isNumberTypedValue)
			{
				v = new java.sql.Timestamp(numTypedValue.longValue());
			}
			else if (isDateTypedValue)
			{
				v = new java.sql.Timestamp(dateTypedValue.getTime());
			}
		}
		else if (Date.class.equals(untypedValueClass))
		{
			if (isNumberTypedValue)
			{
				v = new Date(numTypedValue.longValue());
			}
			else if (isStringTypedValue) {
				try
				{
					v = DateFormat.getDateInstance().parse(strTypedValue);
				} catch (ParseException e)
				{
					LOG.error("Unable to parse the date : " + strTypedValue);
					LOG.debug(e.getMessage());
				}
			}
		}
		return v;
	}

	/**
	 * @param dataTypeClass
	 * @param untypedValue
	 * @return
	 */
	public static Object toDataType(Class<?> dataTypeClass, Object untypedValue)
	{
		if ((dataTypeClass == null) || (untypedValue == null) || ClassUtils.isAssignable(untypedValue.getClass(), dataTypeClass))
		{
			if (Date.class == dataTypeClass)
			{
				return DateUtils.truncate(untypedValue, Calendar.DATE);
			}
			
			return untypedValue;
		}

		Object v = null;
		
		String strUntypedValue = null;
		boolean isStringUntypedValue = untypedValue instanceof String;
		
		Number numUntypedValue = null;
		boolean isNumberUntypedValue = untypedValue instanceof Number;
		
		if (isStringUntypedValue)
		{
			strUntypedValue = (String) untypedValue;
		}
		
		if (isNumberUntypedValue)
		{
			numUntypedValue = (Number) untypedValue;
		}

		if(dataTypeClass == boolean.class || dataTypeClass == Boolean.class)
		{
			if (isNumberUntypedValue)
			{
				v = BooleanUtils.toBooleanObject(numUntypedValue.intValue());
			}
			else if (isStringUntypedValue)
			{
				v = BooleanUtils.toBooleanObject(strUntypedValue);
			}
		}
		else if(dataTypeClass == Integer.class)
		{
			if (isNumberUntypedValue)
			{
				v = new Integer(numUntypedValue.intValue());
			}
			else if (isStringUntypedValue)
			{
				v = NumberUtils.createInteger(strUntypedValue);
			}
		}
		else if(dataTypeClass == Double.class)
		{
			if (isNumberUntypedValue)
			{
				v = new Double(numUntypedValue.doubleValue());
			}
			else if (isStringUntypedValue)
			{
				v = NumberUtils.createDouble(strUntypedValue);
			}
		}
		else if(dataTypeClass == Date.class)
		{
				if (isNumberUntypedValue)
				{
					v = DateUtils.truncate(new Date(numUntypedValue.longValue()), Calendar.DATE);
				}
		}
		else
		{
			v = ObjectUtils.toString(untypedValue);
		}
		
		return v;
	}
}
