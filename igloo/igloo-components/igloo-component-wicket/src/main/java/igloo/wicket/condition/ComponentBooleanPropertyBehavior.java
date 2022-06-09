package igloo.wicket.condition;

public class ComponentBooleanPropertyBehavior extends AbstractConfigurableComponentBooleanPropertyBehavior<ComponentBooleanPropertyBehavior> {

	private static final long serialVersionUID = -4703385998400963974L;

	public ComponentBooleanPropertyBehavior(ComponentBooleanProperty property, Operator operator) {
		super(property, operator);
	}

	@Override
	public ComponentBooleanPropertyBehavior thisAsT() {
		return this;
	}

}
