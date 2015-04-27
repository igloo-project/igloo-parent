package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.IComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.DecoratedCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.DecoratedCoreDataTablePanel.AddInPlacement;

public interface IDecoratedBuildState<T, S extends ISort<?>> {
	
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
	
	IDecoratedBuildState<T, S> addIn(AddInPlacement placement, IComponentFactory<?> addInComponentFactory);

	IDecoratedBuildState<T, S> addIn(AddInPlacement placement, IComponentFactory<?> addInComponentFactory,
			String cssClasses);
	
	IDecoratedBuildState<T, S> addIn(AddInPlacement placement,
			IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>> addInComponentFactory);

	IDecoratedBuildState<T, S> addIn(AddInPlacement placement,
			IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>> addInComponentFactory,
			String cssClasses);
	
	DecoratedCoreDataTablePanel<T, S> build(String id);
	
	DecoratedCoreDataTablePanel<T, S> build(String id, long rowsPerPage);
	
}
