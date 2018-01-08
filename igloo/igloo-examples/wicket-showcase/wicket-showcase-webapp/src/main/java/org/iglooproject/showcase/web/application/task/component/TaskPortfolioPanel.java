package org.iglooproject.showcase.web.application.task.component;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.showcase.core.business.task.model.ShowcaseBatchReportBean;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.console.maintenance.task.component.TaskResultPanel;
import org.iglooproject.wicket.more.console.maintenance.task.model.TaskBatchReportBeanModel;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverOptions;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.model.Detachables;

public class TaskPortfolioPanel extends Panel {

	private static final long serialVersionUID = 3388933897843193596L;

	private final IDataProvider<QueuedTaskHolder> dataProvider;

	private final DecoratedCoreDataTablePanel<QueuedTaskHolder, ISort<?>> dataTable;

	public TaskPortfolioPanel(String id, IDataProvider<QueuedTaskHolder> dataProvider, int itemsPerPage) {
		super(id);
		setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
		
		dataTable =
				DataTableBuilder.start(dataProvider)
						.addLabelColumn(new ResourceModel("tasks.queue"), CoreJpaMoreBindings.queuedTaskHolder().queueId())
						.showPlaceholder(new ResourceModel("tasks.queue.default"))
						.withClass("table-col-md")
				.addLabelColumn(new ResourceModel("tasks.type"), CoreJpaMoreBindings.queuedTaskHolder().taskType())
						.showPlaceholder()
						.withClass("table-col-md")
				.addColumn(
						new AbstractCoreColumn<QueuedTaskHolder, ISort<?>>(new ResourceModel("tasks.name")) {
							private static final long serialVersionUID = 1L;
							@Override
							public void populateItem(Item<ICellPopulator<QueuedTaskHolder>> cellItem, String componentId, IModel<QueuedTaskHolder> rowModel) {
								cellItem.add(new NameFragment(componentId, rowModel));
							}
						}
				)
						.withClass("table-col-lg")
				.addLabelColumn(new ResourceModel("tasks.result"), CoreJpaMoreBindings.queuedTaskHolder().status())
						.withClass("picto table-col-sm")
						.addColumn(
								new AbstractCoreColumn<QueuedTaskHolder, ISort<?>>(new ResourceModel("tasks.name")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<QueuedTaskHolder>> cellItem, String componentId, IModel<QueuedTaskHolder> rowModel) {
										cellItem.add(
												new TaskResultPanel(componentId, BindingModel.of(rowModel, CoreJpaMoreBindings.queuedTaskHolder().result()))
														.hideIfEmpty()
										);
									}
								}
						)
								.withClass("picto table-col-sm")
				
				.addLabelColumn(new ResourceModel("console.maintenance.task.common.creationDate"), CoreJpaMoreBindings.queuedTaskHolder().creationDate(), DatePattern.SHORT_DATETIME)
						.withClass("datetime")
				.addLabelColumn(new ResourceModel("console.maintenance.task.common.startDate"), CoreJpaMoreBindings.queuedTaskHolder().startDate(), DatePattern.SHORT_DATETIME)
						.withClass("datetime")
				.addLabelColumn(new ResourceModel("console.maintenance.task.common.endDate"), CoreJpaMoreBindings.queuedTaskHolder().endDate(), DatePattern.SHORT_DATETIME)
						.withClass("datetime")
				.withNoRecordsResourceKey("common.emptyList")
				.decorate()
						.count("tasks.count")
						.pagers()
				.build("results", itemsPerPage);
		
		add(
				dataTable
		);
	}

	private class NameFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public NameFragment(String id, IModel<QueuedTaskHolder> model) {
			super(id, "nameFragment", TaskPortfolioPanel.this, model);
			
			NotTreatedObjectsPanel notTreatedObjectsPanel = new NotTreatedObjectsPanel("notTreatedObjectsPanel",
					new TaskBatchReportBeanModel<>(ShowcaseBatchReportBean.class, model));
			EnclosureContainer notTreatedObjects = new EnclosureContainer("notTreatedObjects")
					.condition(Condition.componentVisible(notTreatedObjectsPanel));
			BootstrapPopoverOptions popoverOptions = new BootstrapPopoverOptions();
			popoverOptions.setTitleModel(new ResourceModel("tasks.list.notTreatedObjects"));
			popoverOptions.setContentComponent(notTreatedObjectsPanel);
			popoverOptions.setHtml(true);
			notTreatedObjects.add(new BootstrapPopoverBehavior(popoverOptions));
			
			add(
					new CoreLabel("name", BindingModel.of(model, CoreJpaMoreBindings.queuedTaskHolder().name())),
					notTreatedObjectsPanel, notTreatedObjects
			);
		}
		
	}

	public DecoratedCoreDataTablePanel<QueuedTaskHolder, ISort<?>> getDataTable() {
		return dataTable;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(dataProvider);
	}
}
