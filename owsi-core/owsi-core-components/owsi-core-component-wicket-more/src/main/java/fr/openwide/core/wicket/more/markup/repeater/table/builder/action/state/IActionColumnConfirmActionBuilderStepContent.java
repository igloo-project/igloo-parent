package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterModelFactory;

public interface IActionColumnConfirmActionBuilderStepContent<T, S extends ISort<?>> {

	IActionColumnConfirmActionBuilderStepEndContent<T, S> content(IModel<String> contentModel);

	IActionColumnConfirmActionBuilderStepEndContent<T, S> content(IOneParameterModelFactory<? super IModel<T>, String> contentModelFactory);

}
