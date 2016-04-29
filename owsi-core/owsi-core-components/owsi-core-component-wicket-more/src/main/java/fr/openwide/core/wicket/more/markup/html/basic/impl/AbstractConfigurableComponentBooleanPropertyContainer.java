package fr.openwide.core.wicket.more.markup.html.basic.impl;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.AbstractComponentBooleanPropertyBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.IPlaceholderEnclosureBuilder;

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
	
	/**
	 * @deprecated Use {@link Condition}.collectionModelNotEmpty instead.
	 */
	@Deprecated
	@Override
	public T collectionModel(IModel<? extends Collection<?>> model) {
		behavior.collectionModel(model);
		return thisAsT();
	}
	
	/**
	 * @deprecated Use {@link Condition}.modelNotNull instead.
	 */
	@Deprecated
	@Override
	public T model(IModel<?> model) {
		behavior.model(model);
		return thisAsT();
	}
	
	/**
	 * @deprecated Use {@link Condition}.predicate instead
	 */
	@Deprecated
	@Override
	public <T2> T model(Predicate<? super T2> predicate, IModel<? extends T2> model) {
		behavior.model(predicate, model);
		return thisAsT();
	}
	
	/**
	 * @deprecated Use {@link Condition}.modelsAnyNotNull instead
	 */
	@Deprecated
	@Override
	public T models(IModel<?> firstModel, IModel<?>... otherModels) {
		behavior.models(firstModel, otherModels);
		return thisAsT();
	}
	
	/**
	 * Use {@link Condition}.predicateAnyTrue
	 */
	@Deprecated
	@Override
	@SafeVarargs
	public final <T2> T models(Predicate<? super T2> predicate, IModel<? extends T2> firstModel,
			IModel<? extends T2>... otherModels) {
		behavior.models(predicate, firstModel, otherModels);
		return thisAsT();
	}
	
	/**
	 * @deprecated Use {@link Condition}.componentVisible instead
	 */
	@Deprecated
	@Override
	public T component(Component component) {
		behavior.component(component);
		return thisAsT();
	}
	
	/**
	 * @deprecated Use {@link Condition}.componentsAnyVisible instead
	 */
	@Deprecated
	@Override
	public T components(Component firstComponent, Component... otherComponents) {
		behavior.components(firstComponent, otherComponents);
		return thisAsT();
	}
	
	/**
	 * @deprecated Use {@link Condition}.componentsAnyVisible instead
	 */
	@Deprecated
	@Override
	public T components(Collection<? extends Component> components) {
		behavior.components(components);
		return thisAsT();
	}
	
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
