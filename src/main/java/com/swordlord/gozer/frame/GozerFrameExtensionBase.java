/*-----------------------------------------------------------------------------
 **
 ** -Security Officers Best Friend-
 **
 ** Copyright 2004-09 by SOMAP.org - http://www.somap.org/
 **
 ** This program is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU General Public License as published by the Free
 ** Software Foundation, either version 3 of the License, or (at your option)
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful, but WITHOUT
 ** ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 ** FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 ** more details.
 **
 ** You should have received a copy of the GNU General Public License along
 ** with this program. If not, see <http://www.gnu.org/licenses/>.
 **
 **-----------------------------------------------------------------------------
 **
 ** $Id:  $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.frame;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.swing.ImageIcon;

import com.swordlord.common.i18n.Translator;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.databinding.FrameDataBindingContext;
import com.swordlord.gozer.eventhandler.generic.GozerActionEvent;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.jalapeno.businessobject.BusinessObjectBase;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;

@SuppressWarnings("serial")
public abstract class GozerFrameExtensionBase implements IGozerFrameExtension, Serializable
{
	private static final Log LOG = LogFactory.getLog(GozerFrameExtensionBase.class);
	
	private Translator _translator;
	
	private static String PERMISSION_PREFIX = "core:frame";
	protected static String TRANSLATION_PREFIX = "core.frame";
	
	protected BusinessObjectBase _bo;
	private GozerController _gc;
	private IGozerSessionInfo _sessionInfo;
	private FrameDataBindingContext _fdbc;
	
	protected boolean _bEnforceSecurity;
	
	private RootPage _rootPage;

	public GozerFrameExtensionBase(IGozerSessionInfo sessionInfo)
	{
		_sessionInfo = sessionInfo;
		_bEnforceSecurity = true;
	}
	
	protected Translator getTranslator()
	{
		if(_translator == null)
		{
			_translator = new Translator();
		}
		
		return _translator;
	}
	
	public String getPermissionName()
	{
		String permission = this.getClass().getName();
		permission = permission.toLowerCase();
		permission = permission.replace("org.somap.repository.gozerframe.", "");
		permission = permission.replace("org.somap.repository.reports.", "");
		permission = permission.replace("frameextension", "");
		permission = permission.replace(".", ":");
		
		return permission;
	}
	
	public String getTranslationName()
	{
		String permission = this.getClass().getName();
		permission = permission.toLowerCase();
		permission = permission.replace("org.somap.repository.gozerframe.", "");
		permission = permission.replace("org.somap.repository.reports.", "");
		permission = permission.replace("frameextension", "");
		
		return permission;
	}
	
	protected String formatPermission(String strAction)
	{
		String strFrame = this.getPermissionName();
		
		// example: core.frame.gap_analysis.compliancefe.edit
		String permission = MessageFormat.format("{0}:{1}:{2}", PERMISSION_PREFIX, strAction, strFrame);
		
		LOG.info("Checking permission: " + permission);
		
		return permission;
	}

	public boolean canDelete()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("delete"));
		}
		else
		{	
			return true;
		}
	}

	public boolean canDetail()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("detail"));
		}
		else
		{	
			return true;
		}
	}

	public boolean canEdit()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("edit"));
		}
		else
		{	
			return true;
		}
	}

	public boolean canNew()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("new"));
		}
		else
		{	
			return true;
		}
	}

	public boolean canPersist()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("persist"));
		}
		else
		{	
			return true;
		}
	}
	
	public boolean canClickable()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("clickable"));
		}
		else
		{	
			return true;
		}
	}
	
	public boolean canPDF()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("pdf"));
		}
		else
		{	
			return true;
		}
	}
	
	public boolean canCSV()
	{
		if(_bEnforceSecurity)
		{
			return _sessionInfo.isPermitted(formatPermission("csv"));
		}
		else
		{	
			return true;
		}
	}


	public void genericActionPerformed(String actionID)
	{
	}

	public DataRowKeyBase getActiveRow(String boundTableName)
	{
		return null;
	}

	// Get covariant return type by subclasses
	public BusinessObjectBase getBusinessObject()
	{
		return _bo;
	}

	final protected BusinessObjectBase getBusinessObjectInternal()
	{
		return _bo;
	}

	public String getCaption()
	{
		return getName();
	}

	public FrameDataBindingContext getDataBindingContext()
	{
		if (_fdbc == null)
		{
			_fdbc = new FrameDataBindingContext(this);
		}

		return _fdbc;
	}

	public DataContainer getDataContainer()
	{
		return getBusinessObject().getDC();
	}

	public GozerController getGozerController()
	{
		if (_gc == null)
		{
			_gc = new GozerController(this);
		}

		return _gc;
	}

	public IGozerSessionInfo getGozerSessionInfo()
	{
		return _sessionInfo;
	}

	public ImageIcon getIcon()
	{
		return Icons.getImage(Icons.ICON_BOX);
	}

	public boolean isBusinessObjectNull()
	{
		return _bo == null;
	}

	public boolean isRepository()
	{
		return true;
	}

	public void onActivation(DataBinding dataBinding, IGozerAction source)
	{
	}

	public void onCancel()
	{
	}

	public void onDelete(DataBindingMember dbMember)
	{
	}

	public Page onDetail(DataRowBase row)
	{
		return null;
	}

	public void onEdit()
	{
	}

	public void onInit()
	{
	}

	public void onLoad()
	{
	}

	public void onNew(DataBindingMember dbMember)
	{
	}

	public void onPostPersist(GozerActionEvent event)
	{
	}

	public void onPrePersist()
	{
	}
	
	public void onReplaceDetails(AjaxRequestTarget target, String key) {}
	
	public void onUnhandledEvent(GozerActionEvent event) {}

	final protected void setBusinessObjectInternal(BusinessObjectBase bo)
	{
		_bo = bo;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public RootPage getPage()
    {
        return _rootPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPage(RootPage page)
    {
        _rootPage = page;
    }

}
