package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.model.IModel;

public interface IActionColumnConfirmActionBuilderStepEndContent<T, I> {

  IActionColumnConfirmActionBuilderStepEndContent<T, I> keepMarkup();

  IActionColumnConfirmActionBuilderStepEndContent<T, I> cssClassNamesModel(
      IModel<String> cssClassNamesModel);

  IActionColumnConfirmActionBuilderStepNo<T, I> yes(IModel<String> yesLabelModel);

  IActionColumnConfirmActionBuilderStepOnclick<T, I> yesNo();

  IActionColumnConfirmActionBuilderStepOnclick<T, I> confirm();

  IActionColumnConfirmActionBuilderStepOnclick<T, I> validate();

  IActionColumnConfirmActionBuilderStepOnclick<T, I> save();
}
