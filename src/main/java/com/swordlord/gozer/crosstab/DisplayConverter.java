package com.swordlord.gozer.crosstab;

import java.util.ArrayList;
import java.util.List;

import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * Helper class to take a jCrosstabResultSet object and format the data into a
 * cleaner display.
 */

public class DisplayConverter
{
	String[][] hor;
	String[][] vert;
	String[][] data_grid;

	public String getTabDelimitedTable(AccumulatorDefinition ad, boolean measures_on_row, int vert_axis_slice_size)// jCrosstabResultSet
	// jxrs)
	{
		StringBuffer str = new StringBuffer();

		// Headers
		for (int i = 0; i < hor.length; i++)
		{
			// These are the spacers that go above the vertical axis.
			if (ad.getMeasureCount() == 1)
			{
				// if the horizontal axis has more than one slice, put the
				// measure name only on the first row.
				if (i == 0)
				{
					str.append(ad.get(0).getName() + "\t");
					for (int x = 0; x < vert_axis_slice_size - 1; x++)
						str.append('\t');
				} else
				{
					for (int x = 0; x < vert_axis_slice_size; x++)
						str.append('\t');
				}
			} else if (measures_on_row) // && (ad.measure_definitions.size() >
			// 1))
			{
				for (int x = 0; x <= vert_axis_slice_size; x++)
					str.append('\t');

				if (ad.isMultiMeasure() && ad.isMultiMetric())
					str.append('\t');
			} else
			{
				for (int x = 0; x < vert_axis_slice_size; x++)
					str.append('\t');
			}

			// This is the actual axis-slice values.
			for (int j = 0; j < hor[i].length; j++)
			{
				str.append(hor[i][j].trim() + "\t");
			}
			str.append('\n');
		}

		for (int i = 0; i < vert.length; i++)
		{
			for (int j = 0; j < vert[i].length; j++)
			{
				str.append(vert[i][j].trim() + "\t");
			}

			for (int k = 0; k < data_grid[i].length; k++)
			{
				str.append(data_grid[i][k].trim() + "\t");
			}
			str.append('\n');
		}

		str.append('\n');

		return str.toString();
	}

	// The HTML methods are designed to work with CSS from
	// http://icant.co.uk/csstablegallery/

	public String getHtmlTable(AccumulatorDefinition ad, boolean measures_on_row, int vert_axis_slice_size, int hor_axis_slice_size)
	{
		return getHtmlTable(ad, measures_on_row, false, vert_axis_slice_size, hor_axis_slice_size);
	}

	public String getHtmlTable(AccumulatorDefinition ad, boolean measures_on_row, boolean write_entire_page, int vert_axis_slice_size, int hor_axis_slice_size)
	{
		StringBuffer str = new StringBuffer();

		if (write_entire_page)
		{
			str.append("<html>\n");
			str.append("<head>\n");
			str.append("\t<link rel=stylesheet href=\"../app.css\" type=\"text/css\">\n");
			str.append("</head>\n");
			str.append("<body>\n");
		}

		str.append("<table>");

		// Headers
		// This is the "spacer" above the vertical axis columns.
		for (int i = 0; i < hor.length; i++)
		{
			str.append("<tr>");

			if (i == 0)
			{
				if (ad.getMeasureMetricColumnCount() == 1)
				{
					MeasureDefinition md = ad.get(0);
					str.append("<th colspan=" + vert_axis_slice_size + " rowspan=" + hor_axis_slice_size + ">" + md.getName() + "\n");
				} else if (measures_on_row)
				{
					// Multiple measures on row
					if (ad.getMeasureCount() == 1)
						str.append("<th colspan=" + (vert_axis_slice_size + 1) + " rowspan=" + hor_axis_slice_size + ">" + ad.get(0).getName() + "\n");
					else
					{
						if (ad.isMultiMetric() && ad.isMultiMeasure())
							str.append("<th colspan=" + (vert_axis_slice_size + 2) + " rowspan=" + hor_axis_slice_size + ">\n");
						else
							str.append("<th colspan=" + (vert_axis_slice_size + 1) + " rowspan=" + hor_axis_slice_size + ">\n");
					}
				} else
				{
					// Add one: multiple measures on columns
					if (ad.getMeasureCount() == 1)
						str.append("<th colspan=" + vert_axis_slice_size + " rowspan=" + (hor_axis_slice_size + 1) + ">" + ad.get(0).getName() + "\n");
					else
					{
						if (ad.isMultiMeasure() && ad.isMultiMetric())
							str.append("<th colspan=" + vert_axis_slice_size + " rowspan=" + (hor_axis_slice_size + 2) + ">\n");
						else
							str.append("<th colspan=" + vert_axis_slice_size + " rowspan=" + (hor_axis_slice_size + 1) + ">\n");
					}
				}
			}

			for (int j = 0; j < hor[i].length; j++)
			{
				str.append("<th ");
				// Look ahead.
				int colspan = 1;
				while ((j < hor[i].length - 1) && (hor[i][j].equals(hor[i][j + 1])))
				{
					j++;
					colspan++;
				}

				if (colspan > 1)
					str.append(" colspan=" + colspan);

				str.append(" nowrap>");

				str.append(hor[i][j].trim());
			}
		}

		int[] skip_cells = new int[]
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		boolean odd_row = true;
		String odd_even_row = "odd";

		for (int i = 0; i < vert.length; i++)
		{
			if (odd_row)
				odd_even_row = "odd";
			else
				odd_even_row = "";

			str.append("<tr class=\"" + odd_even_row + "\">");

			for (int j = 0; j < vert[i].length; j++)
			{
				if (skip_cells[j] > 1)
				{
					skip_cells[j]--;
				} else
				{
					str.append("<th nowrap ");

					int x = i;

					while ((x < vert.length - 1) && (vert[x][j].equals(vert[x + 1][j])))
					{
						x++;
						skip_cells[j]++;
					}

					str.append(" rowspan=" + skip_cells[j] + " ");

					str.append(">" + vert[i][j].trim());
				}
			}

			for (int l = 0; l < data_grid[i].length; l++)
			{
				if (data_grid[i][l].equals(""))
					str.append("<td></td>\n");
				else
				{
					str.append("<td>" + data_grid[i][l] + "</td>\n");
				}
			}

			odd_row = !odd_row;
		}

		str.append("</table>\n");

		if (write_entire_page)
		{
			str.append("</body>\n");
			str.append("</html>\n");
		}

		return str.toString();
	}

	public String writeDataGrid()
	{
		StringBuffer str = new StringBuffer();

		for (int row = 0; row < data_grid.length; row++)
		{
			for (int col = 0; col < data_grid[row].length; col++)
			{
				str.append(data_grid[row][col] + " ");
			}
			str.append('\n');
		}

		return str.toString();
	}

	public void sortOnColumn(int col_idx)
	{
		sortOnColumn(true, col_idx);
	}

	public void sortOnColumn(boolean sort_ascending, int col_idx)
	{
		boolean made_change = true;

		while (made_change)
		{
			made_change = false;
			for (int i = 0; i < data_grid.length - 1; i++)
			{
				boolean flip = false;

				if (data_grid[i][col_idx].equals("") || data_grid[i + 1][col_idx].equals(""))
				{
					// At least one is Zero Length String
					if (data_grid[i][col_idx].equals("") && data_grid[i + 1][col_idx].equals(""))
					{
						// They're BOTH Zero Length Strings
						// do nothing
					} else
					{
						// One is a number, one is ZLS
						if ((sort_ascending && !data_grid[i][col_idx].equals("") && data_grid[i + 1][col_idx].equals(""))
								|| (!sort_ascending && data_grid[i][col_idx].equals("") && !data_grid[i + 1][col_idx].equals("")))
						{
							flip = true;
						}
					}
				} else
				{
					// both are numbers
					if ((sort_ascending && (Float.parseFloat(data_grid[i][col_idx]) > Float.parseFloat(data_grid[i + 1][col_idx])))
							|| (!sort_ascending && (Float.parseFloat(data_grid[i][col_idx]) < Float.parseFloat(data_grid[i + 1][col_idx]))))
					{
						flip = true;
					} else
					{
						// Do nothing?
					}
				}

				if (flip)
				{
					String[] temp = data_grid[i];
					data_grid[i] = data_grid[i + 1];
					data_grid[i + 1] = temp;

					// Have to sort the vertical Axis too.
					String[] stemp = vert[i];
					vert[i] = vert[i + 1];
					vert[i + 1] = stemp;

					made_change = true;
				}
			}
		}
	}

	public void sortOnRow(int row_idx)
	{
		sortOnRow(true, row_idx);
	}

	public void sortOnRow(boolean sort_ascending, int row_idx)
	{
		boolean made_change = true;

		while (made_change)
		{
			made_change = false;

			for (int j = 0; j < data_grid[row_idx].length - 1; j++)
			{
				boolean flip = false;

				if (data_grid[row_idx][j].equals("") || data_grid[row_idx][j + 1].equals(""))
				{
					// At least one is Zero Length String
					if (data_grid[row_idx][j].equals("") && data_grid[row_idx][j + 1].equals(""))
					{
						// They're BOTH Zero Length Strings
						// do nothing
					} else
					{
						// One is a number, one is ZLS
						if ((sort_ascending && !data_grid[row_idx][j].equals("") && data_grid[row_idx][j + 1].equals(""))
								|| (!sort_ascending && data_grid[row_idx][j].equals("") && !data_grid[row_idx][j + 1].equals("")))
						{
							flip = true;
						}
					}
				} else
				{
					// both are numbers
					if ((sort_ascending && (Float.parseFloat(data_grid[row_idx][j]) > Float.parseFloat(data_grid[row_idx][j + 1])))
							|| (!sort_ascending && (Float.parseFloat(data_grid[row_idx][j]) < Float.parseFloat(data_grid[row_idx][j + 1]))))
					{
						flip = true;
					} else
					{
						// Do nothing?
					}
				}

				if (flip)
				{
					// Flip the horizontal axis values
					for (int ii = 0; ii < hor.length; ii++)
					{
						String stemp = hor[ii][j];
						hor[ii][j] = hor[ii][j + 1];
						hor[ii][j + 1] = stemp;
					}

					for (int ii = 0; ii < data_grid.length; ii++)
					{
						String temp = data_grid[ii][j];
						data_grid[ii][j] = data_grid[ii][j + 1];
						data_grid[ii][j + 1] = temp;
					}

					made_change = true;
				}
			}

		}
	}

	public List<DataRowBase> getDataRowBase(AccumulatorDefinition ad, boolean measures_on_row, int vert_axis_slice_size, int horz_size)
	{
		List<DataRowBase> list = new ArrayList<DataRowBase>();

		// Headers
		String[] columns = new String[horz_size + vert_axis_slice_size];
		for (int i = 0; i < hor.length; i++)
		{
			int colId = 0;

			// These are the spacers that go above the vertical axis.
			if (ad.getMeasureCount() == 1)
			{
				// if the horizontal axis has more than one slice, put the
				// measure name only on the first row.
				if (i == 0)
				{
					columns[colId] = ad.get(0).getName();
					colId++;
					for (int x = 0; x < vert_axis_slice_size - 1; x++)
					{
						columns[colId] = "";
						colId++;
					}
				} else
				{
					for (int x = 0; x < vert_axis_slice_size; x++)
						columns[colId] = "";
					colId++;
				}
			} else if (measures_on_row)
			{
				for (int x = 0; x <= vert_axis_slice_size; x++)
				{
					columns[colId] = "";
					colId++;

				}

				if (ad.isMultiMeasure() && ad.isMultiMetric())
				{
					columns[colId] = "";
					colId++;
				}
			} else
			{
				for (int x = 0; x < vert_axis_slice_size; x++)
				{
					columns[colId] = "";
					colId++;

				}
			}

			// This is the actual axis-slice values.
			for (int j = 0; j < hor[i].length; j++)
			{
				columns[colId] = hor[i][j].trim();
				colId++;
			}

			// No Header
			// DataRowBase dataRowBase = new DataRowBase();
			// for (int k = 0; k < columns.length; k++)
			// {
			// dataRowBase.setProperty("Col" + k, columns[k]);
			// }
			// list.add(dataRowBase);
		}

		for (int i = 0; i < vert.length; i++)
		{
			DataRowBase dataRowBase = new DataRowBase();
			int colId = 0;
			for (int j = 0; j < vert[i].length; j++)
			{
				dataRowBase.setProperty("Col" + colId, vert[i][j].trim());
				colId++;
			}

			for (int k = 0; k < data_grid[i].length; k++)
			{
				dataRowBase.setProperty("Col" + colId, data_grid[i][k].trim());
				colId++;
			}
			list.add(dataRowBase);
		}

		return list;
	}

}
