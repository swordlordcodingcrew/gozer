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
 ** $Id: GFopList.java 1291 2011-12-12 19:25:11Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.fop;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.fop.FopTemplateManager;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.OrderingParam;


@SuppressWarnings("serial")
public class GFopList extends GFopObjectBase
{
	protected static final Log LOG = LogFactory.getLog(GFopList.class);
	
	public static final String TEMPLATE_TABLE_NAME = "fop_list_table";
	public static final String TEMPLATE_COLUMN_NAME = "fop_list_column";
	public static final String TEMPLATE_COLUMN_CELL_NAME = "fop_list_column_cell";
	public static final String TEMPLATE_ROW_NAME = "fop_list_row";
	public static final String TEMPLATE_ROW_CELL_NAME = "fop_list_row_cell";

	private GList _obList;

	public GFopList(IGozerFrameExtension gfe, GList obList)
	{
		super(gfe);

		_obList = obList;
	}
	
	@Override
	public String toString()
	{
        final FopTemplateManager fopTM = FopTemplateManager.instance();
		
		final String strTable = fopTM.getTemplate(TEMPLATE_TABLE_NAME);
        final String strColumn = fopTM.getTemplate(TEMPLATE_COLUMN_NAME);
        final String strColumnCell = fopTM.getTemplate(TEMPLATE_COLUMN_CELL_NAME);
        final String strRow = fopTM.getTemplate(TEMPLATE_ROW_NAME);
        final String strRowCell = fopTM.getTemplate(TEMPLATE_ROW_CELL_NAME);
        final StringBuilder sbList = new StringBuilder();

		String rows = generateRows(strRow, strRowCell);
		if(rows != null)
		{
			String strRenderedTable = strTable;	

			StringBuilder sbColumn = new StringBuilder();
			StringBuilder sbHeader = new StringBuilder();

			// make string builders ready
			fillColumnAndHeader(sbColumn, sbHeader, strColumn, strColumnCell);
	
			strRenderedTable = strRenderedTable.replace("<columns/>", sbColumn.toString()); 
			strRenderedTable = strRenderedTable.replace("<headers/>", sbHeader.toString()); 			
			strRenderedTable = strRenderedTable.replace("<rows/>", rows); 
			
			sbList.append(strRenderedTable);
		}

		return sbList.toString();
	}
	
	private void fillColumnAndHeader(StringBuilder sbColumn, StringBuilder sbHeader, String strColumn, String strColumnCell)
	{
		for (final ObjectBase child : _obList.getChildren())
		{
			int nLength = -1;
			boolean bFieldTypeOK = false;
			if (child.getClass().equals(GField.class))
			{
				DataBinding dataBindingChild = child.getDataBinding();
				nLength = dataBindingChild.getDataBindingField().getMaxLength();

				bFieldTypeOK = true;
			} 
			else if (child.getClass().equals(GCodeField.class))
			{
				nLength = 70;

				bFieldTypeOK = true;
			}
			
			if(bFieldTypeOK)
			{
				sbColumn.append(strColumn);

				final String caption = child.getCaption();
				String strRenderedColumnCell = strColumnCell;
				
				if(strRenderedColumnCell == null) 
				{
					LOG.error("strColumnCell is empty.");
				}
				else
				{
					// TODO: magic, change this line somewhen!
					String strLength = nLength <= 200 ? "70pt" : "150pt";
					
					strRenderedColumnCell = strRenderedColumnCell.replace("<caption/>", caption == null ? "" : caption); 
					strRenderedColumnCell = strRenderedColumnCell.replace("$$WIDTH$$", strLength); 
					
					sbHeader.append(strRenderedColumnCell);
				}
			}
		}
	}

	private String generateRows(String strRow, String strRowCell)
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
		} else
		{
			subList = dbManager.getRows(dbMember);
		}

		if ((subList == null) || (subList.size() == 0))
		{
			return null;
		}
		
		StringBuilder sbRows = new StringBuilder();

		Iterator<DataRowBase> it = subList.iterator();
		while (it.hasNext())
		{
			StringBuilder sbCells = new StringBuilder();

			DataRowBase row = it.next();
			
			for (final ObjectBase child : _obList.getChildren())
			{
				Class<? extends ObjectBase> clazz = child.getClass();
				if (clazz.equals(GField.class) || clazz.equals(GCodeField.class))
				{
					// get cell data
					DataBinding dataBindingChild = child.getDataBinding();
					Object o = dataBindingChild.getFormattedValue(row);
					
					String strContent = o == null ? "" : o.toString();
					strContent = StringEscapeUtils.escapeHtml(strContent);
					
					// append data to cell template
					String strRenderedCell = strRowCell;
					if(strRenderedCell != null && strRenderedCell.length() > 0)
					{
						strRenderedCell = strRenderedCell.replace("<content/>", strContent);
						sbCells.append(strRenderedCell);
					}
					else
					{
						LOG.error("There is something wrong with the strRowCell in GFopList. Template is null.");
					}
				}
			}
			
			// now append the cells to the row
			String strRenderedList = strRow; 
			if(strRenderedList != null && strRenderedList.length() > 0)
			{
				strRenderedList = strRenderedList.replace("<child/>", sbCells.toString());
				sbRows.append(strRenderedList);
			}
			else
			{
				LOG.error("There is something wrong with the strRow in GFopList. Template is null.");
			}
		}
		
		return sbRows.toString();
	}
}
