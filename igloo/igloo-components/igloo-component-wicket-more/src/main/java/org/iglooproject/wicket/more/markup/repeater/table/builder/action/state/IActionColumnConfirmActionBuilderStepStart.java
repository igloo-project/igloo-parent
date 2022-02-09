package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.factory.IDetachableFactory;

public interface IActionColumnConfirmActionBuilderStepStart<T, I> {

	IActionColumnConfirmActionBuilderStepContent<T, I> title(IModel<String> titleModel);

	IActionColumnConfirmActionBuilderStepContent<T, I> title(
			IDetachableFactory<? super IModel<T>, ? extends IModel<String>> titleModelFactory);

	IActionColumnConfirmActionBuilderStepOnclick<T, I> deleteConfirmation();

}
