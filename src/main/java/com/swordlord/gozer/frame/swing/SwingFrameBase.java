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
** $Id: SwingFrameBase.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.frame.swing;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.renderer.swing.SwingRenderer;


@SuppressWarnings("serial")
public class SwingFrameBase extends JFrame implements Action, ActionListener, WindowListener
{
	private static Rectangle DEFAULT_BOUNDS = new Rectangle(20, 20, 600, 400);

	protected IGozerFrameExtension _gfe = null;

	public SwingFrameBase(IGozerFrameExtension gfe)
	{
		super();

		_gfe = gfe;

		initialiseFrame();
		generateContent();
		activateFrame();
	}

	public void actionPerformed(ActionEvent e)
	{
		_gfe.getDataContainer().dumpContentToFile();
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
			ObjectTree ot = parser.getTree();

			SwingRenderer sr = new SwingRenderer(ot, _gfe);

			getContentPane().add(sr.renderTree());
		}
		catch(JDOMException eJDOM)
		{
			eJDOM.printStackTrace();
			Log.instance().getLogger().error(eJDOM);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.instance().getLogger().error(e);
		}

		setBounds(DEFAULT_BOUNDS);

		pack();
	}

	protected Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}

	public Object getValue(String str) { return null; }

	protected void initialiseFrame()
	{
		// Needed for closing behaviour
		addWindowListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		JPanel content = (JPanel) getContentPane();
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);

	    InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(stroke, "DUMP");
	    content.getActionMap().put("DUMP", this);

		setUIManager();
	}
	public void put(String str, Object obj) { }
	public void putValue(String str, Object obj) { }
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
