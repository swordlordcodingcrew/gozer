package com.swordlord.gozer.crosstab;

/**
 * This object is set BEFORE the jCrosstabResultSet is built. It allows the user
 * to define axis slices prior to launching the build.
 */

public class SliceDefinition
{
	private String database_column_name = null;

	public SliceDefinition()
	{
	}

	public SliceDefinition(String s)
	{
		database_column_name = s;
	}

	public void setDatabaseColumn(String s)
	{
		database_column_name = s;
	}

	public String getDatabaseColumn()
	{
		return database_column_name;
	}

	public String toString()
	{
		return "\t\tSlice Defn: " + "Name: " + database_column_name + ", " + "\n";
	}

}
