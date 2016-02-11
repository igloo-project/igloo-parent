package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IActionColumnConfirmActionBuilderStepNo<T, S extends ISort<?>> {

	IActionColumnConfirmActionBuilderStepOnclick<T, S> no(IModel<String> noLabelModel);

}
