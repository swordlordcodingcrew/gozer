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
** $Id: GWPanel.java 1361 2012-04-15 11:04:14Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket;

import java.text.MessageFormat;

import com.swordlord.common.i18n.ITranslator;
import com.swordlord.common.i18n.TranslatorFactory;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.ui.gozerframe.GWContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.box.GHBox;

@SuppressWarnings("serial")
public abstract class GWPanel extends Panel implements IWicketComponent
{
	protected static final Log LOG = LogFactory.getLog(GWPanel.class);
	protected ITranslator _tr = TranslatorFactory.getTranslator();

	public GWPanel(String id, IModel<?> model)
	{
		super(id, model);
        
        // Needed to receive (and update to) ajax events
		setOutputMarkupId(true);
	}
	
	protected boolean hasVBoxAsParent(ObjectBase ob)
	{
		while(ob.hasParent())
		{
			ObjectBase parent = ob.getParent();
			if (parent.getClass().equals(GHBox.class))
			{
				return true;
			}
			
			ob = parent;
		}		
		
		return false;
	}

	protected IGozerFrameExtension getFrameExtension()
	{
		return getGWContext().getFrameExtension();
	}

	public GWContext getGWContext()
	{
		return (GWContext) getModelObject();
	}
	
	protected Object getModelObject()
	{
		return getDefaultModelObject();
	}
	
	protected IModel<?> getModel()
	{
		return getDefaultModel();
	}
	
	protected void setModel(IModel<?> model)
	{
		setDefaultModel(model);
	}
	
	protected String translateCaption(String tableName, String caption) 
	{
		String strPath = "gozer.caption." + tableName.toLowerCase();
		String strKey = caption.toLowerCase();
		
		LOG.info(MessageFormat.format("Loading caption from translation table. Path: {0} Key: {1}", strPath, strKey));
		
		String translation = _tr.getString(strPath, strKey);
		
		return translation == null ? caption : translation;
	}
}
