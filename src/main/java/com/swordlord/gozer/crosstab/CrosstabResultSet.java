package com.swordlord.gozer.crosstab;

import java.util.Iterator;
import java.util.List;

import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.jalapeno.datarow.DataRowBase;

public class CrosstabResultSet
{
	Axis horizontal_axis = null;
	Axis vertical_axis = null;

	DataGrid data_rows = new DataGrid();

	private float build_time_seconds = -1;

	DisplayConverter dc = new DisplayConverter();

	// ---------------------------------------------------------------
	// Constructors
	// ---------------------------------------------------------------
	public CrosstabResultSet(DataBinding dataBindingList)
	{
		horizontal_axis = new Axis(dataBindingList, true);
		vertical_axis = new Axis(dataBindingList, false);

	}

	public void setDefaultsIfNeeded(MetaDataInformations meta)
	{
		if (horizontal_axis.getSliceSize() == 0)
			horizontal_axis.addSlice(meta.getColumnName(1));
		if (vertical_axis.getSliceSize() == 0)
			vertical_axis.addSlice(meta.getColumnName(2));

		if (data_rows.getMeasureCount() == 0)
		{
			MeasureDefinition m = new MeasureDefinition();
			m.setDataRowsColumn(meta.getColumnName(3));

			data_rows.addMeasureDefinition(m);
		}
	}

	public void clear()
	{
		horizontal_axis.clear();
		vertical_axis.clear();
		data_rows.clear();
	}

	public void addHorizontalValue(String h)
	{
		horizontal_axis.addValue(h);
	}

	public void addHorizontalValue(String h, String h2)
	{
		horizontal_axis.addValue(h, h2);
	}

	public void addVerticalValue(String v)
	{
		vertical_axis.addValue(v);
	}

	public void addVerticalValue(String h, String h2)
	{
		vertical_axis.addValue(h, h2);
	}

	public void setMaps()
	{
		horizontal_axis.setMap();
		vertical_axis.setMap();
	}

	public void setTime(float i)
	{
		build_time_seconds = i / 1000;
	}

	public void setAxesValues(List<DataRowBase> rs, MetaDataInformations meta)
	{
		if (horizontal_axis.getSliceSize() == 0)
			horizontal_axis.addSlice(meta.getColumnName(0));
		if (vertical_axis.getSliceSize() == 0)
			vertical_axis.addSlice(meta.getColumnName(1));

		horizontal_axis.setValues(rs);
		vertical_axis.setValues(rs);
	}

	/**
	 * This method traverses the input ResultSet and correctly sets values into
	 * the data rows. The horizontal and vertical axes must be correctly set and
	 * fully configured prior to calling this method.
	 */
	public void setRows(List<DataRowBase> rs)
	{
		data_rows.initialize(vertical_axis.size(), horizontal_axis.size());

		Iterator<DataRowBase> iter = rs.iterator();
		while (iter.hasNext())
		{
			DataRowBase row = iter.next();
			int h_idx = horizontal_axis.getHash(horizontal_axis.getMap(row));
			int v_idx = vertical_axis.getHash(vertical_axis.getMap(row));

			data_rows.addData(row, v_idx, h_idx);
		}
	}

	public void setAxisGrids()
	{
		if (horizontal_axis.measuresOnThisAxis())
		{
			horizontal_axis.setGrid(data_rows.getAD());
			vertical_axis.setGrid(null);
		} else if (vertical_axis.measuresOnThisAxis())
		{
			horizontal_axis.setGrid(null);
			vertical_axis.setGrid(data_rows.getAD());
		} else
		{
			// both axis have measures_on_this_axis = false, fall back to
			// default of horizontal.
			setMeasuresOnColumn(true);
			horizontal_axis.setGrid(data_rows.getAD());
			vertical_axis.setGrid(null);
		}
	}

	public void setDataGrid()
	{
		dc.hor = horizontal_axis.getAxisGrid();
		dc.vert = vertical_axis.getAxisGrid();

		data_rows.initializeGrid(dc.vert.length, dc.hor[0].length);
		data_rows.setGrid(vertical_axis.measuresOnThisAxis(), dc.vert.length);
		dc.data_grid = data_rows.getDataGrid();
	}

	public String[][] getHorizontalGrid()
	{
		return horizontal_axis.getAxisGrid();
	}

	public String[][] getVerticalGrid()
	{
		return vertical_axis.getAxisGrid();
	}

	public Accumulator[][] getDataGrid()
	{
		return data_rows.getDataRows();
	}

	public int getVerticalAxisSliceCount()
	{
		return vertical_axis.getSliceCount();
	}

	public int getHorizontalAxisSliceCount()
	{
		return horizontal_axis.getSliceCount();
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer("jCrosstabResultSet Dump\n");
		str.append("--------------------------\n");

		str.append(horizontal_axis.toString());
		str.append(vertical_axis.toString());

		str.append("build_time_seconds is " + build_time_seconds);

		return str.toString();
	}

	public String getDataRowsColumnName(int measure_index)
	{
		MeasureDefinition md = data_rows.getMeasureDefinition(measure_index);
		return md.getName();
	}

	public void addMeasureDefinition(MeasureDefinition m)
	{
		data_rows.addMeasureDefinition(m);
	}

	public int getMeasureMetricColumnCount()
	{
		return data_rows.getMeasureMetricColumnCount();
	}

	public int getMeasureCount()
	{
		return data_rows.getMeasureCount();
	}

	public void setMeasuresOnColumn(boolean measures_on_column)
	{
		horizontal_axis.setMeasuresOnThisAxis(measures_on_column);
		vertical_axis.setMeasuresOnThisAxis(!measures_on_column);
	}

	public String getTabDelimitedTable()
	{
		return dc.getTabDelimitedTable(data_rows.getAD(), vertical_axis.measuresOnThisAxis(), vertical_axis.getSliceCount());
	}

	public String getHtmlTable(boolean write_entire_page)
	{
		return dc.getHtmlTable(data_rows.getAD(), vertical_axis.measuresOnThisAxis(), write_entire_page, vertical_axis.getSliceCount(), horizontal_axis.getSliceCount());
	}

	public String getHtmlTable()
	{
		return dc.getHtmlTable(data_rows.getAD(), vertical_axis.measuresOnThisAxis(), false, vertical_axis.getSliceCount(), horizontal_axis.getSliceCount());
	}

	public String writeDataGrid()
	{
		return dc.writeDataGrid();
	}

	/**
	 * Returns how long the build took in seconds.
	 */
	public float getBuildTimeSeconds()
	{
		return build_time_seconds;
	}

	/**
	 * Convenience method to getBuildTimeSeconds()
	 */
	public float getBuildTime()
	{
		return getBuildTimeSeconds();
	}

	/**
	 * Convenience method to getBuildTimeSeconds()
	 */
	public float getTime()
	{
		return getBuildTimeSeconds();
	}

	/**
	 * Sort the display Ascending by default on column indicated. Alias to
	 * sortOnColumn(true, col_idx).
	 */
	public void sortOnColumn(int col_idx)
	{
		sortOnColumn(true, col_idx);
	}

	/**
	 * Sort the display Ascending (true) or Descending (false) on column
	 * indicated.
	 */
	public void sortOnColumn(boolean sort_ascending, int col_idx)
	{
		dc.sortOnColumn(sort_ascending, col_idx);
	}

	/**
	 * Sort the display Ascending by default (right to left) on row indicated.
	 * Alias to sortOnRow(true, col_idx).
	 */
	public void sortOnRow(int row_idx)
	{
		sortOnRow(true, row_idx);
	}

	/**
	 * Sort the display Ascending (true) or Descending (false) on row (i.e.,
	 * right-to-left) indicated.
	 */
	public void sortOnRow(boolean sort_ascending, int row_idx)
	{
		dc.sortOnRow(sort_ascending, row_idx);
	}

	/**
	 * Reset the MeasureDefinition vector to a blank state (new Vector).
	 */
	public void setNewMeasureDefintions()
	{
		data_rows.setNewMeasureDefintions();
	}

	/**
	 * Return the AccumulatorDefinition from the data rows.
	 */
	public AccumulatorDefinition getAccumulatorDefinition()
	{
		return data_rows.getAccumulatorDefinition();
	}

	public List<DataRowBase> getDataRowBase()
	{
		return dc.getDataRowBase(data_rows.getAD(), vertical_axis.measuresOnThisAxis(), vertical_axis.getSliceCount(), horizontal_axis.size());
	}

}
