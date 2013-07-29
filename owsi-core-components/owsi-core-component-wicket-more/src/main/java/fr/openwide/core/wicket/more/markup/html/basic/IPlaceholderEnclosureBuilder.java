package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public interface IPlaceholderEnclosureBuilder<T> {

	public abstract T components(Component firstComponent, Component... otherComponents);

	public abstract T component(Component component);

	public abstract T models(IModel<?> firstModel, IModel<?>... otherModels);

	public abstract T model(IModel<?> model);

	public abstract T collectionModel(IModel<? extends Collection<?>> model);

}
