package fr.openwide.core.wicket.more.markup.html.basic;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

public abstract class AbstractHidingBehavior extends Behavior {
	
	private static final long serialVersionUID = -1592398964983541809L;

	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		setVisibility(component, isVisible(component));
	}

	protected abstract boolean isVisible(Component component);

	protected abstract void setVisibility(Component component, boolean visible);

}
