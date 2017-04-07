package com.swordlord.gozer.components.wicket.detail;

import org.apache.commons.lang.NotImplementedException;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWDetailActionPanel extends GWPanel implements IGozerAction
{
	private DataBinding _dataBinding;

    public GWDetailActionPanel(String id, IModel<?> model, DataBinding dataBinding, final IGozerFrameExtension fe)
	{
		super(id, model);

		_dataBinding = dataBinding;
        final Link<?> actionLink = new Link<Object>("link")
        {
            @Override
			public void onClick()
            {
                Page page = fe.getGozerController().detailAction(getRow());
            	if(page != null)
				{
					setResponsePage(page);
				}
            }
        };
        actionLink.add(new Label("caption", new Model<String>("Detail")));
        add(actionLink);
	}

	@Override
    public String getActionID()
	{
		return "";
	}

	@Override
    public DataBinding getDataBinding()
	{
		return _dataBinding;
	}
	
	@Override
    public DataBinding getDataBindingTwo()
	{
		throw new NotImplementedException();
	}

	public DataRowBase getRow()
	{
		return (DataRowBase) getModelObject();
	}


	@Override
    public void setActionID()
    {
    }
}
