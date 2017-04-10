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
package com.swordlord.gozer.ui.modal;

import com.swordlord.common.i18n.ITranslator;
import com.swordlord.common.i18n.TranslatorFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * TODO JavaDoc for ModalWindowEx.java
 * 
 * @author lordeidi
 * 
 */
@SuppressWarnings("serial")
public class ModalWindowEx extends ModalWindow
{
	private DataBinding _dataBinding;
	private DataRowBase _selectedRow;

    /**
     * Constructor
     * 
     * @param strId
     * @param dataBinding
     */
	public ModalWindowEx(String strId, DataBinding dataBinding)
	{
		super(strId);
		
		_dataBinding = dataBinding;
		
		final ITranslator tr = TranslatorFactory.getTranslator();
    	final String strWarning = tr.getString(ModalWindowEx.class.getName(), "close_not_working");

    	super.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
	    {
	        @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target)
	        {
                target.appendJavaScript("alert('" + strWarning + "');");
	            return false;
	        }
	    });
    	
    	setWidthUnit("px");
        setHeightUnit("px");
        
    	setInitialWidth(965);
    	setInitialHeight(600);

        setCookieName("modal");
        // TODO: check this is really not needed
        // setPageMapName("modal");
        setResizable(true);
        setMaskType(MaskType.SEMI_TRANSPARENT);
        //setMaskType(MaskType.TRANSPARENT);
        setCssClassName(ModalWindow.CSS_CLASS_GRAY);
	}

    /**
     * @param target
     * @param bIsOK
     */
	public void close(AjaxRequestTarget target, boolean bIsOK)
	{
		if(bIsOK)
		{
			DataRowBase row = getSelectedRow();
			if(row != null)
			{
				_dataBinding.setRelation(row);
			}
		}

		super.close(target);
	}
	
	public void show(final AjaxRequestTarget target)
	{
		super.show(target);
		
        target.appendJavaScript(""//
				+ "var thisWindow = Wicket.Window.get();\n"//
				+ "if (thisWindow) {\n"//
				+ "thisWindow.window.style.width = \"" + getInitialWidth() + "px\";\n"//
				+ "thisWindow.content.style.height = \"" + getInitialHeight() + "px\";\n"//
				+ "thisWindow.center();\n"//
				+ "}"//
		);
	}

	/**
	 * Returns the id of content component.
	 *
	 * <pre>
	 * ModalWindow window = new ModalWindow(parent, &quot;window&quot;);
	 * new MyPanel(window, window.getContentId());
	 * </pre>
	 *
	 * @return Id of content component.
	 */
	@Override
	public String getContentId()
	{
		return "modal_panel";
	}

    /**
     * @return pointer to the session
     */
	public IGozerSessionInfo getGozerSession()
	{
		return (IGozerSessionInfo) getSession();
	}

    /**
     * @return convenience function to access the data binding of this modal
     *         window
     */
	public DataBinding getDataBinding()
	{
		return _dataBinding;
	}
	
    /**
     * @return pointer to the selected row
     */
	public DataRowBase getSelectedRow()
	{
		return _selectedRow;
	}

    /**
     * @param row
     *            access to set the currently selected row
     */
	public void setSelectedRow(DataRowBase row)
	{
		_selectedRow = row;
	}
}
