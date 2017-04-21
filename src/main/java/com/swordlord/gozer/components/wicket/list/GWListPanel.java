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
 ** $Id: GWListPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.list;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.swordlord.gozer.dataprovider.GozerSortableFilterableDataProvider;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActivateAction;
import com.swordlord.gozer.components.generic.action.GDetailAction;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.generic.field.GLinkField;
import com.swordlord.gozer.components.generic.field.GOneToNField;
import com.swordlord.gozer.components.wicket.GWActivatePanel;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.datatable.GozerColumn;
import com.swordlord.gozer.components.wicket.datatable.GozerDataTable;
import com.swordlord.gozer.components.wicket.datatable.GozerFilteredColumn;
import com.swordlord.gozer.components.wicket.detail.GWDetailActionPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.eventhandler.generic.GozerEvent;
import com.swordlord.gozer.eventhandler.generic.GozerEventListener;
import com.swordlord.gozer.eventhandler.generic.GozerFeedbackInfoEvent;
import com.swordlord.gozer.eventhandler.generic.GozerSelectionEvent;
import com.swordlord.gozer.eventhandler.generic.GozerUpdateUIEvent;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWListPanel extends GWPanel
{
	private static final int ROWS_PER_PAGE = 20;
	private GozerDataTable _table;
	private GList _list;
	private GWListPanel listPanel;
	private Form<?> _form;
	private WebMarkupContainer _div;
	
    /**
     * Flag indicating if a gozer feedback (
     * {@link GozerEventListener#gozerFeedbackInfoPerformed(GozerFeedbackInfoEvent, String)}
     * ) should be shown or not.
     */
    private boolean _showGozerFeedback = true;

    private class GozerListener implements GozerEventListener
    {
        @Override
        public void gozerEventPerformed(GozerEvent ge)
        {
            System.out.println("GWListPanel.gozerEventPerformed()");
            if (ge.getSource() instanceof GozerController)
            {
                // have the children re-render their content
                listPanel.updateComponents();
            }
        }

        @Override
        public void gozerSelectionPerformed(GozerSelectionEvent gse)
        {
        }

        @Override
        public void gozerUpdateUIPerformed(GozerUpdateUIEvent gui)
        {
            Object source = gui.getSource();
            if (source == null)
                return;
            
            listPanel.updateComponents();
        }

        @Override
        public void gozerFeedbackInfoPerformed(GozerFeedbackInfoEvent gui, String message)
        {
            sendFeedbackInfo(message);
        }
    }
    
	public GWListPanel(String id, IModel<?> model)
	{
		super(id, model);
		listPanel = this;
        GozerController gc = getGWContext().getFrameExtension().getGozerController();
        gc.addGozerEventListener(new GozerListener());

	}

	public GWListPanel(String id, IModel<?> model, GList glist, final Form<?> form)
	{
		super(id, model);
		
		_list = glist;
		_form = form;
		
		_div = new WebMarkupContainer("div");
        add(_div);

		// validate if this is within an hbox or not
		if(hasVBoxAsParent(glist))
		{
			_div.add(new AttributeModifier("id", new Model<String>("sub_element_halve")));
		}		
		
		IGozerFrameExtension fe = getFrameExtension();

		SortParam sortParam = null;

		String strOrdering = glist.getOrdering();
		if ((strOrdering != null) && (strOrdering.length() > 0))
		{
			sortParam = new SortParam(strOrdering, true);
		}

		GozerSortableFilterableDataProvider provider = new GozerSortableFilterableDataProvider(fe, glist, sortParam);

		DataBindingContext dbc = fe.getDataBindingContext();

        List<IColumn<DataRowBase, String>> columns = createColumns(glist, fe, dbc);

		int nRows = glist.getPageSize(ROWS_PER_PAGE);
		
        _table = new GozerDataTable("list", columns, provider, nRows, model, glist, form, this);
		_div.add(_table);
		
		listPanel = this;
        GozerController gc = getGWContext().getFrameExtension().getGozerController();
        gc.addGozerEventListener(new GozerListener());

	}
	
	public DataBinding getDataBinding()
	{
		return _list.getDataBinding();
	}

	public void addTopToolbar(AbstractToolbar toolbar)
	{
		_table.addTopToolbar(toolbar);
	}

    private List<IColumn<DataRowBase, String>> createColumns(GList list, final IGozerFrameExtension fe, final DataBindingContext dbc)
	{
        List<IColumn<DataRowBase, String>> columns = new ArrayList<IColumn<DataRowBase, String>>();
	
		final DataBinding dataBindingList = list.getDataBinding();

		for (final ObjectBase child : list.getChildren())
		{
			final String caption;
			if (child.getCaption() == null)
			{
				caption = null;
			}
			else
			{
				caption = translateCaption(child.getDataBinding().getDataBindingMember().getDataBindingTableName(),  child.getCaption());
			}
			
			final DataBinding dataBindingChild = child.getDataBinding();
			final DataBindingField dbField = dataBindingChild.getDataBindingField();
			
			String strSortProperty = child.getAttribute(ObjectBase.ATTRIBUTE_ORIGINAL_BINDING_MEMBER);
			
			if(strSortProperty != null)	strSortProperty = strSortProperty.replace("[0]", "");

			final boolean bFirst = list.getChildren().indexOf(child) == 0;
			
			if (child.getClass() == GField.class)
			{
				GozerFilteredColumn column = new GozerFilteredColumn(new Model<String>(caption), strSortProperty, bFirst, child)
				{
					// rowModel content is of type {@link DataRowBase}, see
					// {@link GozerSortableDataProvider}.
					@Override
					public void populateItem(Item cellItem, String componentId, IModel rowModel)
					{
						if (dbField.isForeignKey())
						{
							try
							{
								GWListFKeyFieldPanel detailFKeyFieldPanel = new GWListFKeyFieldPanel(componentId, rowModel, (GField) child, dataBindingChild, GWListPanel.this);
								detailFKeyFieldPanel.setRenderBodyOnly(true);
								cellItem.add(detailFKeyFieldPanel);
							}
							catch(Exception ex)
							{
								// fallback scenario, render field as normal field when there is no fkey info or constructor crashes...
								LOG.error("Fallback, rendering FKey Field as a normal field: " + ex.getMessage());
								cellItem.add(new GWListFieldPanel(componentId, rowModel, (GField) child, dataBindingChild, GWListPanel.this));
							}
						} 
						else
						{
							cellItem.add(new GWListFieldPanel(componentId, rowModel, (GField) child, dataBindingChild, GWListPanel.this));
						}
					}
				};
				columns.add(column);

			}
			else if (child.getClass() == GLinkField.class)
			{
				final DataBinding dataBindingLinkChild = ((GLinkField) child).getDataBindingLink();

				GozerFilteredColumn column = new GozerFilteredColumn(new Model<String>(caption), strSortProperty, bFirst, child)
				{
					// rowModel content is of type {@link DataRowBase}, see
					// {@link GozerSortableDataProvider}.
					@Override
					public void populateItem(Item cellItem, String componentId, IModel rowModel)
					{
						cellItem.add(new GWListLinkFieldPanel(componentId, rowModel, (GLinkField) child, dataBindingChild, dataBindingLinkChild, GWListPanel.this));
					}
				};
				columns.add(column);

			} 
			else if (child.getClass() == GCodeField.class)
			{
				GozerFilteredColumn column = new GozerFilteredColumn(new Model<String>(caption), strSortProperty, bFirst, child)
				{
					// rowModel content is of type {@link DataRowBase}, see
					// {@link GozerSortableDataProvider}.
					@Override
					public void populateItem(Item cellItem, String componentId, IModel rowModel)
					{
						cellItem.add(new GWListCodeFieldPanel(componentId, rowModel, (GCodeField) child, dataBindingChild, getGWContext()));
					}
				};
				columns.add(column);

			} 
            else if (child.getClass() == GOneToNField.class)
            {
                GozerFilteredColumn column = new GozerFilteredColumn(new Model<String>(caption), strSortProperty, bFirst, child)
                {
                    // rowModel content is of type {@link DataRowBase}, see
                    // {@link GozerSortableDataProvider}.
                    @Override
                    public void populateItem(Item cellItem, String componentId, IModel rowModel)
                    {
                        cellItem.add(new GWListOneToNFieldPanel(componentId, rowModel, (GOneToNField) child, dataBindingChild));
                    }
                };
                columns.add(column);

            }
			else if (child.getClass() == GActivateAction.class)
			{
				GozerColumn column = new GozerColumn(new Model<String>(caption), bFirst)
				{
					// rowModel content is of type {@link DataRowBase}, see
					// {@link GozerSortableDataProvider}.
					@Override
                    public void populateItem(Item cellItem, String componentId, IModel rowModel)
					{
						cellItem.add(new GWActivatePanel(componentId, rowModel, dataBindingList, (GActivateAction)child, fe));
					}

				};
				columns.add(column);
			} 
			else if (child.getClass() == GDetailAction.class)
			{
				GozerColumn column = new GozerColumn(new Model<String>(caption), bFirst)
				{
					// rowModel content is of type {@link DataRowBase}, see
					// {@link GozerSortableDataProvider}.
					@Override
                    public void populateItem(Item cellItem, String componentId, IModel rowModel)
					{
						cellItem.add(new GWDetailActionPanel(componentId, rowModel, dataBindingList, fe));
					}

				};
				columns.add(column);
			} 
			else if (child.getClass() == GActionToolbar.class) {
				//To avoid error - handle by yvc 14.09.2010
			}
			else
			{
				String error = MessageFormat.format("GozerFrame: {0}. Don''t know how to handle {1} in GWListPanel.createColumns.", 
													fe.getGozerLayoutFileName(), child.getClass().getName());
				
				LOG.warn(error);
			}
		}

		return columns;
	}
	
    /**
     * Updates the table by replacing it - thus to rerender it.
     */
    private void updateComponents()
    {
        final GozerSortableFilterableDataProvider provider = new GozerSortableFilterableDataProvider(getFrameExtension(), _list);
        applyFilterAndOrdering(_table, provider);

        final DataBindingContext dbc = getFrameExtension().getDataBindingContext();
        final List<IColumn<DataRowBase, String>> columns = createColumns(_list, getFrameExtension(), dbc);
        final int nRows = _list.getPageSize(ROWS_PER_PAGE);
        // replace table with a new instance
        try
        {
            _div.remove(_table);
            _table = new GozerDataTable("list", columns, provider, nRows, getModel(), _list, _form, this);
            _div.add(_table);
        }
        catch (Exception e)
        {
            LOG.error("Error while updating list panel " + getPageRelativePath(), e);
        }
    }

    /**
     * Applies the filter and sort state of the given table on the passed
     * provider.
     * 
     * @param table
     *            The table from which to get the settings (src)
     * @param provider
     *            The provider on which to apply the settings (dst)
     */
    private void applyFilterAndOrdering(GozerDataTable table, GozerSortableFilterableDataProvider provider)
    {
        final ISortState ordering = table.getSortState();
        if (ordering != null)
        {
            // TODO
            // fix sorting
            // provider.setSort(ordering);
        }
        final Object filter = table.getFilterState();
        if (filter != null)
        {
            provider.setFilterState(filter);
        }
    }

    /**
     * Flag indicating if a gozer feedback (
     * {@link GozerEventListener#gozerFeedbackInfoPerformed(GozerFeedbackInfoEvent, String)}
     * ) should be shown or not.
     * 
     * @return True if gozer feedbacks are shown, false otherwise
     */
    public boolean isShowGozerFeedback()
    {
        return _showGozerFeedback;
    }

    /**
     * Sets wherever gozer feedbacks (
     * {@link GozerEventListener#gozerFeedbackInfoPerformed(GozerFeedbackInfoEvent, String)}
     * ) should be shown or not.
     * 
     * @param showGozerFeedback
     *            True to show the feedbacks, false to suppress them
     */
    public void setShowGozerFeedback(boolean showGozerFeedback)
    {
        _showGozerFeedback = showGozerFeedback;
    }

    /**
     * Shows the given feedback message if {@link #isShowGozerFeedback()} is
     * true.
     * 
     * @param message
     *            A feedback message
     */
    private void sendFeedbackInfo(String message)
    {
        if (_showGozerFeedback)
        {
            info(message);
        }
    }
}
