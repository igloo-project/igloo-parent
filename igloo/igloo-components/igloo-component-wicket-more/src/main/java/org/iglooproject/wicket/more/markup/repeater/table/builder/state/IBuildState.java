package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;
import org.iglooproject.wicket.more.markup.repeater.table.builder.IDataTableFactory;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.CustomizableToolbarBuilder;

public interface IBuildState<T, S extends ISort<?>> {

	CustomizableToolbarBuilder<T, S> addTopToolbar();

	CustomizableToolbarBuilder<T, S> addBottomToolbar();

	IBuildState<T, S> addRowCssClass(IDetachableFactory<? super IModel<? extends T>, ? extends String> rowCssClassFactory);

	IBuildState<T, S> withNoRecordsResourceKey(String noRecordsResourceKey);

	IBuildState<T, S> hideHeadersToolbar();

	IBuildState<T, S> hideNoRecordsToolbar();

	IBuildState<T, S> withFactory(IDataTableFactory<T, S> factory);

	CoreDataTable<T, S> build(String id);

	CoreDataTable<T, S> build(String id, long rowsPerPage);

	IDecoratedBuildState<T, S> decorate();

	IDecoratedBuildState<T, S> bootstrapCard();

	/**
	 * @deprecated Use {@link #bootstrapCard()} instead with Bootstrap 4.
	 */
	@Deprecated
	IDecoratedBuildState<T, S> bootstrapPanel();

}
