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
package com.swordlord.gozer.ui.gozerframe;

import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerDisplayMode;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.ui.modal.ModalWindowEx;

@SuppressWarnings("serial")
public class GozerModalWindow extends ModalWindowEx
{
	private WicketGozerPanel _panel;

	/**
	 * @param strId
	 * @param gfe
	 * @param dataBinding
	 */
	public GozerModalWindow(String strId, IGozerFrameExtension gfe, DataBinding dataBinding)
	{
		super(strId, dataBinding);
		
		_panel = new WicketGozerPanel(this.getContentId(), gfe, GozerDisplayMode.WEB_MODAL);

    	_panel.getContext().setModalWindow(this);

    	setContent(_panel);

        setTitle(_panel.getCaption());
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
		return "fkey_panel";
	}
}
