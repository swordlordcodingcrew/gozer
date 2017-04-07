/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 ** and individual authors
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
 ** $Id: BLAFDrawingSupplier.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.generic.graph;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.plot.DefaultDrawingSupplier;

@SuppressWarnings("serial")
public class BLAFDrawingSupplier extends DefaultDrawingSupplier
{
	// These are the ORACLE Default Graph Colours
	public static Paint[] BLAF_PAINT_SEQUENCE = new Paint[]
					{ 
						new Color(0x336699),
						new Color(0x99CCFF),
						new Color(0x999933),
						new Color(0x666699),
						new Color(0xCC9933),
						new Color(0x006666),
						new Color(0x3399FF),
						new Color(0x993300),
						new Color(0xCCCC99),
						new Color(0x666666),
						new Color(0xFFCC66),
						new Color(0x6699CC),
						new Color(0x663366),
						new Color(0x9999CC),
						new Color(0xCCCCCC),
						new Color(0x669999),
						new Color(0xCCCC66),
						new Color(0xCC6600),
						new Color(0x9999FF),
						new Color(0x0066CC),
						new Color(0x99CCCC),
						new Color(0x999999),
						new Color(0xFFCC00),
						new Color(0x009999),
						new Color(0x99CC33),
						new Color(0xFF9900),
						new Color(0x999966),
						new Color(0x66CCCC),
						new Color(0x339966),
						new Color(0xCCCC33)
					};
	
	public BLAFDrawingSupplier()
	{
		super(
				BLAF_PAINT_SEQUENCE,
				DEFAULT_FILL_PAINT_SEQUENCE,
				DEFAULT_OUTLINE_PAINT_SEQUENCE, 
				DEFAULT_STROKE_SEQUENCE,
				DEFAULT_OUTLINE_STROKE_SEQUENCE,
				DEFAULT_SHAPE_SEQUENCE
		);
	}
}	
	
	
	
	


