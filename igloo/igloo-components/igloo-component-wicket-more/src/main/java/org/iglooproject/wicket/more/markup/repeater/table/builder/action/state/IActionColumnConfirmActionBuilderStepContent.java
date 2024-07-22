package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.wicket.factory.IDetachableFactory;
import org.apache.wicket.model.IModel;

public interface IActionColumnConfirmActionBuilderStepContent<T, I> {

  IActionColumnConfirmActionBuilderStepEndContent<T, I> content(IModel<String> contentModel);

  IActionColumnConfirmActionBuilderStepEndContent<T, I> content(
      IDetachableFactory<? super IModel<T>, ? extends IModel<String>> contentModelFactory);
}
