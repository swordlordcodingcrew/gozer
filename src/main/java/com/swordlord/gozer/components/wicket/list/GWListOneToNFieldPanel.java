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
 ** $Id: GWListFieldPanel.java 979 2010-04-09 14:29:34Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import java.text.MessageFormat;
import java.util.Collection;

import org.apache.cayenne.map.ObjRelationship;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.field.GOneToNField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;

/**
 * A panel to represent a field in a Gozer list.
 */
@SuppressWarnings("serial")
public class GWListOneToNFieldPanel extends GWPanel
{
    /**
     * Label to display OneToN informations
     */
    protected Label _label;
   
    /**
     * Row Key
     */
    protected DataRowKeyBase _key = null;
    
    
    /**
     * Base Data Binding
     */
    protected DataBinding _db;
    
    
    /**
     * Parent Data binding
     */
    protected DataBinding _dbList;
    
    
    private DataBinding _bindingTarget;
    private String _displayField;
    private String _fieldSeparator = ", ";
    private DataRowBase _row;

    /**
     * @param id
     * @param model
     *            must contain a {@link DataRowBase}.
     * @param field
     * @param dataBinding
     */
    public GWListOneToNFieldPanel(String id, IModel<?> model, GOneToNField field, DataBinding dataBinding)
    {
        super(id, model);

        _displayField = field.getDisplayField();

        if (field.getSeparator() != null && !field.getSeparator().isEmpty())
            _fieldSeparator = field.getSeparator();

        _row = (DataRowBase) model.getObject();
        
        getTargetDataBinding(_row, field, dataBinding.getDataBindingManager(), field.getDataBindingTarget());
        
        _label = new Label("name", computeValue());

        GList parent = field.getList();
        _dbList = parent.getDataBinding();

        int iMaxDisplayWidth = field.getMaxDisplayWidth();
        if (iMaxDisplayWidth > 0)
        {
            add(new AttributeModifier("style", new Model<String>(MessageFormat.format("max-width: {0}px;", iMaxDisplayWidth))));
        }

        _label.add(new AttributeModifier("title", new Model<String>(computeValue())));
        add(_label);

        _key = _row.getKey();
        _db = dataBinding;
    }

    /**
     * Default Constructor
     * 
     * @param id
     * @param model
     */
    public GWListOneToNFieldPanel(String id, IModel<?> model)
    {
        super(id, model);
    }

    /**
     * Retrieve Data binding for the N entity
     * 
     * @param row
     * @param fieldForm
     * @param dataBindingManager
     * @param dataBindingTargetStr
     */
    private void getTargetDataBinding(DataRowBase row, GOneToNField fieldForm, DataBindingManager dataBindingManager, String dataBindingTargetStr)
    {
        Collection<ObjRelationship> relations = row.getObjEntity().getRelationships();

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


    /**
     * Compute the value for the field
     * 
     * @return the value
     */
    private String computeValue()
    {       
        DataRowBase[] drb = _bindingTarget.getDataBindingManager().getResolvedRows(_bindingTarget.getDataBindingMember(), _row);
        String val = "";
        for (int i = 0; i < drb.length; i++)
        {
            val += drb[i].getPropertyAsString(_displayField) + _fieldSeparator;
        }

        if (val.length() > 0)
            val = val.substring(0, val.length() - _fieldSeparator.length());

        return val;
    }


}
