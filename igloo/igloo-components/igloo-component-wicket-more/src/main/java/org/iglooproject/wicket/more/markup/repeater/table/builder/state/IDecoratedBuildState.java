package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnNoParameterBuildState;

public interface IDecoratedBuildState<T, S extends ISort<?>> {

  IDecoratedBuildState<T, S> responsive(Condition responsiveCondition);

  IDecoratedBuildState<T, S> title(String resourceKey);

  IDecoratedBuildState<T, S> title(IModel<?> model);

  IDecoratedBuildState<T, S> title(IComponentFactory<?> addInComponentFactory);

  IDecoratedBuildState<T, S> count(String countResourceKey);

  IDecoratedBuildState<T, S> count(AddInPlacement placement, String countResourceKey);

  IDecoratedBuildState<T, S> pagers();

  IDecoratedBuildState<T, S> pagers(int viewSize);

  IDecoratedBuildState<T, S> pager(AddInPlacement placement);

  IDecoratedBuildState<T, S> pager(AddInPlacement placement, int viewSize);

  IDecoratedBuildState<T, S> ajaxPagers();

  IDecoratedBuildState<T, S> ajaxPagers(int viewSize);

  IDecoratedBuildState<T, S> ajaxPager(AddInPlacement placement);

  IDecoratedBuildState<T, S> ajaxPager(AddInPlacement placement, int viewSize);

  IDecoratedBuildState<T, S> addIn(
      AddInPlacement placement, IComponentFactory<?> addInComponentFactory);

  IDecoratedBuildState<T, S> addIn(
      AddInPlacement placement, IComponentFactory<?> addInComponentFactory, String cssClasses);

  IDecoratedBuildState<T, S> addIn(
      AddInPlacement placement,
      IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>
          addInComponentFactory);

  IDecoratedBuildState<T, S> addIn(
      AddInPlacement placement,
      IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>
          addInComponentFactory,
      String cssClasses);

  DecoratedCoreDataTablePanel<T, S> build(String id);

  DecoratedCoreDataTablePanel<T, S> build(String id, long rowsPerPage);

  <Z> IActionColumnBuildState<Z, IDecoratedBuildState<T, S>> actions(
      AddInPlacement placement, IModel<Z> model);

  IActionColumnNoParameterBuildState<Void, IDecoratedBuildState<T, S>> actions(
      AddInPlacement placement);
}
