package com.swordlord.gozer.crosstab;

import java.util.List;

import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.jalapeno.datarow.DataRowBase;

//For thought: can this class be removed and everything done from the jCrosstabResultSet class?

/** This class is primarily a wrapper for jCrosstabResultSet. */
public class Crosstabber
{
	CrosstabResultSet jxtab = null;

	
	public Crosstabber(DataBinding dataBindingList)
	{
		jxtab = new CrosstabResultSet(dataBindingList);
	}

	/**
	 * Given a ResultSet object, return a jCrosstabResultSet data structure.
	 * This is the primary method for this class. Note: the ResultSet object
	 * passed in has to be created by a Statement object, NOT a
	 * PreparedStatement object. PreparedStatements do not put column names and
	 * such in the same places as Statement. See TestHarness.java for an
	 * example. Setting slices and indicating where columns should go must be
	 * done BEFORE calling this method.
	 */
	public CrosstabResultSet getCrosstabResultSet(List<DataRowBase> rs, MetaDataInformations meta) throws InadequateColumnCountException
	{
		java.util.Date startTime = new java.util.Date();

		jxtab.clear();

		jxtab.setDefaultsIfNeeded(meta);

		jxtab.setAxesValues(rs, meta);

		jxtab.setMaps();
		jxtab.setAxisGrids();

		jxtab.setRows(rs);

		jxtab.setDataGrid();

		java.util.Date endTime = new java.util.Date();
		jxtab.setTime(endTime.getTime() - startTime.getTime());

		return jxtab;
	}

	/**
	 * Add a new slice to the horizontal axis, the String parameter signifies
	 * that the corresponding database column will based on column name.
	 */
	public void addHorizontalSlice(String s)
	{
		jxtab.horizontal_axis.addSlice(s);
	}

	/**
	 * Add a new slice to the vertical axis, the String parameter signifies that
	 * the corresponding database column will based on column name.
	 */
	public void addVerticalSlice(String s)
	{
		jxtab.vertical_axis.addSlice(s);
	}

	public void addMeasureDefinition(MeasureDefinition md)
	{
		jxtab.addMeasureDefinition(md);
	}

	/**
	 * Sets measures to display as columns with column headers. This is the
	 * default and the normal way most people would expect to see data.
	 */
	public void setMeasuresOnColumn()
	{
		jxtab.setMeasuresOnColumn(true);
	}

	/**
	 * Sets the measures to display from left-to-right with the headers at the
	 * "row" level.
	 */
	public void setMeasuresOnRow()
	{
		jxtab.setMeasuresOnColumn(false);
	}
}
