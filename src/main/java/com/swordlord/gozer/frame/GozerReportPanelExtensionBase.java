/*-----------------------------------------------------------------------------
 **
 ** -Security Officers Best Friend-
 **
 ** Copyright 2004-08 by SOMAP.org - http://www.somap.org/
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
 ** $Id: GozerReportPanelExtensionBase.java 1170 2011-10-07 16:24:10Z ... $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.frame;

import com.swordlord.common.i18n.Translator;
import com.swordlord.common.prefs.UserPrefs;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.ReportDataBindingContext;
import com.swordlord.gozer.eventhandler.generic.ReportController;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;

@SuppressWarnings("serial")
public abstract class GozerReportPanelExtensionBase extends GozerReportExtension
{
	private ReportController _rc;
	private IGozerSessionInfo _sessionInfo;
	private ReportDataBindingContext _rdbc;
	private String _gozerLayoutFileName;
	private DataBindingContext _parentContext;
	private String _strGenericReportName;

	public GozerReportPanelExtensionBase(IGozerSessionInfo sessionInfo, String gozerLayoutFileName, DataBindingContext parentContext)
	{
		_sessionInfo = sessionInfo;
		_gozerLayoutFileName = gozerLayoutFileName;
		_parentContext = parentContext;
	}
	
	public String getPermissionName()
	{
		return this.getClass().getName().replace("org.somap.repository.gozerframe.", "").toLowerCase();
	}

	public DataRowKeyBase getActiveRow(String boundTableName)
	{
		return null;
	}
	
	private String getGenericReportName()
	{
		if(_strGenericReportName == null)
		{
			Translator t = new Translator();
			_strGenericReportName = t.getString("GenericReportName", "org.somap.sobf.reports");
		}
		return _strGenericReportName;
	}

	public String getCaption()
	{
		String strName = getName();
		return strName == null ? getGenericReportName() : strName;
	}
	
	public String getGozerLayoutFileName()
	{
		return UserPrefs.APP_REPORT_GOZER_FILES_FOLDER + _gozerLayoutFileName;
	}


	public ReportDataBindingContext getDataBindingContext()
	{
		if (_rdbc == null)
		{
			_rdbc = new ReportDataBindingContext(this);
		}

		return _rdbc;
	}
	
	public DataBindingContext getParentDataBindingContext()
	{
		return _parentContext;
	}

	public DataContainer getDataContainer()
	{
		return getBusinessObject().getDC();
	}

	public ReportController getGozerController()
	{
		if (_rc == null)
		{
			_rc = new ReportController(this);
		}

		return _rc;
	}

	public IGozerSessionInfo getGozerSessionInfo()
	{
		return _sessionInfo;
	}

	public void onInit()
	{
	}

	public void onLoad()
	{
	}

}
