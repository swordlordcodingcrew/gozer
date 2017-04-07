package com.swordlord.gozer.dataformat;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class BooleanFormat extends Format
{

	private static final long serialVersionUID = -1226588527868136587L;

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
	{
		return new StringBuffer(obj.toString());
	}

	@Override
	public Object parseObject(String source, ParsePosition pos)
	{
		return null;
	}

}
