package org.iglooproject.wicket.more.markup.repeater.table.builder.rows.state;

import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IBuildState;

public interface IDataTableRowsState<T, S extends ISort<?>> {

  IDataTableRowsState<T, S> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  IDataTableRowsState<T, S> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  IDataTableRowsState<T, S> withClass(IModel<? extends String> valueModel);

  IDataTableRowsState<T, S> withClass(String firstValue, String... otherValues);

  IDataTableRowsState<T, S> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          rowsBehaviorFactories);

  IDataTableRowsState<T, S> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> rowsBehaviorFactory);

  IDataTableRowsState<T, S> add(Behavior firstRowsBehavior, Behavior... otherRowsBehaviors);

  IBuildState<T, S> end();
}
