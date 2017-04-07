/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
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
 ** $Id: GCsvList.java 1291 2011-12-12 19:25:11Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.csv;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.OrderingParam;

@SuppressWarnings("serial")
public class GCsvList extends GCsvObjectBase
{
	private GList _obList;

	public GCsvList(IGozerFrameExtension gfe, GList obList)
	{
		super(gfe);

		_obList = obList;
	}

	private void createRows(Sheet sheet)
	{
		DataBindingMember dbMember = _obList.getDataBindingMember();
		DataBindingContext dbc = getFrameExtension().getDataBindingContext();
		DataBindingManager dbManager = dbc.getDataBindingManager(dbMember);

		List<DataRowBase> subList = null;

		String strOrdering = _obList.getOrdering();
		if ((strOrdering != null) && (strOrdering.length() > 0))
		{
            OrderingParam orderingParam = new OrderingParam(strOrdering, true, false);
			subList = dbManager.getRows(dbMember, orderingParam);
		} 
		else
		{
			subList = dbManager.getRows(dbMember);
		}

		if ((subList == null) || (subList.size() == 0))
		{
			return;
		}

		int nColumns = 0;

		LinkedList<ObjectBase> children = _obList.getChildren();
		if (children.size() > 0)
		{
			// TODO fix this hack
			nColumns = children.size();
		}

		Iterator<DataRowBase> it = subList.iterator();
		while (it.hasNext())
		{
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);

			DataRowBase dataRow = it.next();

			for (int i = 0; i < nColumns; i++)
			{
				final ObjectBase child = children.get(i);
				
				Cell cell = row.createCell(i);

				if ((child.getClass().equals(GField.class)) || (child.getClass().equals(GCodeField.class)))
				{
					DataBinding dataBindingChild = child.getDataBinding();
					
					Object oValue = dataBindingChild.getFormattedValue(dataRow);
					String strValue = oValue == null ? "" : oValue.toString();
					
					cell.setCellValue(strValue);
				}
			}
		}
	}

	private void createTableColumn(Sheet sheet)
	{
		LinkedList<ObjectBase> children = _obList.getChildren();
		
		Row row = sheet.createRow(0);

		int nSize = children.size();

		for (int i = 0; i < nSize; i++)
		{
			ObjectBase child = children.get(i);

			Cell cell = row.createCell(i);
			cell.setCellValue(child.getCaption());
		}
	}

	@Override
	public void renderToWorkbook(Workbook wb)
	{
		Sheet sheet = null;
		String caption = _obList.getCaption();
		
		if(caption == null)
		{
			sheet = wb.createSheet();
		}
		else
		{
			if(wb.getSheet(caption) == null)
			{
				sheet = wb.createSheet(caption);
			}
			else
			{
				sheet = wb.createSheet(MessageFormat.format("{0} {1}", caption, wb.getNumberOfSheets()));
			}
		}
		
		createTableColumn(sheet);
		createRows(sheet);
	}
}
