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
 ** $Id: LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import java.util.Collection;

import org.apache.cayenne.map.ObjRelationship;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.field.GOneToNField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

/**
 * Panel for a one to N relations
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWDetailOneToNPanel extends GWPanel
{
    /**
     * Dispalyed tex field
     */
    protected Label _textField;
    private DataBinding _binding;
    private DataBinding _bindingTarget;
    private String _displayField;
    private String _fieldSeparator = ", ";

    /**
     * Is the field read only or not
     */
    protected boolean _isReadOnly;

    /**
     * Constructor
     * 
     * @param id
     * @param model
     * @param fieldForm
     * @param detailPanel
     */
    public GWDetailOneToNPanel(String id, IModel<?> model, GOneToNField fieldForm, final GWDetailPanel detailPanel)
    {
        super(id, model);

        _isReadOnly = fieldForm.isReadOnly();
        _binding = fieldForm.getDataBinding();
        _displayField = fieldForm.getDisplayField();

        if (fieldForm.getSeparator() != null && !fieldForm.getSeparator().isEmpty())
            _fieldSeparator = fieldForm.getSeparator();

        final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(), fieldForm.getCaption());

        DataBindingField field = _binding.getDataBindingField();

        final DataBinding dataBinding = fieldForm.getDataBinding();
        DataBindingManager dbm = dataBinding.getDataBindingManager();
        getTargetDataBinding(fieldForm, dbm, fieldForm.getDataBindingTarget());

        Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
        add(label);

        _textField = new Label("textField", computeValue());
        add(_textField);
    }

    private void refreshRepeatingView()
    {
        _textField.setDefaultModel(new Model(computeValue()));
    }

    
    /**
     * Compute the value for the field
     * 
     * @return the value
     */
    private String computeValue()
    {
        DataRowBase[] drb = _bindingTarget.getDataBindingManager().getResolvedRows(_bindingTarget.getDataBindingMember(), _binding.getCurrentRow());
        String val = "";
        for (int i = 0; i < drb.length; i++)
        {
            val += drb[i].getPropertyAsString(_displayField) + _fieldSeparator;
        }

        if (val.length() > 0)
            val = val.substring(0, val.length() - _fieldSeparator.length());

        return val;
    }


    /**
     * Retrieve Data binding for the N entity
     * 
     * @param fieldForm
     * @param dataBindingManager
     * @param dataBindingTargetStr
     */
    private void getTargetDataBinding(GOneToNField fieldForm, DataBindingManager dataBindingManager, String dataBindingTargetStr)
    {
        Collection<ObjRelationship> relations = _binding.getCurrentRow().getObjEntity().getRelationships();

        ObjRelationship relTarget = null;

        for (ObjRelationship rel : relations)
        {
            if (rel.getTargetEntityName().compareTo(dataBindingTargetStr) == 0)
            {
                relTarget = rel;
                break;
            }
        }

        String bindingMember = null;
        if (relTarget != null)
        {
            bindingMember = fieldForm.getDataBindingMember().getDataBindingMemberWOField() + "." + relTarget.getName() + "[0]." + _displayField;
        }

        DataBindingMember dataBindingMember = new DataBindingMember(bindingMember);
        _bindingTarget = new DataBinding(fieldForm, dataBindingManager, dataBindingMember);
    }

    @Override
    public void onBeforeRender()
    {
        super.onBeforeRender();

        refreshRepeatingView();

        IGozerFrameExtension gfe = getFrameExtension();

        if (_isReadOnly || !((DataBindingModel) _textField.getDefaultModel()).hasRows())
        {
            _textField.setEnabled(false);
        }
        else
        {
            _textField.setEnabled((gfe.getGozerController().getFrameStatus() == GozerFrameStatus.EDIT)
                    || (gfe.getGozerController().getFrameStatus() == GozerFrameStatus.NEW));
        }

    }

}
