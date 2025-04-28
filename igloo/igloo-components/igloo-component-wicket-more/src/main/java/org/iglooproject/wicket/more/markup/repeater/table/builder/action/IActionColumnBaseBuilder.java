package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import java.util.Collection;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

public interface IActionColumnBaseBuilder<T>
    extends IOneParameterComponentFactory<MarkupContainer, IModel<T>> {

  IActionColumnBaseBuilder<T> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);
}
