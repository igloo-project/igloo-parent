package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

public interface IActionColumnConfirmActionBuilderStepNo<T, I> {

  IActionColumnConfirmActionBuilderStepOnclick<T, I> no(IModel<String> noLabelModel);
}
