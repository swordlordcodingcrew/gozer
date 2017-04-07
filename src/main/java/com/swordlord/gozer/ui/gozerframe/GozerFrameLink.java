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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.link.Link;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.sobf.wicket.ui.page.GozerPage;

@SuppressWarnings("serial")
public class GozerFrameLink extends Link<Object>
{
	protected static final Log LOG = LogFactory.getLog(WicketGozerPanel.class);

	protected Class<? extends IGozerFrameExtension> _gfe;
	
	private IGozerSessionInfo _sessionInfo;
	private Class<? extends GozerPage> _clazz;

	public GozerFrameLink(String strLinkName, Class<? extends IGozerFrameExtension> gfe, IGozerSessionInfo sessionInfo)
	{
		super(strLinkName);

		setGozerFrameExtension(gfe);
		_sessionInfo = sessionInfo;
	}

	public GozerFrameLink(String strLinkName, Class<? extends IGozerFrameExtension> gfe, IGozerSessionInfo sessionInfo, Class<? extends GozerPage> clazz)
	{
		super(strLinkName);

		setGozerFrameExtension(gfe);
		_sessionInfo = sessionInfo;
		_clazz = clazz;
	}
	
	/**
	 * @return the _gfe
	 */
	public Class<? extends IGozerFrameExtension> getGozerFrameExtension() 
	{
		return _gfe;
	}

	/**
	 * @param _gfe the _gfe to set
	 */
	public void setGozerFrameExtension(Class<? extends IGozerFrameExtension> gfe) 
	{
		_gfe = gfe;
	}
	
	public boolean hasGozerFrameExtension() 
	{
		return _gfe != null;
	}

	@Override
	public void onClick()
    {
		IGozerFrameExtension gfe = null;
		
		try
		{
			Class<? extends IGozerFrameExtension> clazz = getGozerFrameExtension();
			Constructor<? extends IGozerFrameExtension> c = clazz.getConstructor(new Class[] {IGozerSessionInfo.class});
	        gfe = c.newInstance(new Object[] {_sessionInfo});
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
		
    	GozerPage page = null;

    	if(_clazz != null)
    	{
    		page = GozerPage.getFrame(gfe, _clazz);
    	}
    	else
    	{
    		page = GozerPage.getFrame(gfe);
    	}

    	if(page != null)
		{
			setResponsePage(page);
		}
    	else
    	{
    		LOG.error("GozerFile does not exist, is null or empty: " + gfe.getGozerLayoutFileName());
    	}
    }
}