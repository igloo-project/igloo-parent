package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.ActionColumnBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.ICoreColumn;
import org.iglooproject.wicket.more.rendering.Renderer;
import org.iglooproject.wicket.more.util.IDatePattern;


public interface IColumnState<T, S extends ISort<?>> extends IBuildState<T, S> {

	IAddedColumnState<T, S> addColumn(IColumn<T, S> column);

	IAddedCoreColumnState<T, S> addColumn(ICoreColumn<T, S> column);

	IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel);

	IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Renderer<? super T> renderer);

	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			ICoreBinding<? super T, C> binding);

	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			ICoreBinding<? super T, C> binding, Renderer<? super C> renderer);

	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, SerializableFunction2<? super T, C> function);

	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, SerializableFunction2<? super T, C> function,
			Renderer<? super C> renderer);

	IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			ICoreBinding<? super T, ? extends Date> binding, IDatePattern datePattern);

	<C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
			ICoreBinding<? super T, C> binding, BootstrapRenderer<? super C> renderer);

	<C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
			SerializableFunction2<? super T, C> function, BootstrapRenderer<? super C> renderer);

	IAddedBooleanLabelColumnState<T, S> addBooleanLabelColumn(IModel<String> headerModel,
			ICoreBinding<? super T, Boolean> binding);

	ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn();

	ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn(IModel<String> headerLabelModel);

}
