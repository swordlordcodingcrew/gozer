package com.swordlord.gozer.crosstab;

@SuppressWarnings("serial")
public class InadequateColumnCountException extends RuntimeException
{
	InadequateColumnCountException(String message)
	{
		super(message);
	}

}
