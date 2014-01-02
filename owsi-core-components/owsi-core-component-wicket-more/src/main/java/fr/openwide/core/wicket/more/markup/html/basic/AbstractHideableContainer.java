package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;

import fr.openwide.core.wicket.more.markup.html.basic.impl.PlaceholderEnclosureVisibilityBuilder;
import fr.openwide.core.wicket.more.markup.html.basic.impl.PlaceholderEnclosureVisibilityBuilder.Visibility;

/**
 * NE PAS RÉFÉRENCER CETTE CLASSE. Elle devrait être déplacée dans le sous-package 'impl' et être renommée
 * en AbstractPlaceholderEnclosureBehavior afin de créer un véritable AbstractHideableContainer, dont la seule fonction
 * implémentée est le fait de définir sa visibilité à chaque onConfigure() (cf. AbstractHidingBehavior).
 */
public abstract class AbstractHideableContainer<T extends AbstractHideableContainer<T>>
		extends WebMarkupContainer
		implements IPlaceholderEnclosureBuilder<T> {
	
	private static final long serialVersionUID = -4570949966472824133L;

	private final PlaceholderEnclosureVisibilityBuilder visibilityBuilder;
	
	protected AbstractHideableContainer(String wicketId, Visibility visibility) {
		super(wicketId);
		this.visibilityBuilder = new PlaceholderEnclosureVisibilityBuilder(visibility);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		setVisible(visibilityBuilder.isVisible());
	}
	
	/**
	 * @return this as an object of type T
	 * @see PlaceholderBehavior
	 * @see EnclosureBehavior
	 */
	protected abstract T thisAsT();
	
	@Override
	public T collectionModel(IModel<? extends Collection<?>> model) {
		visibilityBuilder.collectionModel(model);
		return thisAsT();
	}
	
	@Override
	public T model(IModel<?> model) {
		visibilityBuilder.model(model);
		return thisAsT();
	}
	
	@Override
	public <T2> T model(Predicate<? super T2> predicate, IModel<? extends T2> model) {
		visibilityBuilder.model(predicate, model);
		return thisAsT();
	}
	
	@Override
	public T models(IModel<?> firstModel, IModel<?>... otherModels) {
		visibilityBuilder.models(firstModel, otherModels);
		return thisAsT();
	}
	
	@Override
	public <T2> T models(Predicate<? super T2> predicate, IModel<? extends T2> firstModel, IModel<? extends T2>... otherModels) {
		visibilityBuilder.models(predicate, firstModel, otherModels);
		return thisAsT();
	}
	
	@Override
	public T component(Component component) {
		visibilityBuilder.component(component);
		return thisAsT();
	}
	
	@Override
	public T components(Component firstComponent, Component... otherComponents) {
		visibilityBuilder.components(firstComponent, otherComponents);
		return thisAsT();
	}
	
	@Override
	public T components(List<Component> components) {
		visibilityBuilder.components(components);
		return thisAsT();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		visibilityBuilder.detach();
	}

}
