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
 ** $Id:  $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.jalapeno.tablemodel;

// IMPORTS
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.table.AbstractTableModel;

import com.swordlord.common.i18n.ITranslator;
import com.swordlord.common.i18n.TranslatorFactory;
import com.swordlord.repository.database.backuprestore.BackupRestoreTablesItem;
import com.swordlord.repository.database.backuprestore.BackupRestoreTablesList;
import com.swordlord.repository.database.backuprestore.ManifestFile;

import com.swordlord.sobf.swing.Controller;

//{{{ PluginTableModel class
/**
 * TODO JavaDoc for ObjEntityTableModel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class ObjEntityTableModel extends AbstractTableModel
{
	private ITranslator _tr = TranslatorFactory.getTranslator();
	
	private BackupRestoreTablesList _listTables;
	private byte _bytDisplayType = DISPLAY_TYPE_BACKUP;
	
	public static byte DISPLAY_TYPE_BACKUP = 1;
	public static byte DISPLAY_TYPE_RESTORE = 2;

	public ObjEntityTableModel(byte bytDisplayType)
	{
		 _listTables = new BackupRestoreTablesList();
		 _bytDisplayType = bytDisplayType;
	} 
	
	public void updateListFromSnapshot(File fileSnapshot)
	{
		if(fileSnapshot == null)
		{
			Iterator it = _listTables.iterator();
			while(it.hasNext())
			{
				BackupRestoreTablesItem item = (BackupRestoreTablesItem) it.next();
				item.setImport(false);
			}
			return;
		}
		
		try
		{
			ZipFile zipfile = new ZipFile(fileSnapshot.getName());
			ZipEntry entryManifest = zipfile.getEntry(ManifestFile.MANIFEST_FILE_NAME);
			
			BufferedInputStream is = new BufferedInputStream (zipfile.getInputStream(entryManifest));
            
    		BufferedReader brFileToBeLoaded = null;
    		StringBuffer sbResource = new StringBuffer();
    			
    		try 
    		{
    			int iFileCharacter;
    			
    			brFileToBeLoaded = new BufferedReader(new InputStreamReader(is));
    			
    			while(true)
    			{
    							iFileCharacter = brFileToBeLoaded.read();
    							if(iFileCharacter==-1) break;
    							sbResource.append((char)iFileCharacter);
    			}
    		} 
    		catch(Exception ex) 
    		{
    			Log.instance().getLogger().error(ex);
    		} 
    		
    		try 
    		{
    				if(brFileToBeLoaded!=null) brFileToBeLoaded.close();
    		} 
    		catch(Exception ex) 
    		{
    			Log.instance().getLogger().error(ex);
    		}
    		
    		ManifestFile manifest = new ManifestFile();
    		manifest.createFromXML(sbResource.toString());
		}
		catch(Exception e)
		{
			Log.instance().getLogger().error(e.getLocalizedMessage());
		}
	}
	
	public BackupRestoreTablesList getList()
	{
		return _listTables;		
	}
	
	@Override
    public int getColumnCount()
	{
		return 3;
	}

	@Override
    public Class getColumnClass(int columnIndex)
	{
		switch (columnIndex)
		{
			case 0: return Boolean.class;
			case 1: return String.class;
			case 2: return String.class;
		  	default: return Object.class;
		}
	}

	@Override
    public String getColumnName(int column)
	{
		switch (column)
		{
			case 0:
			{
				if(_bytDisplayType == DISPLAY_TYPE_BACKUP)
				{
					return _tr.getString("export", ObjEntityTableModel.class);
				}
				else
				{
					return _tr.getString("import", ObjEntityTableModel.class);
				}
			}
			case 1:
				return _tr.getString("table", ObjEntityTableModel.class);
			case 2:
				return _tr.getString("query", ObjEntityTableModel.class);
			default:
				throw new Error(_tr.getString("column_out_of_range", ObjEntityTableModel.class));
		}
	} 

	@Override
    public int getRowCount()
	{
		return _listTables.size();
	}
	
	@Override
    public Object getValueAt(int rowIndex,int columnIndex)
	{
		if(rowIndex < 0 | rowIndex > _listTables.size())
		{
			throw new Error(_tr.getString("column_out_of_range", ObjEntityTableModel.class));
		}
		
		BackupRestoreTablesItem item = _listTables.getItem(rowIndex);

		switch (columnIndex)
		{
			case 0:
			{
				if(_bytDisplayType == DISPLAY_TYPE_BACKUP)
				{
					return new Boolean(item.getExport());
				}
				else
				{
					return new Boolean(item.getImport());
				}
			}
			case 2:
				return item.getQuery();
			default:
				return item.getTableName();
		}		
	}

	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				return true;
			default:
				return false;
		}
	} 
	
	@Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		if(rowIndex < 0 | rowIndex > _listTables.size())
		{
			throw new Error(_tr.getString("column_out_of_range", ObjEntityTableModel.class));
		}
		
		BackupRestoreTablesItem item = _listTables.getItem(rowIndex);

		switch (columnIndex)
		{
			case 0:
			{
				if(_bytDisplayType == DISPLAY_TYPE_BACKUP)
				{
					item.setExport(((Boolean)value).booleanValue());
				}
				else
				{
					item.setImport(((Boolean)value).booleanValue());		
				}
				break;
			}				
			default:
				// do nothing
		}
	}
}
