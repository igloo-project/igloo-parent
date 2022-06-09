package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

import igloo.wicket.action.IOneParameterAjaxAction;

public interface IActionColumnConfirmActionBuilderStepOnclick<T, I> {

	IActionColumnAddedConfirmActionState<T, I> onClick(IOneParameterAjaxAction<? super IModel<T>> onClick);

}
