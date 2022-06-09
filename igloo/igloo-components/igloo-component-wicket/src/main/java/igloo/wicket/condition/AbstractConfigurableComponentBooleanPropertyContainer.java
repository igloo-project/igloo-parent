package igloo.wicket.condition;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * NE PAS RÉFÉRENCER CETTE CLASSE. Elle devrait être déplacée dans le sous-package 'impl' et être renommée
 * en AbstractPlaceholderEnclosureBehavior afin de créer un véritable AbstractHideableContainer, dont la seule fonction
 * implémentée est le fait de définir sa visibilité à chaque onConfigure() (cf. {@link AbstractComponentBooleanPropertyBehavior}).
 */
public abstract class AbstractConfigurableComponentBooleanPropertyContainer<T extends AbstractConfigurableComponentBooleanPropertyContainer<T>>
		extends WebMarkupContainer
		implements IPlaceholderEnclosureBuilder<T> {
	
	private static final long serialVersionUID = -4570949966472824133L;
	
	private final AbstractConfigurableComponentBooleanPropertyBehavior<?> behavior;

	protected AbstractConfigurableComponentBooleanPropertyContainer(String wicketId, AbstractConfigurableComponentBooleanPropertyBehavior<?> behavior) {
		super(wicketId);
		this.behavior = behavior;
		add(behavior);
	}
	
	/**
	 * @return this as an object of type T
	 * @see PlaceholderBehavior
	 * @see EnclosureBehavior
	 */
	protected abstract T thisAsT();
	
	@Override
	public T condition(Condition condition) {
		behavior.condition(condition);
		return thisAsT();
	}
	
	public T anyChildVisible() {
		behavior.condition(Condition.anyChildVisible(this));
		return thisAsT();
	}
	
	public T anyChildEnabled() {
		behavior.condition(Condition.anyChildEnabled(this));
		return thisAsT();
	}

}
