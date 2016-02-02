package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterModelFactory;

public interface IActionColumnConfirmActionBuilderStepStart<T, S extends ISort<?>> {

	IActionColumnConfirmActionBuilderStepContent<T, S> title(IModel<String> titleModel);

	IActionColumnConfirmActionBuilderStepContent<T, S> title(IOneParameterModelFactory<IModel<T>, String> titleModelFactory);

	IActionColumnConfirmActionBuilderStepOnclick<T, S> deleteConfirmation();

}
