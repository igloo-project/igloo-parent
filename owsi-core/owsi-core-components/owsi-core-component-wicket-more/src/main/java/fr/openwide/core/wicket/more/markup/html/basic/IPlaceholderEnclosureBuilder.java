package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public interface IPlaceholderEnclosureBuilder<T> {

	T components(Component firstComponent, Component... otherComponents);

	T component(Component component);

	T models(IModel<?> firstModel, IModel<?>... otherModels);

	T model(IModel<?> model);

	T collectionModel(IModel<? extends Collection<?>> model);

}
