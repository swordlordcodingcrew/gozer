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

import java.text.MessageFormat;
import java.util.LinkedList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.generic.field.GLibraryField;
import com.swordlord.gozer.components.generic.field.GLinkField;
import com.swordlord.gozer.components.generic.field.GOneToNField;
import com.swordlord.gozer.components.generic.field.GPredefinedListField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.detail.GWDetailPanelActionToolbar;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.datatypeformat.BooleanTypeFormat;
import com.swordlord.gozer.datatypeformat.DateTypeFormat;
import com.swordlord.jalapeno.datatable.DataTableBase;

@SuppressWarnings("serial")
public class GWDetailPanel extends GWPanel
{
    @SuppressWarnings("unchecked")
    public GWDetailPanel(String id, IModel model, GDetail detail, final Form<?> form)
    {
        super(id, model);

        final IModel<?> detailModel = getModel();

        GWDetailPanelActionToolbar tb = new GWDetailPanelActionToolbar("actionToolbox", model, detail, form);
        add(tb);

        LinkedList childs = (LinkedList) detail.getChildren().clone();
        for (int i = 0; i < childs.size(); i++)
        {
            if (childs.get(i) instanceof GActionToolbar)
            {
                childs.remove(i);
            }
        }

        ListView listView = new ListView("eachGuiElem", childs)
        {
            @Override
            protected void populateItem(ListItem item)
            {
                ObjectBase formObject = (ObjectBase) item.getModelObject();

                String tblName = formObject.getDataBinding().getDataBindingMember().getDataBindingTableName();
                String fieldName = formObject.getDataBinding().getDataBindingField().getFieldName();
                DataTableBase dtb = formObject.getDataBinding().getDataBindingManager().getDataContainer().getTableByAbsoluteName(
                        "com.swordlord.repository.datatable." + tblName + ".base." + tblName + "DataTableBase");
                String[] listOfFields = dtb.getPropertyList();

                // TODO re-add EAD Manager
                //if (!formObject.getClass().equals(GLibraryField.class) && !formObject.getClass().equals(GOneToNField.class)
                //        && !EADManager.instance().isAttributeDefined(listOfFields, tblName, fieldName))
                if (!formObject.getClass().equals(GLibraryField.class) && !formObject.getClass().equals(GOneToNField.class))
                {
                    String msg = _tr.getString("com.swordlord.sobf.wicket.message", "virutal_attribute_not_defined_db");
                    msg = MessageFormat.format(msg + " ({0}, {1})", fieldName, tblName);
                    LOG.warn(msg);
                    Label label = new Label("cell", "<td colspan=3><b><font color=red>" + msg + "<br></font></b></td>");
                    label.setEscapeModelStrings(false);
                    item.add(label);
                }
                else
                {

                    if (formObject.getClass().equals(GField.class))
                    {
                        final DataBinding dataBinding = formObject.getDataBinding();
                        final DataBindingField field = dataBinding.getDataBindingField();

                        if (field.isForeignKey())
                        {
                            GWDetailFKeyFieldPanel detailFKeyFieldPanel = new GWDetailFKeyFieldPanel("cell", detailModel, (GField) formObject,
                                    GWDetailPanel.this);
                            detailFKeyFieldPanel.setRenderBodyOnly(true);
                            // omit the <span> tags, we want to have the <td>
                            // tags of DetailFieldPanel
                            // right after <tr>

                            item.add(detailFKeyFieldPanel);
                        }
                        else if (field.getDataFormat() instanceof DateTypeFormat)
                        {
                            GWDetailDateFieldPanel detailFieldPanel = new GWDetailDateFieldPanel("cell", detailModel, (GField) formObject);
                            detailFieldPanel.setRenderBodyOnly(true);
                            // omit the <span> tags, we want to have the <td>
                            // tags of DetailFieldPanel
                            // right after <tr>
                            item.add(detailFieldPanel);
                        }
                        else if (field.getDataFormat() instanceof BooleanTypeFormat)
                        {
                            GWDetailBooleanFieldPanel detailBooleanFieldPanel = new GWDetailBooleanFieldPanel("cell", detailModel, (GField) formObject);
                            detailBooleanFieldPanel.setRenderBodyOnly(true);
                            // omit the <span> tags, we want to have the <td>
                            // tags of DetailFieldPanel
                            // right after <tr>

                            item.add(detailBooleanFieldPanel);
                        }

                        else
                        {
                            GWDetailFieldPanel detailFieldPanel = new GWDetailFieldPanel("cell", detailModel, (GField) formObject);
                            detailFieldPanel.setRenderBodyOnly(true);
                            // omit the <span> tags, we want to have the <td>
                            // tags of DetailFieldPanel
                            // right after <tr>

                            item.add(detailFieldPanel);
                        }
                    }
                    else if (formObject.getClass().equals(GLinkField.class))
                    {
                        GWDetailLinkFieldPanel detailLinkFieldPanel = new GWDetailLinkFieldPanel("cell", detailModel, (GLinkField) formObject);
                        detailLinkFieldPanel.setRenderBodyOnly(true);
                        // omit the <span> tags, we want to have the <td> tags
                        // of DetailFieldPanel
                        // right after <tr>

                        item.add(detailLinkFieldPanel);
                    }
                    else if (formObject.getClass().equals(GCodeField.class))
                    {
                        GWDetailCodeFieldPanel detailCodeFieldPanel = new GWDetailCodeFieldPanel("cell", detailModel, (GCodeField) formObject);
                        detailCodeFieldPanel.setRenderBodyOnly(true);
                        // omit the <span> tags, we want to have the <td> tags
                        // of DetailFieldPanel
                        // right after <tr>

                        item.add(detailCodeFieldPanel);
                    }
                    else if (formObject.getClass().equals(GPredefinedListField.class))
                    {
                        GWDetailPredefinedListFieldPanel detailPredefinedListFieldPanel = new GWDetailPredefinedListFieldPanel("cell", detailModel,
                                (GPredefinedListField) formObject);
                        detailPredefinedListFieldPanel.setRenderBodyOnly(true);
                        // omit the <span> tags, we want to have the <td> tags
                        // of DetailFieldPanel
                        // right after <tr>

                        item.add(detailPredefinedListFieldPanel);
                    }
                    else if (formObject.getClass().equals(GLibraryField.class))
                    {
                        GWDetailLibraryPanel detailLibraryPanel = new GWDetailLibraryPanel("cell", detailModel, (GLibraryField) formObject, GWDetailPanel.this);
                        detailLibraryPanel.setRenderBodyOnly(true);

                        item.add(detailLibraryPanel);
                    }
                    else if (formObject.getClass().equals(GOneToNField.class))
                    {
                        GWDetailOneToNPanel detailOneToNPanel = new GWDetailOneToNPanel("cell", detailModel, (GOneToNField) formObject, GWDetailPanel.this);
                        detailOneToNPanel.setRenderBodyOnly(true);

                        item.add(detailOneToNPanel);
                    }
                }
            }
        };

        listView.setReuseItems(true);
        add(listView);
    }

    public void updateComponents()
    {
        // this.renderComponent();
    }
}
