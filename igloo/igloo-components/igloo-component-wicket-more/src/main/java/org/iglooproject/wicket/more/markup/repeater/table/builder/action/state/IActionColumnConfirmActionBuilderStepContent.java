package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import igloo.wicket.factory.IDetachableFactory;


public interface IActionColumnConfirmActionBuilderStepContent<T, I> {

	IActionColumnConfirmActionBuilderStepEndContent<T, I> content(IModel<String> contentModel);

	IActionColumnConfirmActionBuilderStepEndContent<T, I> content(
			IDetachableFactory<? super IModel<T>, ? extends IModel<String>> contentModelFactory);

}
