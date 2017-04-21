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
package com.swordlord.gozer.components.wicket.datatable;

import com.swordlord.sobf.wicket.main.FilterState.escapePropertyPath;

import java.util.Date;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.generic.field.GLinkField;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.filter.DateFilter;
import com.swordlord.gozer.filter.TextFilterEx;
import com.swordlord.gozer.renderer.wicket.CodeChoiceRenderer;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.datatable.DataTableBase;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.jalapeno.dataview.OrderingParam;
import com.swordlord.jalapeno.fkey.FKeyBase;
import com.swordlord.repository.datatable.Code.CodeDataTable;
import com.swordlord.repository.datatable.Code.base.CodeDataTableBase;
import com.swordlord.sobf.common.config.UserPrefs;

/**
 * {@link GozerColumn} with filter support.
 * 
 * @author LordEidi
 */
@SuppressWarnings("serial")
public class GozerFilteredColumn extends GozerColumn implements IFilteredColumn<DataRowBase, String>
{
	private GField _field;
	
    /**
     * @param displayModel
     * @param sortProperty
     * @param bIsFirstRow
     * @param obField
     */
	public GozerFilteredColumn(IModel<String> displayModel, String sortProperty, Boolean bIsFirstRow, ObjectBase obField) 
	{
        super(displayModel, escapePropertyPath(sortProperty), bIsFirstRow);
		
		initialise(obField);
	}

    /**
     * @param displayModel
     * @param bIsFirstRow
     * @param obField
     */
	public GozerFilteredColumn(IModel<String> displayModel, Boolean bIsFirstRow, ObjectBase obField) 
	{
		super(displayModel, bIsFirstRow);

		initialise(obField);
	}
	
	private void initialise(ObjectBase obField)
	{
		if(obField instanceof GField)
		{
			_field = (GField)obField;
		}
	}
	
	private GField getField()
	{
		return _field;
	}
	
	private boolean hasField()
	{
		return _field != null;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getFilter(String componentId, FilterForm<?> form)
    {
        if (!hasField())
        {
            return null;
        }

        final GField field = getField();
        if (!field.isFilterable())
        {
            return null;
        }

        if (field instanceof GLinkField)
        {
            return new TextFilter<String>(componentId, getFilterModel(form, String.class), form);
        }
        final DataBinding binding = field.getDataBinding();
        final DataBindingField bindingField = binding.getDataBindingField();
        if (field instanceof GCodeField)
        {
            CodeDataTable tabCode = new CodeDataTable(new DataContainer());
            tabCode.fillByLanguageAndCodeName(UserPrefs.instance().getLanguageCode(), ((GCodeField) field).getCodeType());

            OrderingParam orderingParam = new OrderingParam(CodeDataTableBase.SORT_NR_PROPERTY, true);
            List<DataRowBase> codes = tabCode.getDataRows(orderingParam);

            CodeChoiceRenderer renderer = new CodeChoiceRenderer(bindingField);

            ChoiceFilter aChoiceFilter = new ChoiceFilter<Object>(componentId, getFilterModel(form, Object.class), form, codes, renderer, true);
            aChoiceFilter.getChoice().setNullValid(true);
            return aChoiceFilter;
        }
        if (Date.class.equals(bindingField.getFieldType()))
        {
            return new DateFilter(componentId, this.<Date> getFilterModel(form, Date.class), form, true);
        }
        if (bindingField.isForeignKey())
        {
            final IModel<String> model = getFKFilterModel(field, form);
            // disable filtering if the 'target' field could not be resolved
            if (model == null)
            {
                return null;
            }
            return new TextFilterEx(componentId, model, form, true);
        }
        return new TextFilterEx(componentId, getFilterModel(form, String.class), form, true);
    }

    /**
     * Creates the filter model for an FK field (
     * {@link DataBindingField#isForeignKey()}).
     * 
     * @param <T>
     *            The model type
     * @param gfield
     *            The gozer field node
     * @param form
     *            The filter form of the table
     * @return The filter model or null
     */
    private <T> IModel<T> getFKFilterModel(GField gfield, FilterForm form)
    {
        final DataBinding binding = gfield.getDataBinding();
        final DataBindingField field = binding.getDataBindingField();
        final DataViewBase view = binding.getDataBindingManager().getDataView(binding.getDataBindingMember());
        if (view == null)
        {
            return null;
        }
        final DataTableBase dt = view.getDataTable();
        final FKeyBase fk = dt.getFKey(field.getFieldName());
        if (fk == null)
        {
            return null;
        }
        final DataBindingMember path = fk.getDisplayDataBindingMember(field.getObjEntity(), gfield);
        if (path == null)
        {
            return null;
        }
        return new PropertyModel<T>(form.getDefaultModel(), escapePropertyPath(path.getRelativePathWithField()));
    }

    /**
     * Returns the model that will be passed on to the text filter. Users can
     * override this method to change the model.
     * 
     * @param <T>
     *            The model object type
     * @param form
     *            filter form
     * @param valueType
     *            The model value type
     * @return model passed on to the text filter
     */
    protected <T> IModel<T> getFilterModel(FilterForm form, final Class<T> valueType)
	{
        return new TypedPropertyModel<T>(form.getDefaultModel(), getSortProperty(), valueType);
	}

    /**
     * {@link PropertyModel} with fixed typing.<br>
     * Avoids wicket warnings caused by property type resolution problems (which
     * may occur if a map is used as base model).
     * 
     * @author LordEidi
     * 
     * @param <T>
     *            The property value type
     */
    private static class TypedPropertyModel<T> extends PropertyModel<T>
    {
        /** The property value type. */
        private final Class<T> _type;

        /**
         * Default constructor.
         * 
         * @param modelObject
         *            The model object
         * @param expression
         *            The expression to access the property to use as model
         * @param type
         *            The property type
         */
        private TypedPropertyModel(Object modelObject, String expression, Class<T> type)
        {
            super(modelObject, expression);
            _type = type;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<T> getObjectClass()
        {
            return _type;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSortProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSortable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach()
    {
        // TODO Auto-generated method stub

    }

}
