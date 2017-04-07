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
** $Id: LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.frame;

import java.util.UUID;

import javax.swing.ImageIcon;

import org.apache.wicket.Page;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.eventhandler.generic.GozerActionEvent;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;
import com.swordlord.repository.businessobject.BusinessObjectBase;
import com.swordlord.sobf.wicket.ui.page.RootPage;

public interface IGozerFrameExtension
{
	// Manage states
	public boolean canDelete();
	public boolean canDetail();
	public boolean canEdit();
	public boolean canNew();
	public boolean canPersist();
	public boolean canClickable();
	
	public boolean canPDF();
	public boolean canCSV();
	
	public RootPage getPage();
	public void setPage(RootPage page);
	
	/**
	 * pipes the action to the BO or handles it itself
	 * @param actionID the action id. Corresponds to the object-tag
	 */
	public void genericActionPerformed(String actionID);

	public DataRowKeyBase getActiveRow(String boundTableName);

	public BusinessObjectBase getBusinessObject();
	public String getCaption();

	public DataBindingContext getDataBindingContext();
	public DataContainer getDataContainer();

	public UUID getFrameId();

	public GozerController getGozerController();

	public String getPermissionName();
	public String getGozerLayoutFileName();

	public UUID getHelpId();
	public ImageIcon getIcon();
	public String getName();
	public boolean isRepository();
	public void onActivation(DataBinding dataBinding, IGozerAction source);
	public void onCancel();
	public void onDelete(DataBindingMember dbMember);

	// TODO, remove Page so that things work with Swing as well!!
	public Page onDetail(DataRowBase row);
	public void onEdit();
	// Status changes
	public void onInit();
	public void onLoad();
	public void onNew(DataBindingMember dbMember);
	public void onPostPersist(GozerActionEvent event);

	public void onPrePersist();
	
	public void onUnhandledEvent(GozerActionEvent event);
}
