package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;


public interface IActionColumnConfirmActionBuilderStepContent<T, I> {

	IActionColumnConfirmActionBuilderStepEndContent<T, I> content(IModel<String> contentModel);

	IActionColumnConfirmActionBuilderStepEndContent<T, I> content(
			IDetachableFactory<? super IModel<T>, ? extends IModel<String>> contentModelFactory);

}
