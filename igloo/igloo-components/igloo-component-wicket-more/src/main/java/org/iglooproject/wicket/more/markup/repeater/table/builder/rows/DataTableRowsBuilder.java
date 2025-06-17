package org.iglooproject.wicket.more.markup.repeater.table.builder.rows;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.markup.repeater.table.builder.rows.state.IDataTableRowsState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IBuildState;

public abstract class DataTableRowsBuilder<T, S extends ISort<?>>
    implements IDataTableRowsState<T, S> {

  private final Builder<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
      rowsBehaviorFactoriesBuilder = ImmutableList.builder();

  public DataTableRowsBuilder() {
    super();
  }

  @Override
  public IDataTableRowsState<T, S> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories) {
    this.rowsBehaviorFactoriesBuilder.addAll(
        valueModelFactories.stream()
            // https://bugs.openjdk.java.net/browse/JDK-8212750 -> JDK-11 le type de f est
            // nécessaire dans la déclaration
            // corrigé dans JDK-12
            .map(
                (IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
                        f) ->
                    ((IDetachableFactory<IModel<? extends T>, Behavior>)
                        itemModel -> new ClassAttributeAppender(f.create(itemModel))))
            .collect(ImmutableList.toImmutableList()));
    return this;
  }

  @Override
  public IDataTableRowsState<T, S> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory) {
    return withClass(List.of(Objects.requireNonNull(valueModelFactory)));
  }

  @Override
  public IDataTableRowsState<T, S> withClass(IModel<? extends String> valueModel) {
    return withClass(DetachableFactories.constant(valueModel));
  }

  @Override
  public IDataTableRowsState<T, S> withClass(String firstValue, String... otherValues) {
    Lists.asList(Objects.requireNonNull(firstValue), otherValues).stream()
        .map(Model::of)
        .forEach(this::withClass);
    return this;
  }

  @Override
  public IDataTableRowsState<T, S> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          rowsBehaviorFactories) {
    this.rowsBehaviorFactoriesBuilder.addAll(Objects.requireNonNull(rowsBehaviorFactories));
    return this;
  }

  @Override
  public IDataTableRowsState<T, S> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> rowsBehaviorFactory) {
    return add(List.of(Objects.requireNonNull(rowsBehaviorFactory)));
  }

  @Override
  public IDataTableRowsState<T, S> add(Behavior firstRowsBehavior, Behavior... otherRowsBehaviors) {
    return add(
        Lists.asList(Objects.requireNonNull(firstRowsBehavior), otherRowsBehaviors).stream()
            .map(DetachableFactories::constant)
            .collect(ImmutableList.toImmutableList()));
  }

  @Override
  public IBuildState<T, S> end() {
    return onEnd(rowsBehaviorFactoriesBuilder.build());
  }

  protected abstract IBuildState<T, S> onEnd(
      List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          rowsBehaviorFactories);
}
