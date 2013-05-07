package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public abstract class AbstractHideableContainer<T extends AbstractHideableContainer<T>> extends WebMarkupContainer {
	
	private static final long serialVersionUID = -4570949966472824133L;
	
	private final AbstractHideableBehavior hideableBehavior;
	
	protected AbstractHideableContainer(String id, AbstractHideableBehavior hideableBehavior) {
		super(id);
		this.hideableBehavior = hideableBehavior;
		add(hideableBehavior);
	}
	
	/**
	 * @return this as an object of type T
	 * @see PlaceholderContainer
	 * @see EnclosureContainer
	 */
	protected abstract T thisAsT();
	
	public T collectionModel(IModel<? extends Collection<?>> model) {
		hideableBehavior.collectionModel(model);
		return thisAsT();
	}

	/**
	 * @deprecated Use {@link #collectionModel(IModel)}
	 */
	@Deprecated
	public T listModel(IModel<? extends List<?>> model) {
		hideableBehavior.collectionModel(model);
		return thisAsT();
	}
	
	public T model(IModel<?> model) {
		hideableBehavior.model(model);
		return thisAsT();
	}
	
	public T models(IModel<?>... model) {
		hideableBehavior.models(model);
		return thisAsT();
	}
	
	public T component(Component component) {
		hideableBehavior.component(component);
		return thisAsT();
	}
	
	public T components(Component... component) {
		hideableBehavior.components(component);
		return thisAsT();
	}

}
