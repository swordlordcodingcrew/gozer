package com.swordlord.gozer.components.wicket.action.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.eventhandler.generic.GozerActionEvent;
import com.swordlord.gozer.eventhandler.generic.GozerController;

/**
 * An ajax submit image button that will degrade to a normal request if ajax is
 * not available or javascript is disabled.
 * 
 */
@SuppressWarnings("serial")
public abstract class GWAbstractAjaxFallbackAction extends GWAbstractAction
{
	private Form<?> mForm;

    /**
     * @param id
     * @param gc
     * @param actionBase
     * @param resource
     * @param form
     */
    public GWAbstractAjaxFallbackAction(String id, GozerController gc, GActionBase actionBase, PackageResourceReference resource, Form<?> form)
	{
		super(id, gc, actionBase, resource);

		initialize(form);
	}

	private void initialize(Form<?> form)
	{
		mForm = form;

		add(new AjaxFormSubmitBehavior(form, "onclick")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				GWAbstractAjaxFallbackAction.this.onSubmit(target, GWAbstractAjaxFallbackAction.this.getForm());
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
				GWAbstractAjaxFallbackAction.this.onError(target, GWAbstractAjaxFallbackAction.this.getForm());
			}

			/*
			@Override
			protected CharSequence getEventHandler()
			{
				return new AppendingStringBuffer(super.getEventHandler()).append("; return false;");
			}

			@Override
			protected IAjaxCallListener getAjaxCallDecorator()
			{
				return GWAbstractAjaxFallbackAction.this.getAjaxCallDecorator();
			}
			*/
		});
	}

	/**
	 * Listener method invoked on form submit with errors
	 * 
	 * @param target
	 * @param form
	 * 
	 *            TODO 1.3: Make abstract to be consistent with onsubmit()
	 */
	protected void onError(AjaxRequestTarget target, Form<?> form)
	{
		// created to override
	}

	/**
	 * @see org.apache.wicket.markup.html.form.IFormSubmittingComponent#onSubmit()
	 */
	@Override
	public void onSubmit()
	{
        AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class);
        if (target == null)
        {
            onSubmit(null, getForm());
        }
	}

	/**
	 * 
	 * @see org.apache.wicket.markup.html.form.Button#getForm()
	 */
	@Override
	public Form<?> getForm()
	{
		return mForm == null ? super.getForm() : mForm;
	}

	/**
	 * Callback for the onClick event. If ajax failed and this event was
	 * generated via a normal submission, the target argument will be null
	 * 
	 * @param target
	 *            ajax target if this linked was invoked using ajax, null
	 *            otherwise
	 * @param form
	 */
	protected void onSubmit(final AjaxRequestTarget target, final Form<?> form)
	{
		GozerActionEvent event = new GozerActionEvent(this);
		event.setTarget(target);
		_gc.genericActionPerformed(event);
        target.add(form);
		// remain on this page, hence do not set response page
	}


	/**
	 * 
	 * @return call decorator to use or null if none
	 */
    /*
     * protected IAjaxCallDecorator getAjaxCallDecorator() { return null; }
     */
}
