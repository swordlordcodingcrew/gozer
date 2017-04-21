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
package com.swordlord.gozer.components.wicket.page;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.ui.gozerframe.WicketGozerPanel;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class GozerPage extends RootPage
{
	protected WebMarkupContainer _content;

	public static GozerPage getFrame(IGozerFrameExtension gfe) throws RestartResponseAtInterceptPageException
	{
		GozerPage page = null;

		try
		{
			page = new GozerPage(gfe);
		}
		catch(RestartResponseAtInterceptPageException e)
		{
			LOG.error(e);
			throw e;			
		}
		catch(Exception e)
		{
			LOG.error(e);
		}

		return page;
	}

	public static GozerPage getFrame(IGozerFrameExtension gfe, Class<? extends GozerPage> clazz)
	{
		GozerPage page = null;

		try
		{
			Constructor<? extends GozerPage> c = clazz.getConstructor(new Class[] {IGozerFrameExtension.class});
	        page = c.newInstance(new Object[] {gfe});
		}
		catch(InvocationTargetException e)
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		} 
		catch (InstantiationException e) 
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		} 
		catch (SecurityException e) 
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e) 
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		}
		

		return page;
	}
	
	public static GozerPage getFrame(IGozerFrameExtension gfe, Class<? extends GozerPage> clazz, PageParameters params)
	{
		GozerPage page = null;

		try
		{
			Constructor<? extends GozerPage> c = clazz.getConstructor(new Class[] {IGozerFrameExtension.class, PageParameters.class});
	        page = c.newInstance(new Object[] {gfe, params});
		}
		catch(InvocationTargetException e)
		{
			LOG.error(e.getCause());
		}
		catch(Exception e)
		{
			LOG.error(e.getCause());
			e.printStackTrace();
		}

		return page;
	}

	protected UUID _uuidHelpContent;

	protected UUID _uuidFrameId;

	protected WicketGozerPanel _gp;
	
	protected GozerPage(IGozerFrameExtension gfe, PageParameters params)
	{
		super(params);
		
		constructor(gfe);
	}

	protected GozerPage(IGozerFrameExtension gfe)
	{
		super();

		constructor(gfe);
	}
	
	private void constructor(IGozerFrameExtension gfe)
	{
		if(gfe != null)
		{
			_uuidHelpContent = gfe.getHelpId();
			_uuidFrameId = gfe.getFrameId();

			_gp = new WicketGozerPanel("gozerpanel", gfe);

			_content.add(_gp);
			//add(new Label("dc", gfe.getDataContainer().dumpContentToString()));

			if(_gp.getIsLandscape())
			{
	    		//changePageOrientationToLandscape();

	    		/*
	    		* public void changePageOrientationToLandscape()
					{
						// HACK: needs some better solution
						//_content.add(new SimpleAttributeModifier("class", "one-col"));
						//_sidebar.setVisible(false);
					}
					*/
			}
			
			gfe.setPage(this);
		}
		else
		{
			_content.add(new EmptyPanel("gozerpanel"));
		}
	}

	protected boolean isPageLayoutLandscape()
	{
		return _gp == null ? false : _gp.getIsLandscape();
	}

	protected boolean isSidebarPanelVisible()
	{
		return _gp == null ? true : !_gp.getIsLandscape();
	}

	@Override
	protected void onBeforeRender()
	{
		if(_gp != null)
		{
			DataContainer dc = _gp.getDataContainer();
			if((dc != null) && dc.hasErrors())
			{
				for(String error : dc.getErrors())
				{
					//LOG.error(error);
					//this.error(error);
				}
			}
		}

		try
		{
			super.onBeforeRender();
		}
		catch(WicketRuntimeException e)
		{			
			Throwable cause = e.getCause();
			LOG.error(e.getMessage() + "with cause: " + cause.getMessage());
			cause.printStackTrace();
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage());
		}
	}
}
