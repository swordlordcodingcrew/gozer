package com.swordlord.gozer.components.wicket;

import org.apache.wicket.model.IModel;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.wicket.action.button.list.GWActivateButton;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datarow.DataRowBase;

@SuppressWarnings("serial")
public class GWActivatePanel extends GWPanel
{
	public GWActivatePanel(String id, IModel<?> model, DataBinding dataBinding, GActionBase actionBase, IGozerFrameExtension fe)
	{
		super(id, model);
        add(new GWActivateButton("action", fe.getGozerController(), actionBase, getRow().getKey()));
	}

	public DataRowBase getRow()
	{
		return (DataRowBase) getModelObject();
	}
}
