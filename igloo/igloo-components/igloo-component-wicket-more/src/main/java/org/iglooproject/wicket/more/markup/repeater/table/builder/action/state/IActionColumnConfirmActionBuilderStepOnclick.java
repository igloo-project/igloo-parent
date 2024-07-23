package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.action.IOneParameterAjaxAction;
import org.apache.wicket.model.IModel;

public interface IActionColumnConfirmActionBuilderStepOnclick<T, I> {

  IActionColumnAddedConfirmActionState<T, I> onClick(
      IOneParameterAjaxAction<? super IModel<T>> onClick);
}
