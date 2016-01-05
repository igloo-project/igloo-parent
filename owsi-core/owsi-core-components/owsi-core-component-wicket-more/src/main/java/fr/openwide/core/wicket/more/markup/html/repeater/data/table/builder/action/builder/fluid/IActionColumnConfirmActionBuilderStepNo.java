package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.builder.fluid;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IActionColumnConfirmActionBuilderStepNo<T, S extends ISort<?>> {

	IActionColumnConfirmActionBuilderStepOnclick<T, S> no(IModel<String> noLabelModel);

}
