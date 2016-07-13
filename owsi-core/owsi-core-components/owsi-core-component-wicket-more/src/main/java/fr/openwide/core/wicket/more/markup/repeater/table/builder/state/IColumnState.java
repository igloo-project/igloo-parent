package fr.openwide.core.wicket.more.markup.repeater.table.builder.state;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.ActionColumnBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.column.ICoreColumn;
import fr.openwide.core.wicket.more.rendering.Renderer;
import fr.openwide.core.wicket.more.util.IDatePattern;

public interface IColumnState<T, S extends ISort<?>> extends IBuildState<T, S> {
	
	IAddedColumnState<T, S> addColumn(IColumn<T, S> column);

	IAddedCoreColumnState<T, S> addColumn(ICoreColumn<T, S> column);

	IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel);

	IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Renderer<? super T> renderer);

	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, C> binding);
	
	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, C> binding, Renderer<? super C> renderer);

	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Function<? super T, C> function);
	
	<C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Function<? super T, C> function,
			Renderer<? super C> renderer);

	IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, ? extends Date> binding, IDatePattern datePattern);

	<C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, C> binding, BootstrapRenderer<? super C> renderer);
	
	<C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
			Function<? super T, C> function, BootstrapRenderer<? super C> renderer);

	<C> IAddedCoreColumnState<T, S> addBootstrapLabelColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, C> binding, BootstrapRenderer<? super C> renderer);

	<C> IAddedCoreColumnState<T, S> addBootstrapLabelColumn(IModel<String> headerModel,
			Function<? super T, C> function, BootstrapRenderer<? super C> renderer);

	<C> IAddedBooleanLabelColumnState<T, S> addBooleanLabelColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, Boolean> binding);
	
	ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn();
	
	ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn(IModel<String> headerLabelModel);

}
