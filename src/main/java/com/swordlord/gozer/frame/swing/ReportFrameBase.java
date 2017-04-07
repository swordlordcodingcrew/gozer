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
** $Id: ReportFrameBase.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.frame.swing;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.frame.IGozerFrameExtension;


@SuppressWarnings("serial")
// TODO:
public class ReportFrameBase extends JFrame implements ActionListener, WindowListener
{
	private static Rectangle DEFAULT_BOUNDS = new Rectangle(20, 20, 600, 400);

	protected IGozerFrameExtension _gfe = null;

	public ReportFrameBase(IGozerFrameExtension gfe)
	{
		super();

		_gfe = gfe;

		initialiseFrame();
		generateContent();
		activateFrame();

	}

	public void actionPerformed(ActionEvent e)
	{
		/*
		 
		String strCommand = e.getActionCommand();
		 		
		if (strCommand.equals(DataViewToolBar.CMD_DELETE))
		{
			deleteRow();
		}
		else if (strCommand.equals(DataViewToolBar.CMD_RELOAD))
		{
			reloadData();
		}
		else if (strCommand.equals(DataViewToolBar.CMD_PRINT))
		{
			changeState(DISPLAY_METHOD_REPORT, false);
		}
		else if (strCommand.equals(DataViewToolBar.CMD_FILTER))
		{
			applyFilter();
		}
		else if (strCommand.equals(DataViewToolBar.CMD_HELP))
		{
			viewHelp();
		}
		else if (strCommand.equals(DataViewToolBar.CMD_LIST))
		{
			changeState(DISPLAY_METHOD_LIST, false);
		}
		else if (strCommand.equals(DataViewToolBar.CMD_DETAIL))
		{
			changeState(DISPLAY_METHOD_DETAIL, false);
		}
		else if (strCommand.equals(DataViewToolBar.CMD_ADD))
		{
			// make new record for the edit state
			Persistent obj = getObjectContext().newObject(_we.getDBCommon());
			if (_dataObjectList != null && _listPnl != null)
			{
				_dataObjectList.add(obj);

				setSelectedRecord(_dataObjectList.size() - 1);
			}

			changeState(DISPLAY_METHOD_NEW, true);
		}
		else if (strCommand.equals(DataViewToolBar.CMD_TREE))
		{
			new AssetTreeFrame();
			// TODO: show assettreeframe or inventorytreeframe
		}
		else if (strCommand.equals(DataViewToolBar.CMD_REPORT))
		{
			changeState(DISPLAY_METHOD_REPORT, false);
		}
		*/
	}

	protected void activateFrame()
	{
		setVisible(true);
		toFront();
	}

	// upcall to see if Frame can be closed
	protected boolean canCloseWindow()
	{
		return true;
	}

	protected void generateContent()
	{
		SAXBuilder sb = new SAXBuilder();
		Document document = null;

		try
		{
			File file = new File(_gfe.getGozerLayoutFileName());
			FileReader fr = new FileReader(file);

			document = sb.build(fr);

			Parser parser = new Parser(_gfe.getDataBindingContext());
			parser.createTree(document);

			//SwingRenderer sr = new SwingRenderer(ot, _dc, _controller);

			//getContentPane().add(sr.renderTree());
			setBounds(DEFAULT_BOUNDS);

			pack();
		}
		catch(JDOMException eJDOM)
		{
			Log.instance().getLogger().error(eJDOM);
		}
		catch (Exception e)
		{
			Log.instance().getLogger().error(e);
		}
	}

	protected Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}

	protected void initialiseFrame()
	{
		// Needed for closing behaviour
		addWindowListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setUIManager();
	}

	protected void setUIManager()
	{
			Font font = new Font("Dialog", 0, 12);

			UIDefaults defaults = UIManager.getDefaults();

			// Help can be found here :)
			// http://www.mindprod.com/jgloss/font.html
			defaults.put("Menu.font", font);
			defaults.put("MenuBar.font", font);
			defaults.put("MenuItem.font", font);
			defaults.put("Tree.font", font);
			defaults.put("List.font", font);
			defaults.put("TextArea.font", font);
			defaults.put("TextPane.font", font);
			defaults.put("Label.font", font);
			defaults.put("TabbedPane.font", font);
	}

	public void windowActivated(WindowEvent e) {}
	// Window handling
	// ---------------------------------------------------------------
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
