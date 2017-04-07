package com.swordlord.gozer.crosstab;

import java.util.*;

import com.swordlord.jalapeno.datarow.DataRowBase;

public class DataGrid
{
	private Accumulator[][] data_rows;
	private AccumulatorDefinition ad = new AccumulatorDefinition();

	private String[][] data_grid;

	// ------------------------------------------------------------
	// -- basic setters
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// -- getters
	// ------------------------------------------------------------

	public Accumulator[][] getDataRows()
	{
		return data_rows;
	}

	public String[][] getDataGrid()
	{
		return data_grid;
	}

	// ------------------------------------------------------------
	// -- initializers
	// ------------------------------------------------------------

	public void clear()
	{
		data_rows = null;
	}

	public void initialize(int vert, int horz)
	{
		data_rows = new Accumulator[vert][horz];
	}

	public void initializeGrid(int vert, int horz)
	{
		data_grid = new String[vert][horz];
	}

	// ------------------------------------------------------------
	// -- workhorse methods
	// ------------------------------------------------------------

	public void addData(DataRowBase row, int vIdx, int hIdx)
	{
		if (data_rows[vIdx][hIdx] == null)
			data_rows[vIdx][hIdx] = new Accumulator(ad);

		data_rows[vIdx][hIdx].add(row, ad);
	}

	public void setGrid(boolean measuresOnRow, int vertAxisLength)
	{
		if (ad.getMeasureMetricColumnCount() == 1)
		{
			// Single measure, single metric - the simplest and most common
			// case. Should work always.
			for (int row = 0; row < data_grid.length; row++)
			{
				for (int col = 0; col < data_grid[row].length; col++)
				{
					if (data_rows[row][col] == null)
					{
						data_grid[row][col] = "";
					} else
					{
						Measure m = data_rows[row][col].getMeasure(0);
						data_grid[row][col] = m.getMetricValue(0);
					}
				}
			}
		} else if (measuresOnRow) // multiple measures on row, or single
									// measure, multi-metric
		{
			/*
			 * Unlike the others, this algorithm traverses DOWN a column first
			 * across each ROW, then moves to the next column and proceeds DOWN
			 * that second column.
			 */
			int row = 0;

			for (int col = 0; col < data_rows[0].length; col++)
			{
				for (int data_row_index = 0; data_row_index < data_rows.length; data_row_index++)
				{
					for (int measure = 0; measure < ad.getMeasureCount(); measure++)
					{
						Measure m = data_rows[data_row_index][col].get(measure);
						for (int metric = 0; metric < m.getMetricCount(); metric++)
						{
							// TODO: chedk if data_rows[data_row_index][col] ==
							// null
							Metric met = m.get(metric);
							data_grid[row++][col] = met.get();
						}
					}
				}
				row = 0;
			}
		} else
		// multiple measures on column. OR single measure, multi-metric.
		{
			int data_grid_column = 0;
			for (int row = 0; row < data_grid.length; row++)
			{
				for (int col = 0; col < data_rows[row].length; col++)
				{
					if (data_rows[row][col] == null)
					{
						for (int measure = 0; measure < ad.getMeasureCount(); measure++)
						{
							MeasureDefinition md = ad.get(measure);
							for (int metric = 0; metric < md.getMetricCount(); metric++)
							{
								data_grid[row][data_grid_column] = "";
								data_grid_column++;
							}
						}
					} else
					{
						for (int measure = 0; measure < ad.getMeasureCount(); measure++)
						{
							MeasureDefinition md = ad.get(measure);
							Measure m = data_rows[row][col].get(measure);

							for (int metric = 0; metric < md.getMetricCount(); metric++)
							{
								Metric met = m.getMetric(metric);

								data_grid[row][data_grid_column] = met.get();
								data_grid_column++;
							}
						}
					}
				}
				data_grid_column = 0;
			}
		}

	}

	// ------------------------------------------------------------
	// -- pass thru to AccumulatorDefinition
	// ------------------------------------------------------------

	/**
	 * Reset the MeasureDefinition vector to a blank state (new Vector).
	 */
	public void setNewMeasureDefintions()
	{
		ad.setNewMeasureDefintions();
	}

	public AccumulatorDefinition getAccumulatorDefinition()
	{
		return ad;
	}

	public int getMeasureCount()
	{
		return ad.getMeasureCount();
	}

	public void addMeasureDefinition(MeasureDefinition md)
	{
		ad.add(md);
	}

	public Vector<MeasureDefinition> getMeasureDefinitions()
	{
		return ad.getMeasureDefinitions();
	}

	public MeasureDefinition getMeasureDefinition(int i)
	{
		return ad.get(i);
	}

	public int getMeasureMetricColumnCount()
	{
		return ad.getMeasureMetricColumnCount();
	}

	public AccumulatorDefinition getAD()
	{
		return getAccumulatorDefinition();
	}
}
