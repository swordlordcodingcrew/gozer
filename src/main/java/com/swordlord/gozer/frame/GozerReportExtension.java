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
 ** $Id: GozerReportExtension.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.frame;

import java.io.Serializable;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.components.generic.report.GQueries;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.eventhandler.generic.GozerActionEvent;
import com.swordlord.jalapeno.datarow.DataRowBase;


@SuppressWarnings("serial")
public abstract class GozerReportExtension implements IGozerFrameExtension, Serializable
{
	protected static final Log LOG = LogFactory.getLog(GozerReportExtension.class);
	
	private GQueries _queries;
	
	public GQueries getQueries()
	{
		return _queries;
	}

	public void setQueries(GQueries queries)
	{
		this._queries = queries;
	}

	public boolean isRepository()
	{
		return false;
	}

	// Manage states
	public final boolean canDelete()
	{
		return false;
	}

	public final boolean canDetail()
	{
		return false;
	}

	public ImageIcon getIcon()
	{
		return null;
	}

	public final boolean canEdit()
	{
		return false;
	}

	public final boolean canNew()
	{
		return false;
	}

	public final boolean canPersist()
	{
		return false;
	}

	public final boolean canClickable()
	{
		return true;
	}
	
	public final boolean canPDF()
	{
		return true;
	}
	
	public final boolean canCSV()
	{
		return true;
	}

	/**
	 * pipes the action to the BO or handles it itself
	 * 
	 * @param actionID
	 *            the action id. Corresponds to the object-tag
	 */
	public final void genericActionPerformed(String actionID)
	{
	}

	public final void onActivation(DataBinding dataBinding, IGozerAction source)
	{
		LOG.error("The function onActivation() is not allowed.");
	}

	public final void onCancel()
	{
		LOG.error("The function onCancel() is not allowed.");
	}

	public final void onDelete(DataBindingMember dbMember)
	{
		LOG.error("The function onDelete() is not allowed.");
	}

	public final Page onDetail(DataRowBase row)
	{
		LOG.error("The function onDetail() is not allowed.");
		return null;
	}

	public final void onEdit()
	{
		LOG.error("The function onEdit() is not allowed.");
	}

	public final void onNew(DataBindingMember dbMember)
	{
		LOG.error("The function onNew() is not allowed.");
	}

	public final void onPostPersist()
	{
		LOG.error("The function onPersist() is not allowed.");
	}

	public final void onPrePersist()
	{
		LOG.error("The function onPrePersist() is not allowed.");
	}
	
	public void onUnhandledEvent(GozerActionEvent event) 
	{
		// TODO Auto-generated method stub	
	}
}
