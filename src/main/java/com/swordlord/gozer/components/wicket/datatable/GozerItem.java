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
 ** $Id: GozerItem.java 1371 2013-05-26 15:48:00Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.datatable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryAjaxEventBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.box.GWFrame;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GozerItem extends OddEvenItem<DataRowBase>
{
	//protected static final Log LOG = LogFactory.getLog(REPLACEME);
	protected GozerDataTable _gdt;

	public GozerItem(String id, int index, IModel<DataRowBase> model, GozerDataTable gdt)
	{
		super(id, index, model);

		_gdt = gdt;
		
		// selects the current row...
		OnClickRowBehaviour click = new OnClickRowBehaviour();
		// no throttling anymore, since there is no double click
		add(click);

		// no double click anymore, resulted in errors anyway...
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBeforeRender()
    {
        super.onBeforeRender();
        //highlight current row if desired
        if (_gdt.isMarkCurrentRow() && getModelObject().getKey().equals(_gdt.getDataBinding().getCurrentRow().getKey()))
        {
            add(new AttributeModifier("class", new Model<String>("current")));
        }
    }

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
        tag.put("onmouseover", "style.backgroundColor='#FFF7C6';");
        tag.put("onmouseout", "style.backgroundColor='';");
	}

	protected class OnClickRowBehaviour extends WiQueryAjaxEventBehavior
	{
        public OnClickRowBehaviour() 
        {
			super(MouseEvent.CLICK);
		}
        
        @Override
        protected void onEvent(AjaxRequestTarget target) 
		{
        	// Set current row to the one which was selected
        	IModel<DataRowBase> model = getModel();
    		DataRowBase row = model.getObject();
    		
            DataBinding db = _gdt.getDataBinding();

    		// the databinding member of the list is the correct dbm to move when somebody clicked on the list
    		DataBindingMember dbm = db.getDataBindingMember();
    		
    		// Have the list being re-rendered - but only if something was moved
    		if(db.getDataBindingManager().move(dbm, row.getKey()))
    		{
                final GWPanel parent = _gdt.getGWParent();

                // whenever possible we refresh the complete gozer form - thus
                // to refresh dependent elements too
                final GWFrame form = parent.getGWContext().getFormPanel();
                if (form != null)
                {
                    target.add(form);
                }
                else
	    		{
                    target.add(parent);
	    		}
    		}
		}

        /**
         * {@inheritDoc}
         */
        @Override
        public JsStatement statement()
        {
            // TODO Auto-generated method stub
            return null;
        }
	}
	
	/*
	protected class OnDblClickRowBehaviour extends WiQueryAjaxEventBehavior
	{
        public OnDblClickRowBehaviour() 
        {
			super(MouseEvent.CLICK, KeyboardEvent.KEYDOWN);
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) 
		{
        	LOG.info("dblclick");
			/*
			// Set current row to the one which was selected
        	IModel<?> model = getModel();
    		DataRowBase row = (DataRowBase) model.getObject();

    		// the databinding member of the list is the correct dbm to move when somebody clicked on the list
    		DataBindingMember dbm = _list.getDataBinding().getDataBindingMember();

    		_db.getDataBindingManager().moveRoot(dbm, row.getKey());
    		
    		// Then find the detail page and jump!
    		Page page = _list.getGWContext().getFrameExtension().getGozerController().detailAction(row);
        	if(page != null)
			{
        		ContentPage tp = (ContentPage) page;
        		tp.setLastPage(getPage());
        		
				setResponsePage(page);
			}
			
		}
	}
	*/
}
