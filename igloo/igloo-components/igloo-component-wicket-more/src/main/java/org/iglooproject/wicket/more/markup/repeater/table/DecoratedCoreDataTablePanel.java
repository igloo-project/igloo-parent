package org.iglooproject.wicket.more.markup.repeater.table;

import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPageableItems;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.navigation.paging.HideableAjaxPagingNavigator;
import org.iglooproject.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import org.iglooproject.wicket.more.markup.repeater.FactoryRepeatingView;
import org.iglooproject.wicket.more.markup.repeater.table.builder.IDataTableFactory;
import org.iglooproject.wicket.more.model.IErrorAwareDataProvider;

import com.google.common.collect.Multimap;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.CountLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import igloo.wicket.model.DataProviderBindings;
import igloo.wicket.model.ISequenceProvider;

public class DecoratedCoreDataTablePanel<T, S extends ISort<?>> extends Panel implements IPageableItems {
	
	private static final long serialVersionUID = 3327546179785797119L;
	
	private final CoreDataTable<T, S> dataTable;
	
	private final ISequenceProvider<T> sequenceProvider;

	private final String variation;
	
	public static enum AddInPlacement {
		HEADING_MAIN,
		HEADING_LEFT,
		HEADING_RIGHT,
		BODY_TOP,
		BODY_BOTTOM,
		FOOTER_MAIN,
		FOOTER_LEFT,
		FOOTER_RIGHT
	}
	
	public DecoratedCoreDataTablePanel(
		String id,
		IDataTableFactory<T, S> factory,
		Map<IColumn<T, S>, Condition> columns,
		ISequenceProvider<T> sequenceProvider,
		List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> rowsBehaviorFactories,
		List<Behavior> tableBehaviors,
		long rowsPerPage,
		Multimap<AddInPlacement, ? extends IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>> addInComponentFactories,
		Condition responsiveCondition
	) {
		super(id);
		this.variation = BootstrapRequestCycle.getVariation();
		
		this.sequenceProvider = sequenceProvider;
		
		dataTable = newDataTable("dataTable", factory, columns, sequenceProvider, rowsBehaviorFactories, tableBehaviors, rowsPerPage);
		
		add(
				new WebMarkupContainer("dataTableContainer")
						.add(dataTable)
						.setRenderBodyOnly(responsiveCondition.negate().applies())
		);
		
		dataTable.setComponentToRefresh(this);
		
		EnclosureContainer headingMainAddinWrapper = new EnclosureContainer("mainAddInWrapper");
		EnclosureContainer headingRightAddinWrapper = new EnclosureContainer("rightAddInWrapper");
		EnclosureContainer headingLeftAddinWrapper = new EnclosureContainer("leftAddInWrapper");
		
		FactoryRepeatingView headingMainAddins = new FactoryRepeatingView("mainAddIn");
		FactoryRepeatingView headingRightAddins = new FactoryRepeatingView("rightAddIn");
		FactoryRepeatingView headingLeftAddins = new FactoryRepeatingView("leftAddIn");
		
		FactoryRepeatingView bodyTopAddins = new FactoryRepeatingView("bodyTopAddIn");
		FactoryRepeatingView bodyBottomAddins = new FactoryRepeatingView("bodyBottomAddIn");
		
		EnclosureContainer footerMainAddinWrapper = new EnclosureContainer("mainAddInWrapper");
		EnclosureContainer footerRightAddinWrapper = new EnclosureContainer("rightAddInWrapper");
		EnclosureContainer footerLeftAddinWrapper = new EnclosureContainer("leftAddInWrapper");
		
		FactoryRepeatingView footerMainAddins = new FactoryRepeatingView("mainAddIn");
		FactoryRepeatingView footerRightAddins = new FactoryRepeatingView("rightAddIn");
		FactoryRepeatingView footerLeftAddins = new FactoryRepeatingView("leftAddIn");
		
		add(
				new EnclosureContainer("headingAddInContainer")
						.condition(Condition.visible(headingMainAddinWrapper).or(Condition.visible(headingRightAddinWrapper).or(Condition.visible(headingLeftAddinWrapper))))
						.add(
								headingMainAddinWrapper
										.condition(Condition.anyChildVisible(headingMainAddins))
										.add(headingMainAddins),
								headingRightAddinWrapper
										.condition(Condition.anyChildVisible(headingRightAddins))
										.add(headingRightAddins),
								headingLeftAddinWrapper
										.condition(Condition.anyChildVisible(headingLeftAddins))
										.add(headingLeftAddins)
						),
				new EnclosureContainer("bodyTopAddInContainer")
						.condition(Condition.anyChildVisible(bodyTopAddins))
						.add(bodyTopAddins),
				new EnclosureContainer("bodyBottomAddInContainer")
						.condition(Condition.anyChildVisible(bodyBottomAddins))
						.add(bodyBottomAddins),
				new EnclosureContainer("footerAddInContainer")
						.condition(Condition.visible(footerMainAddinWrapper).or(Condition.visible(footerRightAddinWrapper).or(Condition.visible(footerLeftAddinWrapper))))
						.add(
								footerMainAddinWrapper
										.condition(Condition.anyChildVisible(footerMainAddins))
										.add(footerMainAddins),
								footerRightAddinWrapper
										.condition(Condition.anyChildVisible(footerRightAddins))
										.add(footerRightAddins),
								footerLeftAddinWrapper
										.condition(Condition.anyChildVisible(footerLeftAddins))
										.add(footerLeftAddins)
						)
		);

		headingMainAddins.addAll(addInComponentFactories.get(AddInPlacement.HEADING_MAIN), this);
		headingRightAddins.addAll(addInComponentFactories.get(AddInPlacement.HEADING_RIGHT), this);
		headingLeftAddins.addAll(addInComponentFactories.get(AddInPlacement.HEADING_LEFT), this);
		bodyTopAddins.addAll(addInComponentFactories.get(AddInPlacement.BODY_TOP), this);
		bodyBottomAddins.addAll(addInComponentFactories.get(AddInPlacement.BODY_BOTTOM), this);
		footerMainAddins.addAll(addInComponentFactories.get(AddInPlacement.FOOTER_MAIN), this);
		footerRightAddins.addAll(addInComponentFactories.get(AddInPlacement.FOOTER_RIGHT), this);
		footerLeftAddins.addAll(addInComponentFactories.get(AddInPlacement.FOOTER_LEFT), this);
	}
	
	protected CoreDataTable<T, S> newDataTable(String id, IDataTableFactory<T, S> factory,
			Map<IColumn<T, S>, Condition> columns, ISequenceProvider<T> sequenceProvider,
			List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> rowsBehaviorFactories,
			List<Behavior> tableBehaviors,
			long rowsPerPage) {
		return factory.create(id, columns, sequenceProvider, rowsBehaviorFactories, tableBehaviors, rowsPerPage);
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
	
	public static class LabelAddInComponentFactory implements IComponentFactory<Component> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		private final IModel<?> labelModel;
		
		public LabelAddInComponentFactory(IModel<?> labelModel) {
			super();
			this.labelModel = labelModel;
		}
		
		@Override
		public Component create(String wicketId) {
			return new CoreLabel(wicketId, labelModel);
		}
	}
	
	public static class CountAddInComponentFactory implements IComponentFactory<Component> {
		private static final long serialVersionUID = 7358590231263113101L;
		
		private final ISequenceProvider<?> sequenceProvider;
		private final String countResourceKey;
		
		public CountAddInComponentFactory(ISequenceProvider<?> sequenceProvider, String countResourceKey) {
			super();
			this.sequenceProvider = sequenceProvider;
			this.countResourceKey = countResourceKey;
		}
		
		@Override
		public Component create(String wicketId) {
			IModel<Integer> countModel = new PropertyModel<>(sequenceProvider, DataProviderBindings.iBindableDataProvider().size().getPath());
			return new CountLabel(wicketId, countResourceKey, countModel);
		}
	}
	
	public static class AjaxPagerAddInComponentFactory implements IOneParameterComponentFactory<Component, DecoratedCoreDataTablePanel<?, ?>> {
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
	
	public static class PagerAddInComponentFactory implements IOneParameterComponentFactory<Component, IPageable> {
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
		if ((sequenceProvider instanceof IErrorAwareDataProvider) 
				&& (((IErrorAwareDataProvider) sequenceProvider).hasError())) {
			error(getString("common.error.unexpected"));
		}
	}

	@Override
	public String getVariation() {
		return variation;
	}

}
