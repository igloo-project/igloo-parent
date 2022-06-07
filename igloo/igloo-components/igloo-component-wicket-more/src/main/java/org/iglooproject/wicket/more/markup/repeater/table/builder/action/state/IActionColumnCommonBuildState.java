package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.factory.IDetachableFactory;

public interface IActionColumnCommonBuildState<T, I> {

	IActionColumnCommonBuildState<T, I> withClassOnElements(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>> valueModelFactories);

	IActionColumnCommonBuildState<T, I> withClassOnElements(IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>> valueModelFactory);

	IActionColumnCommonBuildState<T, I> withClassOnElements(IModel<? extends String> valueModel);

	IActionColumnCommonBuildState<T, I> withClassOnElements(String firstValue, String... otherValues);

	I end();

}
