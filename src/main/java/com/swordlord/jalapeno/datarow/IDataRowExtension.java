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

package com.swordlord.jalapeno.datarow;

public interface IDataRowExtension
{
	//public void fieldChanged(CDBCommon object, String strFieldName, Object oOldVal, Object oNewVal);
	
	/*
	 * 
	PostAdd 	Within "ObjectContext.newObject()" after ObjectId and ObjectContext are set.
	PrePersist  Within "ObjectContext.newObject()" after ObjectId and ObjectContext are set.
	PreRemove  	Before an object is deleted inside "ObjectContext.deleteObject()"; also includes all objects that will be deleted as a result of CASCADE delete rule.
	PreUpdate  	Prior to commit (and prior to "validateFor*") within "ObjectContext.commitChanges()" and "ObjectContext.commitChangesToParent()"
	PostPersist Within "ObjectContext.commitChanges()", after commit of a new object is done.
	PostRemove  Within "ObjectContext.commitChanges()", after commit of a deleted object is done.
	PostUpdate  Within "ObjectContext.commitChanges()", after commit of a modified object is done.
	PostLoad	Within "ObjectContext.performQuery()" after the object is fetched.
				Within "ObjectContext.rollbackChanges()" after the object is reverted.
				Anytime a faulted object is resolved (i.e. if a relationship is fetched.
	*/
	
	public void postAdd();
	public void prePersist();
	public void preRemove();
	public void preUpdate();
	public void postPersist();
	public void postRemove();
	public void postUpdate();
	public void postLoad();
	
	// Was dropTarget
	public void setRelation(DataRowBase droppedRecord, DataRowBase targetRecord);
}