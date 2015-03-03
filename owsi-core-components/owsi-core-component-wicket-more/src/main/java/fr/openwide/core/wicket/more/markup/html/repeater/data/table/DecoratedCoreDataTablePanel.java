package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPageableItems;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.google.common.collect.Multimap;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CountLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.ComponentFactories;
import fr.openwide.core.wicket.more.markup.html.factory.IParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideableAjaxPagingNavigator;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBindings;

public class DecoratedCoreDataTablePanel<T, S extends ISort<?>> extends Panel implements IPageableItems {
	
	private static final long serialVersionUID = 3327546179785797119L;
	
	private final CoreDataTable<T, S> dataTable;
	
	public static enum AddInPlacement {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT;
	}
	
	public DecoratedCoreDataTablePanel(String id, Map<IColumn<T, S>, Condition> columns, IDataProvider<T> dataProvider,
			long rowsPerPage,
			Multimap<AddInPlacement, ? extends IParameterizedComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>> addInComponentFactories) {
		super(id);
		
		dataTable = newDataTable("dataTable", columns, dataProvider, rowsPerPage);
		add(dataTable);
		
		RepeatingView topRightAddins = new RepeatingView("rightAddIn");
		RepeatingView topLeftAddins = new RepeatingView("leftAddIn");
		RepeatingView bottomRightAddins = new RepeatingView("rightAddIn");
		RepeatingView bottomLeftAddins = new RepeatingView("leftAddIn");
		
		add(
				new EnclosureContainer("topAddInContainer").condition(Condition.anyChildVisible(topRightAddins).or(Condition.anyChildVisible(topLeftAddins)))
						.add(topRightAddins, topLeftAddins),
				new EnclosureContainer("bottomAddInContainer").condition(Condition.anyChildVisible(bottomRightAddins).or(Condition.anyChildVisible(bottomLeftAddins)))
						.add(bottomRightAddins, bottomLeftAddins)
		);

		ComponentFactories.addAll(topRightAddins, addInComponentFactories.get(AddInPlacement.TOP_RIGHT), this);
		ComponentFactories.addAll(topLeftAddins, addInComponentFactories.get(AddInPlacement.TOP_LEFT), this);
		ComponentFactories.addAll(bottomRightAddins, addInComponentFactories.get(AddInPlacement.BOTTOM_RIGHT), this);
		ComponentFactories.addAll(bottomLeftAddins, addInComponentFactories.get(AddInPlacement.BOTTOM_LEFT), this);
	}
	
	protected CoreDataTable<T, S> newDataTable(String id, Map<IColumn<T, S>, Condition> columns, IDataProvider<T> dataProvider, long rowsPerPage) {
		return new CoreDataTable<T, S>(id, columns, dataProvider, rowsPerPage);
	}
	
	public CoreDataTable<T, S> getDataTable() {
		return dataTable;
	}

	@Override
	public long getCurrentPage() {
		return dataTable.getCurrentPage();
	}

	@Override
	public long getPageCount() {
		return dataTable.getPageCount();
	}

	@Override
	public void setCurrentPage(long page) {
		dataTable.setCurrentPage(page);
	}

	@Override
	public long getItemCount() {
		return dataTable.getItemCount();
	}

	@Override
	public long getItemsPerPage() {
		return dataTable.getItemsPerPage();
	}

	@Override
	public void setItemsPerPage(long arg0) {
		dataTable.setItemsPerPage(arg0);
	}
	
	public static class LabelAddInComponentFactory extends AbstractComponentFactory<Component> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		private final IModel<?> labelModel;
		
		public LabelAddInComponentFactory(IModel<?> labelModel) {
			super();
			this.labelModel = labelModel;
		}
		
		@Override
		public Component create(String wicketId) {
			return new Label(wicketId, labelModel);
		}
	}
	
	public static class CountAddInComponentFactory extends AbstractComponentFactory<Component> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		private final IDataProvider<?> dataProvider;
		private final String countResourceKey;
		
		public CountAddInComponentFactory(IDataProvider<?> dataProvider, String countResourceKey) {
			super();
			this.dataProvider = dataProvider;
			this.countResourceKey = countResourceKey;
		}
		
		@Override
		public Component create(String wicketId) {
			IModel<Integer> countModel = new PropertyModel<Integer>(dataProvider,
					CoreWicketMoreBindings.iBindableDataProvider().size().getPath());
			return new CountLabel(wicketId, countResourceKey, countModel);
		}
	}
	
	public static class AjaxPagerAddInComponentFactory extends AbstractParameterizedComponentFactory<Component, DecoratedCoreDataTablePanel<?, ?>> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		public AjaxPagerAddInComponentFactory() {
			super();
		}
		
		@Override
		public Component create(String wicketId, DecoratedCoreDataTablePanel<?, ?> dataTable) {
			dataTable.setOutputMarkupId(true);
			return new HideableAjaxPagingNavigator(wicketId, dataTable)
					.add(new ClassAttributeAppender("add-in-pagination"));
		}
	}
	
	public static class PagerAddInComponentFactory extends AbstractParameterizedComponentFactory<Component, IPageable> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		public PagerAddInComponentFactory() {
			super();
		}
		
		@Override
		public Component create(String wicketId, IPageable pageable) {
			return new HideablePagingNavigator(wicketId, pageable)
					.add(new ClassAttributeAppender("add-in-pagination"));
		}
	}
}
