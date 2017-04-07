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
** $Id: GFopDetail.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.fop;

import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.frame.IGozerFrameExtension;


@SuppressWarnings("serial")
public class GFopDetail extends GFopObjectBase
{
	public static final String TEMPLATE_NAME = "fop_detail";

	private GDetail _obDetail;

	public GFopDetail(IGozerFrameExtension gfe, GDetail obDetail)
	{
		super(gfe);

		_obDetail = obDetail;
	}

	/*
	private void appendRows(StringBuilder sb)
	{
		DataBindingMember dbMember = _obList.getDataBindingMember();
		DataBindingContext dbc = getFrameExtension().getDataBindingContext();
		DataBindingManager dbManager = dbc.getDataBindingManager(dbMember);

		List<DataRowBase> subList = null;

		String strOrdering = _obList.getOrdering();
		if((strOrdering != null) && (strOrdering.length() > 0))
		{
			OrderingParam orderingParam = new OrderingParam(strOrdering, true);
			subList = dbManager.getRows(dbMember, orderingParam);
		}
		else
		{
			subList = dbManager.getRows(dbMember);
		}

		Iterator<DataRowBase> it = subList.iterator();
		while(it.hasNext())
		{
			sb.append("<fo:table-row border-width=\"0.5pt\">");

			DataRowBase row = it.next();

			for (final ObjectBase child : _obList.getChildren())
			{
				if (child.getClass() == GField.class)
				{
					DataBinding dataBindingChild = child.getDataBinding();

					sb.append("<fo:table-cell>");
					sb.append("<fo:block text-align=\"left\">");
					sb.append(dataBindingChild.getFormattedValue(row));
			        sb.append("</fo:block>");
			        sb.append("</fo:table-cell>)");
				}
			}

			sb.append("</fo:table-row>");
		}
	}

	private void appendTableColumn(StringBuilder sbColumn, StringBuilder sbHeader)
	{
		for (final ObjectBase child : _obList.getChildren())
		{
			final String caption = child.getCaption();

			if (child.getClass() == GField.class)
			{
				sbColumn.append("<fo:table-column column-width=\"6cm\"/>");

				sbHeader.append("<fo:table-cell>");
				sbHeader.append("<fo:block font-weight=\"bold\" text-align=\"center\" ");
					sbHeader.append("vertical-align=\"middle\" border-width=\"1pt\" border-color=\"black\" ");
					sbHeader.append("background-color=\"#F7F24D\">");
				sbHeader.append(caption);
		        sbHeader.append("</fo:block>");
		        sbHeader.append("</fo:table-cell>)");
			}
		}
	}
	*/

	@Override
	public String toString()
	{
		/*
		StringBuilder sbList = new StringBuilder();
		StringBuilder sbColumn = new StringBuilder();
		StringBuilder sbHeader = new StringBuilder();

		sbList.append("<fo:table border-width=\"0.5pt\" border-color=\"red\">");

		sbHeader.append("<fo:table-header>");
		sbHeader.append("<fo:table-row>");

		appendTableColumn(sbColumn, sbHeader);

		sbHeader.append("</fo:table-row>");
		sbHeader.append("</fo:table-header>");

		sbList.append(sbColumn);
		sbList.append(sbHeader);

		sbList.append("<fo:table-body>");

		appendRows(sbList);

		sbList.append("</fo:table-body>");

		sbList.append("</fo:table>");

		return sbList.toString();
		*/

		return "";
	}
}
