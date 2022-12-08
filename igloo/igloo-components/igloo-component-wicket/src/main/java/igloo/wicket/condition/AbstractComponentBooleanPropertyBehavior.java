package igloo.wicket.condition;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.lang.Args;

public abstract class AbstractComponentBooleanPropertyBehavior extends Behavior {
	
	private static final long serialVersionUID = -4569686564756782933L;
	
	private final ComponentBooleanProperty property;
	
	public AbstractComponentBooleanPropertyBehavior(ComponentBooleanProperty property) {
		super();
		Args.notNull(property, "property");
		this.property = property;
	}

	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		property.set(component, generatePropertyValue(component));
	}

	protected abstract boolean generatePropertyValue(Component component);

}