package com.swordlord.gozer.crosstab;

import java.util.*;

import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * A generic Axis, could be either horizontal or vertical. It contains one or
 * more slices.
 */
public class Axis
{
	private DataBinding _dataBindingList;
	private Vector<SliceDefinition> slices = new Vector<SliceDefinition>();
	private Slice first_slice = new Slice();
	private Map<String, Integer> map = null;

	private boolean is_horizontal = true; // If false, then this must be the
	// vertical axis.
	private boolean measures_on_this_axis = true; // Measures are by default on
	// the horizontal axis.

	private String[][] axis_grid = null; // A representation of this Axis as a 2

	// dimensional string array.

	// ------------------------------------------------------------
	// -- constructor
	// ------------------------------------------------------------

	public Axis(DataBinding dataBindingList, boolean horizontal)
	{
		_dataBindingList = dataBindingList;
		is_horizontal = horizontal;
		measures_on_this_axis = horizontal;
	}

	/**
	 * Add a value to slice index 0. This is included because it will be a very
	 * common, simple case.
	 */
	public void addValue(String s)
	{
		first_slice.addValue(s);
	}

	/**
	 * Add a new slice value under the first parameter. The first param must
	 * already exist in Slice 0, the second will be added as a new sub-slice if
	 * it doesn't already exist.
	 */
	public void addValue(String s, String s2)
	{
		for (int i = 0; i < first_slice.sub_slice.size(); i++)
		{
			Slice slice = first_slice.sub_slice.get(i);
			if (slice.getValue().equals(s))
			{
				slice.addValue(s2);
			}
		}
	}

	/**
	 * For slice value lists 3 items or longer.
	 */
	public void addValue(Vector<String> value_list)
	{
		first_slice.addValue(value_list);
	}

	// -------------------------------------------------------------------
	// -- sort methods
	// -------------------------------------------------------------------

	/**
	 * Sort the slice given by the index parameter.
	 */
	public void sort(int slice_idx)
	{
		first_slice.sort(slice_idx);
	}

	public int getSliceDefinitionCount()
	{
		return slices.size();
	}

	public int size()
	{
		return map.size();
	}

	public int getHash(String s)
	{
		return map.get(s);
	}

	public int getColumnCount(int measure_columns)
	{
		return first_slice.getColumnCount(measure_columns);
	}

	public void setMap()
	{
		map = new HashMap<String, Integer>(getColumnCount(1));

		int current_val = 0;

		for (int i = 0; i < first_slice.sub_slice.size(); i++)
		{
			Slice slice = first_slice.sub_slice.get(i);
			String map_string = "map-";

			current_val = slice.getMapSlice(map_string, map, current_val);
		}
	}

	public void setGrid(AccumulatorDefinition ad)
	{
		int measure_metric_column_count = 0;

		if (ad != null)
		{
			measure_metric_column_count = ad.getMeasureMetricColumnCount();
		}

		if (measures_on_this_axis)
		{
			if (ad.isMultiMetric() && ad.isMultiMeasure())
				axis_grid = new String[slices.size() + 2][getColumnCount(measure_metric_column_count)];
			else if (ad.isMultiMeasure() || ad.isMultiMetric())
				axis_grid = new String[slices.size() + 1][getColumnCount(measure_metric_column_count)];
			else
				// single measure or single metric
				axis_grid = new String[slices.size()][getColumnCount(measure_metric_column_count)];
		} else
			axis_grid = new String[slices.size()][getColumnCount(1)];

		int current_slice = 0;

		for (int i = 0; i < first_slice.sub_slice.size(); i++)
		{
			Slice slice = first_slice.sub_slice.get(i);
			Vector<String> grid_slice_values = new Vector<String>();

			if ((measures_on_this_axis) && (ad.isMultiMeasure() || ad.isMultiMetric()))
				current_slice = slice.getGridSlice(grid_slice_values, current_slice, axis_grid, ad);
			else
				current_slice = slice.getGridSlice(grid_slice_values, current_slice, axis_grid, null);
		}

		if (!is_horizontal)
		{
			String[][] rotated_grid;

			// "rotate" the grid;
			if (measures_on_this_axis)
			{
				if (ad.isMultiMeasure() && ad.isMultiMetric())
					rotated_grid = new String[getColumnCount(measure_metric_column_count)][slices.size() + 2];
				else
					rotated_grid = new String[getColumnCount(measure_metric_column_count)][slices.size() + 1];
			} else
				rotated_grid = new String[getColumnCount(1)][slices.size()];
			// and
			// THIN

			for (int i = 0; i < axis_grid.length; i++)
			{
				for (int j = 0; j < axis_grid[i].length; j++)
				{
					rotated_grid[j][i] = axis_grid[i][j];
				}
			}
			axis_grid = rotated_grid;
		}
	}

	public Vector<String> getSliceValues(int slice_idx)
	{
		return first_slice.getSliceValues(slice_idx);
	}

	public Vector<Slice> getSliceElements(int slice_idx)
	{
		return first_slice.getSliceElements(slice_idx);
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer("\n ---------- Axis Values ---------------- \n");

		str.append("\tSlice Definitions:\n");
		for (int i = 0; i < slices.size(); i++)
		{
			SliceDefinition defn = slices.get(i);
			str.append(defn.toString());
		}
		str.append('\n');

		str.append("map, size is " + map.size() + ": " + map.toString());

		str.append('\n');

		str.append(first_slice.toString(1));

		return str.toString();
	}

	public void clear()
	{
		System.out.println("Axis.java, 214: " + "Clearing axis");
		first_slice = new Slice();
		map = null;
	}

	public SliceDefinition getSliceDefinition(int i)
	{
		return slices.get(i);
	}

	public void addSlice(String s)
	{
		slices.add(new SliceDefinition(s));
	}

	public int getSliceCount()
	{
		return slices.size();
	}

	public int getSpan()
	{
		return first_slice.getSpan();
	}

	public void setValues(List<DataRowBase> rs)
	{
		Vector<String> slice_value_list = new Vector<String>();

		for (int i = 0; i < getSliceCount(); i++)
		{
			Iterator<DataRowBase> iter = rs.iterator();
			while (iter.hasNext())
			{
				DataRowBase datarow = iter.next();
				slice_value_list.clear();
				for (int j = 0; j <= i; j++)
				{
					String dataBinding = _dataBindingList.getDataBindingMember() + "." + getSliceDefinition(j).getDatabaseColumn();
					DataBindingMember dbm = new DataBindingMember(dataBinding);
					DataRowBase row = _dataBindingList.getDataBindingManager().getResolvedRow(dbm, datarow);	

					slice_value_list.add(row.getPropertyAsStringForce(dbm.getDataBindingFieldName()));
				}
				addValue(slice_value_list);
			}
			sort(i);
		}

	}

	public String getMap(DataRowBase datarow)
	{
		StringBuffer h = new StringBuffer("map-");

		for (int i = 0; i < slices.size(); i++)
		{
			String dataBinding = _dataBindingList.getDataBindingMember() + "." + getSliceDefinition(i).getDatabaseColumn();
			DataBindingMember dbm = new DataBindingMember(dataBinding);
			DataRowBase row = _dataBindingList.getDataBindingManager().getResolvedRow(dbm, datarow);	

			h.append(row.getPropertyAsStringForce(dbm.getDataBindingFieldName()));

			if (i < (slices.size() - 1))
				h.append('-');
		}

		return h.toString();
	}

	public int getWidth()
	{
		return axis_grid[0].length;
	}

	public int getHeight()
	{
		return axis_grid.length;
	}

	public boolean measuresOnThisAxis()
	{
		return measures_on_this_axis;
	}

	public void setMeasuresOnThisAxis(boolean on_this_axis)
	{
		measures_on_this_axis = on_this_axis;
	}

	public int getSliceSize()
	{
		return slices.size();
	}

	public String[][] getAxisGrid()
	{
		return axis_grid;
	}
}
