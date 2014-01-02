package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public interface IPlaceholderEnclosureBuilder<T> {

	T component(Component component);

	/**
	 * Shorthand for <code>builder.component(firstComponent).component(secondComponent).component(thirdComponent) ...</code>
	 */
	T components(Component firstComponent, Component... otherComponents);
	
	T components(List<Component> components);

	T model(IModel<?> model);

	T collectionModel(IModel<? extends Collection<?>> model);

	/**
	 * Adds a <code>model</code> that will be considered "non-empty" if and only if <code>predicate.apply(model == null ? null : model.getObject())</code> returns <code>true</code>.
	 * <p><strong>WARNING:</strong> <code>predicate</code> must be serializable in order for this object to be serializable.
	 * Also, it must implement IDetachable in order to be properly detached by this object. {@link Predicates#and(Predicate, Predicate)}, for instance, will produce not-detachable predicates.
	 */
	<T2> T model(Predicate<? super T2> predicate, IModel<? extends T2> model);

	/**
	 * Shorthand for <code>builder.model(firstModel).model(secondModel).model(thirdModel) ...</code>
	 */
	T models(IModel<?> firstModel, IModel<?>... otherModels);

	/**
	 * Shorthand for <code>builder.model(predicate, firstModel).model(predicate, secondModel).model(predicate, thirdModel) ...</code>
	 * <p><strong>WARNING:</strong> <code>predicate</code> must be serializable in order for this object to be serializable.
	 * Also, it must implement IDetachable in order to be properly detached by this object. {@link Predicates#and(Predicate, Predicate)}, for instance, will produce not-detachable predicates.
	 */
	<T2> T models(Predicate<? super T2> predicate, IModel<? extends T2> firstModel, IModel<? extends T2>... otherModels);

}
