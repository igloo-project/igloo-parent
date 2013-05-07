package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public abstract class AbstractHideableContainer extends WebMarkupContainer {
	
	private static final long serialVersionUID = -4570949966472824133L;
	
	private final AbstractHideableBehavior hideableBehavior;
	
	protected AbstractHideableContainer(String id, AbstractHideableBehavior hideableBehavior) {
		super(id);
		this.hideableBehavior = hideableBehavior;
		add(hideableBehavior);
	}
	
	public AbstractHideableContainer collectionModel(IModel<? extends Collection<?>> model) {
		hideableBehavior.collectionModel(model);
		return this;
	}
	
	/**
	 * @deprecated Use {@link #collectionModel(IModel)}
	 */
	@Deprecated
	public AbstractHideableContainer listModel(IModel<? extends List<?>> model) {
		hideableBehavior.collectionModel(model);
		return this;
	}
	
	public AbstractHideableContainer model(IModel<?> model) {
		hideableBehavior.model(model);
		return this;
	}
	
	public AbstractHideableContainer models(IModel<?>... model) {
		hideableBehavior.models(model);
		return this;
	}
	
	public AbstractHideableContainer component(Component component) {
		hideableBehavior.component(component);
		return this;
	}
	
	public AbstractHideableContainer components(Component... component) {
		hideableBehavior.components(component);
		return this;
	}

}
