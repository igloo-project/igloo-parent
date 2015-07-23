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
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideableAjaxPagingNavigator;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.util.IDataTableFactory;
import fr.openwide.core.wicket.more.model.IErrorAwareDataProvider;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBindings;

public class DecoratedCoreDataTablePanel<T, S extends ISort<?>> extends Panel implements IPageableItems {
	
	private static final long serialVersionUID = 3327546179785797119L;
	
	private final CoreDataTable<T, S> dataTable;
	
	private final IDataProvider<T> dataProvider;
	
	public static enum AddInPlacement {
		HEADING_MAIN,
		HEADING_LEFT,
		HEADING_RIGHT,
		BODY_TOP,
		BODY_BOTTOM,
		FOOTER_LEFT,
		FOOTER_RIGHT
	}
	
	public DecoratedCoreDataTablePanel(
			String id,
			IDataTableFactory<T, S> factory,
			Map<IColumn<T, S>, Condition> columns,
			IDataProvider<T> dataProvider,
			long rowsPerPage,
			Multimap<AddInPlacement, ? extends IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>> addInComponentFactories) {
		super(id);
		
		this.dataProvider = dataProvider;
		
		dataTable = newDataTable("dataTable", factory, columns, dataProvider, rowsPerPage);
		add(dataTable);
		
		RepeatingView headingMainAddins = new RepeatingView("mainAddIn");
		RepeatingView headingRightAddins = new RepeatingView("rightAddIn");
		RepeatingView headingLeftAddins = new RepeatingView("leftAddIn");
		
		RepeatingView bodyTopAddins = new RepeatingView("bodyTopAddIn");
		RepeatingView bodyBottomAddins = new RepeatingView("bodyBottomAddIn");
		
		RepeatingView footerRightAddins = new RepeatingView("rightAddIn");
		RepeatingView footerLeftAddins = new RepeatingView("leftAddIn");
		
		add(
				new EnclosureContainer("headingAddInContainer")
						.condition(Condition.anyChildVisible(headingMainAddins).or(Condition.anyChildVisible(headingRightAddins).or(Condition.anyChildVisible(headingLeftAddins))))
						.add(headingMainAddins, headingRightAddins, headingLeftAddins),
				new EnclosureContainer("bodyTopAddInContainer")
						.condition(Condition.anyChildVisible(bodyTopAddins))
						.add(bodyTopAddins),
				new EnclosureContainer("bodyBottomAddInContainer")
						.condition(Condition.anyChildVisible(bodyBottomAddins))
						.add(bodyBottomAddins),
				new EnclosureContainer("footerAddInContainer")
						.condition(Condition.anyChildVisible(footerRightAddins).or(Condition.anyChildVisible(footerLeftAddins)))
						.add(footerRightAddins, footerLeftAddins)
		);

		ComponentFactories.addAll(headingMainAddins, addInComponentFactories.get(AddInPlacement.HEADING_MAIN), this);
		ComponentFactories.addAll(headingRightAddins, addInComponentFactories.get(AddInPlacement.HEADING_RIGHT), this);
		ComponentFactories.addAll(headingLeftAddins, addInComponentFactories.get(AddInPlacement.HEADING_LEFT), this);
		ComponentFactories.addAll(bodyTopAddins, addInComponentFactories.get(AddInPlacement.BODY_TOP), this);
		ComponentFactories.addAll(bodyBottomAddins, addInComponentFactories.get(AddInPlacement.BODY_BOTTOM), this);
		ComponentFactories.addAll(footerRightAddins, addInComponentFactories.get(AddInPlacement.FOOTER_RIGHT), this);
		ComponentFactories.addAll(footerLeftAddins, addInComponentFactories.get(AddInPlacement.FOOTER_LEFT), this);
	}
	
	protected CoreDataTable<T, S> newDataTable(String id, IDataTableFactory<T, S> factory,
			Map<IColumn<T, S>, Condition> columns, IDataProvider<T> dataProvider, long rowsPerPage) {
		return factory.create(id, columns, dataProvider, rowsPerPage);
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
		
		private final int viewSize;
		
		public AjaxPagerAddInComponentFactory(int viewSize) {
			super();
			this.viewSize = viewSize;
		}
		
		@Override
		public Component create(String wicketId, DecoratedCoreDataTablePanel<?, ?> dataTable) {
			dataTable.setOutputMarkupId(true);
			return new HideableAjaxPagingNavigator(wicketId, dataTable, viewSize)
					.add(new ClassAttributeAppender("add-in-pagination"));
		}
	}
	
	public static class PagerAddInComponentFactory extends AbstractParameterizedComponentFactory<Component, IPageable> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		private final int viewSize;
		
		public PagerAddInComponentFactory(int viewSize) {
			super();
			this.viewSize = viewSize;
		}
		
		@Override
		public Component create(String wicketId, IPageable pageable) {
			return new HideablePagingNavigator(wicketId, pageable, viewSize)
					.add(new ClassAttributeAppender("add-in-pagination"));
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		// notification en cas d'erreur
		if ((dataProvider instanceof IErrorAwareDataProvider) 
				&& (((IErrorAwareDataProvider) dataProvider).hasError())) {
			error(getString("common.error.unexpected"));
		}
	}
}
