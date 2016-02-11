package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IActionColumnConfirmActionBuilderStepEndContent<T, S extends ISort<?>> {

	IActionColumnConfirmActionBuilderStepEndContent<T, S> keepMarkup();

	IActionColumnConfirmActionBuilderStepEndContent<T, S> cssClassNamesModel(IModel<String> cssClassNamesModel);

	IActionColumnConfirmActionBuilderStepNo<T, S> yes(IModel<String> yesLabelModel);

	IActionColumnConfirmActionBuilderStepOnclick<T, S> yesNo();

	IActionColumnConfirmActionBuilderStepOnclick<T, S> confirm();

	IActionColumnConfirmActionBuilderStepOnclick<T, S> validate();

	IActionColumnConfirmActionBuilderStepOnclick<T, S> save();

}
