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
** $Id: WicketRenderer.java 1361 2012-04-15 11:04:14Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.wicket;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GFrame;
import com.swordlord.gozer.components.wicket.box.GWFrame;
import com.swordlord.gozer.renderer.generic.FrameRendererBase;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;

/**
 * @author LordEidi
 *
 */
public class WicketRenderer extends FrameRendererBase
{
	private GWContext _context;
	
    /**
     * @param ot
     * @param context
     */
	public WicketRenderer(ObjectTree ot, GWContext context)
	{
		super(ot, context.getFrameExtension());
		
		_context = context;
	}
	
	private Panel renderObject(ObjectBase ob, Panel parent)
	{
		/*
		boolean bChildren = true;
		Panel pnl = null;

		if(ob.getClass() == GActionBox.class)
		{
			pnl = createGWActionBox(ob);
			bChildren = false;
		}
		else if(ob.getClass() == GBox.class)
		{
			pnl = createGWBox(ob);
		}
		else if(ob.getClass() == GLabel.class)
		{
			pnl = createGJLabel(ob);
		}
		else if(ob.getClass() == GList.class)
		{
			pnl = createGJList(ob);
			bChildren = false;
		}
		else if(ob.getClass() == GDetail.class)
		{
			pnl = createGJDetail(ob);
			bChildren = false;
		}
		else if(ob.getClass() == GListAndDetail.class)
		{
			pnl = createGJListAndDetail(ob);
			bChildren = false;
		}
		else if(ob.getClass() == GImage.class)
		{
			System.out.println("the image tag is not implemented - will be done if necessary");
			pnl = new GJBox();
			pnl.setBorder(BorderFactory.createTitledBorder("Image"));
		}

		if (bChildren)
		{
			LinkedList<ObjectBase> children = ob.getChildren();
			for(ObjectBase obChild : children)
			{
				Panel childPnl = (Panel) renderObject(obChild, pnl);

				if(childPnl != null)
				{
					pnl.add(childPnl);
				}
			}
		}		

		return pnl;
		*/
		return null;
	}

	@Override
	public Panel renderTree()
	{
		return renderTree("gozerform");
	}
	
    /**
     * @param id
     * @return
     */
	public Panel renderTree(String id)
	{
		ObjectBase ob = getObjectTree().getRoot();

		// the first panel - should be a GFrame
		GWFrame gjf = null;
		if(ob.getClass().equals(GFrame.class))
		{
			Object objOr = ob.getAttribute("orientation");
			if ((objOr != null) && (objOr instanceof String) && objOr.toString().equalsIgnoreCase("h"))
			{
				gjf = new GWFrame(id, new Model<GWContext>(_context), ob);
			}
			else
			{
				gjf = new GWFrame(id, new Model<GWContext>(_context), ob);
			}

			// TODO: Think about a central renderer for wicket as we do have for fop
			//LinkedList<ObjectBase> children = ob.getChildren();
			//for(ObjectBase obChild : children)
			//{
				//Panel childPnl = (Panel) renderObject(obChild, gjf);
				//gjf.add(childPnl);
			//}

			return gjf;
		}

        return new EmptyPanel(id);
	}
}
