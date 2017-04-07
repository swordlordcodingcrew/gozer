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
 ** $Id: CodeDropDown.java 1369 2013-01-04 10:39:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.components.wicket.code;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.renderer.wicket.CodeChoiceRenderer;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * Implements gozer element 'listanddetail'. Supports GDetail, GList and
 * GActionBox.
 */
@SuppressWarnings("serial")
public class CodeDropDown extends DropDownChoice<Object>
{
    /**
     * @param id
     * @param dataBinding
     * @param model
     * @param choices
     */
    public CodeDropDown(String id, DataBinding dataBinding, IModel<Object> model, List<DataRowBase> choices)
	{
		super(id, model, choices);
		
		setChoiceRenderer(new CodeChoiceRenderer(dataBinding.getDataBindingField()));
	}
}
