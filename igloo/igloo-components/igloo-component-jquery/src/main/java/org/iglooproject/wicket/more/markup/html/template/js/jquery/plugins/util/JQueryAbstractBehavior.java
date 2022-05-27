package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

public class JQueryAbstractBehavior extends Behavior {

	private static final long serialVersionUID = 1663031730842864720L;

	private Component component;

	public JQueryAbstractBehavior()
	{
	}

	/**
	 * Bind this handler to the given component.
	 * 
	 * @param hostComponent
	 *            the component to bind to
	 */
	@Override
	public final void bind(final Component hostComponent)
	{
		if (hostComponent == null)
		{
			throw new IllegalArgumentException("Argument hostComponent must be not null");
		}

		if (component != null)
		{
			throw new IllegalStateException("this kind of handler cannot be attached to "
				+ "multiple components; it is already attached to component " + component
				+ ", but component " + hostComponent + " wants to be attached too");
		}

		component = hostComponent;

		// call the callback
		onBind();
	}

	/**
	 * Called when the component was bound to it's host component. You can get the bound
	 * host component by calling getComponent.
	 */
	protected void onBind()
	{
		getComponent().setOutputMarkupId(true);
	}

	/**
	 * @return the binded component
	 */
	public Component getComponent()
	{
		return component;
	}
}
