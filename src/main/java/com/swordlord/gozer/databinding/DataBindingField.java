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
** $Id: DataBindingField.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.databinding;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;

import com.swordlord.common.i18n.ITranslator;
import com.swordlord.common.i18n.TranslatorFactory;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import com.swordlord.gozer.components.EditMode;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.datatypeformat.DataTypeFormat;
import com.swordlord.jalapeno.DBConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class DataBindingField implements Serializable
{
	protected static final Log LOG = LogFactory.getLog(DataBindingField.class);

	protected int _nMaxLength = 0;
	protected DataTypeFormat _dataFormat;
	protected Class<?> _fieldType = Object.class;
	
	protected ObjEntity _objEntity;

	protected boolean _bIsMandatory;
	protected boolean _bIsVisible = true;
	protected boolean _bIsPrimaryKey;
	protected boolean _bIsForeignKey;
	protected boolean _bIsAttributeVirtual = false;

	protected Object _defaultValue;

	protected String _strCaption = "";
	protected String _strFieldName;
	protected String _strTableName;

	protected EditMode _maxEditMode = EditMode.WRITE;

	/**
	 * @param objectBase
	 * @param db
	 */
	public DataBindingField(ObjectBase objectBase, DataBinding db)
	{
		_strFieldName = db.getDataBindingFieldName();
		if(_strFieldName == null)
		{
			LOG.info(MessageFormat.format("No fieldname in {0} with binding {1}", objectBase, db));
			return;
		}		

		DataBindingMember dbm = db.getDataBindingMember();

		_strTableName = dbm.getDataBindingTableName();

		_objEntity = DBConnection.instance().getObjectContext().getEntityResolver().getObjEntity(_strTableName);
				
		if (objectBase.getCaption() == null)
		{
			_strCaption = null;
		
		}
		else
		{
            ITranslator tr = TranslatorFactory.getTranslator();
			_strCaption = tr.getString("gozer.caption." + _objEntity.getDbEntityName(),  objectBase.getCaption().toLowerCase());
		}
		
		// Set the Definitions from the ROWtype
		// TODO re-add EAD Manager
		/*
		if (EADManager.instance().isAttributeVirtual(_strTableName, _strFieldName))
		{
			EntityAttributeDefinitionDataRow dataRow =  EADManager.instance().getAttributeDefinition(_strTableName, _strFieldName);
			try
			{
				_fieldType = Class.forName(dataRow.getAttributeType());
			} catch (ClassNotFoundException e)
			{
                LOG.error("Unknow Class : " + dataRow.getAttributeType());
				LOG.warn(e.getMessage());
                _fieldType = String.class;
			}
			
			_dataFormat = DataTypeFormat.getDataTypeFormat(_fieldType, (dataRow.getAttributeFormat()==null)?"":dataRow.getAttributeFormat());

			// And now the db attributes
			//TODO DbAttribute dbAttribute = objAttribute.getDbAttribute();

			_nMaxLength = dataRow.getAttributeLength();
			_bIsMandatory = dataRow.getAttributeNullable().booleanValue();
			_bIsPrimaryKey = false;	
			_bIsForeignKey = false;
			_bIsAttributeVirtual = true;

		} 
		else
		{*/
			Collection<ObjAttribute> colAttributes = _objEntity.getAttributes();
			for (ObjAttribute objAttribute : colAttributes)
			{
				if (_strFieldName.compareToIgnoreCase(objAttribute.getName()) == 0)
				{
					_fieldType = objAttribute.getJavaClass();
					_dataFormat = DataTypeFormat.getDataTypeFormat(_fieldType, objectBase.getAttribute(ObjectBase.ATTRIBUTE_FORMAT));

					// And now the db attributes
					DbAttribute dbAttribute = objAttribute.getDbAttribute();

					_nMaxLength = objectBase.getAttributeAsInt(ObjectBase.ATTRIBUTE_MAX_LENGTH, dbAttribute.getMaxLength());
					_bIsMandatory = dbAttribute.isMandatory();
					_bIsPrimaryKey = dbAttribute.isPrimaryKey();
					_bIsForeignKey = dbAttribute.isForeignKey();
					_bIsAttributeVirtual = false;
					
					break;
				}
			}

			// TODO: Load fkey constraint infos from datarow
			// currently solved in GW**FKeyFieldPanel
			if (isForeignKey())
			{

			}
		//} from fix me

		// Now set the Overrides from the Gozer File
		// objectBase.getMaxLengthOverride
	}

	public DataTypeFormat getDataFormat()
	{
		return _dataFormat;
	}
	
	public ObjEntity getObjEntity()
	{
		return _objEntity;
	}

	public String getFieldName()
	{
		return _strFieldName;
	}
	
	public String getTableName()
	{
		return _strTableName;
	}

	public Class<?> getFieldType()
	{
		return _fieldType;
	}

	public int getMaxLength()
	{
		return _nMaxLength;
	}

	public boolean isForeignKey()
	{
		return _bIsForeignKey;
	}

	public boolean isMandatory()
	{
		return _bIsMandatory;
	}

	public boolean isPrimaryKey()
	{
		return _bIsPrimaryKey;
	}
	
	public boolean isAttributeVirtual()
	{
		return _bIsAttributeVirtual;
	}
}
