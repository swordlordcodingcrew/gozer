package com.swordlord.gozer.crosstab;

import java.util.*;

/**
 * A Slice is a lengthwise cross section of an axis. For example, if you have
 * the horizontal axis set to Year, Quarter, then that axis has 2 slices, the
 * first by Year, then a second by Quarter.
 */

public class Slice
{
	private String value = null; // These will be null on the first slice, slice
									// index 0? Is that right?

	// This is a temporary data structure to speed entry of values prior to
	// building sub_slice
	private SortedSet<String> values = new TreeSet<String>();

	Vector<Slice> sub_slice = new Vector<Slice>();

	public Slice()
	{
	}

	public Slice(String s)
	{
		value = s;
	}

	public String toString(int tabs)
	{
		String spacer = "\t";

		for (int i = 0; i < tabs; i++)
		{
			spacer = spacer + "\t";
		}

		StringBuffer str = new StringBuffer("\n" + spacer + " --- Slice --- " + value + " " + " -------------------- \n");
		str.append(spacer + " value: " + value + "\n");

		str.append(spacer + "String values: " + values + "\n");

		if (sub_slice.size() > 0)
		{
			str.append(spacer + "... sub_slice: \n");
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				str.append(slice.toString(tabs + 1));
			}
		}

		str.append(spacer + "--- End Slice ------------------------------------------ \n");

		return str.toString();
	}

	public int getColumnCount(int measure_column_count)
	{
		// System.out.println("Slice.java, 63: " +
		// "Entered column count for slice: " + getValue());
		if (sub_slice.size() > 0)
		{
			int col_count = 0;
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				col_count = col_count + slice.getColumnCount(measure_column_count);
			}
			// System.out.println("Slice.java, 73: " +
			// "returning column count of " + col_count);
			return col_count;
		} else
		{
			// System.out.println("Slice.java, 78: " +
			// "sub slice is size 0, returning");
			return measure_column_count;
		}
	}

	public String getValue()
	{
		return value;
	}

	public int getMapSlice(String map_string, Map<String, Integer> map, int current_index)
	{
		map_string = map_string + getValue();

		if (sub_slice.size() > 0)
		{
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				// System.out.println("Slice.java, 108: " +
				// "Going to sub-slice");
				current_index = slice.getMapSlice(map_string + "-", map, current_index);
			}
		} else
		{
			// bottom of tree, time to set map key
			// System.out.println("Slice.java, 115: " +
			// "At bottom of map slice, map_string is " + map_string +
			// " current index is " + current_index);
			map.put(map_string, current_index);
			current_index++;
		}

		// For thought: set span here??
		return current_index;
	}

	public int getGridSlice(Vector<String> grid_slice_values, int current_slice, String[][] axis_grid, AccumulatorDefinition ad)
	{
		Vector<MeasureDefinition> measures_list = null;

		if (ad != null)
			measures_list = ad.getMeasureDefinitions();

		// System.out.println("Slice.java, 128: " +
		// "Entered getGridSlice, values are " + grid_slice_values);
		grid_slice_values.add(getValue());
		if (sub_slice.size() > 0)
		{
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				current_slice = slice.getGridSlice(grid_slice_values, current_slice, axis_grid, ad);
				grid_slice_values.remove(grid_slice_values.lastElement());
			}
		} else
		{
			if (measures_list != null)
			{
				if (measures_list.size() == 0)
				{
					// what??
				} else if (measures_list.size() == 1) // Single Measure
				{
					MeasureDefinition md = measures_list.get(0);

					if (ad.isMultiMetric()) // Single measure. multimetric
					{
						for (int metric = 0; metric < md.getMetricShowCount(); metric++)
						{
							Metric m = md.getMetric(metric);
							pushWritePopValue(axis_grid, grid_slice_values, m.getName(), current_slice);
							current_slice++;
						}
					} else
					// Single measure, single metric
					{
						pushWritePopValue(axis_grid, grid_slice_values, md.getName(), current_slice);
						current_slice++;
					}
				} else
				// Multi measure
				{
					// TODO: fix this, it only examines the first measure for
					// multiple metrics. need to test all measures.
					if (ad.isMultiMetric()) // mutli metric
					{
						for (int k = 0; k < measures_list.size(); k++)
						{
							MeasureDefinition md = measures_list.get(k);
							grid_slice_values.add(md.getName());

							for (int metric = 0; metric < md.getMetricShowCount(); metric++)
							{
								Metric m = md.getMetric(metric);
								pushWritePopValue(axis_grid, grid_slice_values, m.getName(), current_slice);
								current_slice++;
							}

							grid_slice_values.remove(md.getName());
						}
					} else
					// Multi measure, single metric
					{
						for (int k = 0; k < measures_list.size(); k++)
						{
							MeasureDefinition md = measures_list.get(k);
							pushWritePopValue(axis_grid, grid_slice_values, md.getName(), current_slice);
							current_slice++;
						}
					}
				}
			} else
			{
				for (int i = 0; i < grid_slice_values.size(); i++)
				{
					// System.out.println("Slice.java, 231: " +
					// "Going to add values to grid " + grid_slice_values);
					axis_grid[i][current_slice] = grid_slice_values.get(i);
				}
				current_slice++;
			}
		}

		return current_slice;
	}

	private void pushWritePopValue(String[][] axis_grid, Vector<String> grid_slice_values, String push, int current_slice)
	{
		grid_slice_values.add(push);

		for (int i = 0; i < grid_slice_values.size(); i++)
		{
			// System.out.println("Slice.java, 143: " +
			// "Going to add values to grid " + grid_slice_values);
			axis_grid[i][current_slice] = grid_slice_values.get(i);
		}

		grid_slice_values.remove(push);
	}

	public void addValue(String s)
	{
		values.add(s);
	}

	public void addValue(Vector<String> value_list)
	{
		if (value_list == null)
		{
			System.out.println("Slice.java, 218: " + "Error: value list is null");
		} else if (value_list.size() == 0)
		{
			System.out.println("Slice.java, 222: " + "Error: value list size is 0");
		} else if (value_list.size() == 1)
		{
			addValue(value_list.get(0));
		} else
		{
			// Go thru sub_slice, see which one to send the value list to.
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);

				if (slice.getValue().equals(value_list.firstElement()))
				{
					value_list.remove(0);
					slice.addValue(value_list);
					return;
				}
			}
		}
	}

	public void sort(int slice_idx)
	{
		// System.out.println("Slice.java, 168: " +
		// "Entered slice sort for slice_idx " + slice_idx);
		if (slice_idx == 0)
		{
			Iterator<String> it = values.iterator();
			while (it.hasNext())
			{
				sub_slice.add(new Slice(it.next()));
			}
		} else
		{
			slice_idx--;
			// Now descend into the tree and set/sort each sub-slice.
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				slice.sort(slice_idx);
			}
		}
		// System.out.println("Slice.java, 201: " + "Exiting slice sort");
	}

	public Vector<String> getSliceValues(int slice_idx)
	{
		if (slice_idx == 0)
		{
			Vector<String> vals = new Vector<String>();
			Iterator<String> it = values.iterator();
			while (it.hasNext())
			{
				vals.add(it.next());
			}
			return vals;
		} else
		{
			// Descend into each slice and get values, then append.
			Vector<String> slice_vals_as_row = new Vector<String>();

			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				slice_vals_as_row.addAll(slice.getSliceValues(slice_idx - 1));
			}
			return slice_vals_as_row;
		}
	}

	/**
	 * This method returns the slice element parts, giving access to the span
	 * info etc.
	 */
	public Vector<Slice> getSliceElements(int slice_idx)
	{
		if (slice_idx == 0)
		{
			return sub_slice;
		} else
		{
			// Descend into each slice and get values, then append.
			Vector<Slice> slice_vals_as_row = new Vector<Slice>();

			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				slice_vals_as_row.addAll(slice.getSliceElements(slice_idx - 1));
			}
			return slice_vals_as_row;
		}
	}

	public int getSpan()
	{
		if (sub_slice.size() == 0)
		{
			return 1;
		} else
		{
			int slices_span = 0;
			for (int i = 0; i < sub_slice.size(); i++)
			{
				Slice slice = sub_slice.get(i);
				slices_span = slices_span + slice.getSpan();
			}
			return slices_span;
		}
	}

}
