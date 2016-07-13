package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;

public interface IActionColumnConfirmActionBuilderStepOnclick<T, I> {

	IActionColumnAddedConfirmActionState<T, I> onClick(IOneParameterAjaxAction<? super IModel<T>> onClick);

}
