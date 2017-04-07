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
 ** $Id:  $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.ui.gozerframe;

import java.util.Iterator;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.report.GQueries;
import com.swordlord.gozer.components.wicket.box.GWFrame;
import com.swordlord.gozer.eventhandler.generic.GozerDisplayMode;
import com.swordlord.gozer.frame.GozerReportExtension;

@SuppressWarnings("serial")
public class GWReportContext extends GWContext
{
	public GWReportContext(GWFrame formPanel, GozerReportExtension gfe, GozerDisplayMode displayMode, ObjectTree objectTree)
	{
		super(formPanel, gfe);

		try
		{
			Iterator<ObjectBase> it = objectTree.getRoot().getChildren().iterator();
			while (it.hasNext())
			{
				ObjectBase child = it.next();
	
				if (child instanceof GQueries)
				{
					gfe.setQueries((GQueries) child);
					break;
				}
			}
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage());
		}

		setDisplayMode(displayMode);
	}
}
