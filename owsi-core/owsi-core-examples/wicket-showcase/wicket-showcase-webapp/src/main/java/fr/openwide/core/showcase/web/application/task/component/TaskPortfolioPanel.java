package fr.openwide.core.showcase.web.application.task.component;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;
import fr.openwide.core.showcase.core.business.task.model.ShowcaseBatchReportBean;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.basic.CountLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.console.maintenance.task.component.TaskResultPanel;
import fr.openwide.core.wicket.more.console.maintenance.task.component.TaskStatusPanel;
import fr.openwide.core.wicket.more.console.maintenance.task.model.TaskBatchReportBeanModel;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.basic.DefaultPlaceholderPanel;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.markup.html.list.AbstractGenericItemListPanel;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverOptions;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBindings;

public class TaskPortfolioPanel extends AbstractGenericItemListPanel<QueuedTaskHolder> {

	private static final long serialVersionUID = 3388933897843193596L;
	
	private final IModel<Integer> countModel;
	
	public TaskPortfolioPanel(String id, IDataProvider<QueuedTaskHolder> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
		setOutputMarkupId(true);
		countModel = new PropertyModel<Integer>(dataProvider,
				CoreWicketMoreBindings.iBindableDataProvider().size().getPath());
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		countModel.detach();
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		// Count
		add(new CountLabel("topCount", "tasks.count", countModel));
		
		// Pagers
		add(new HideablePagingNavigator("topPager", getDataView()));
		add(new HideablePagingNavigator("bottomPager", getDataView()));
	}

	@Override
	protected void addItemColumns(Item<QueuedTaskHolder> item, IModel<? extends QueuedTaskHolder> itemModel) {
		IModel<QueuedTaskHolder> queuedTaskHolderModel = item.getModel();
		TaskResultPanel result = new TaskResultPanel("result", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().result())).hideIfEmpty();
		
		NotTreatedObjectsPanel notTreatedObjectsPanel = new NotTreatedObjectsPanel("notTreatedObjectsPanel",
				new TaskBatchReportBeanModel<>(ShowcaseBatchReportBean.class, queuedTaskHolderModel));
		EnclosureContainer notTreatedObjects = new EnclosureContainer("notTreatedObjects")
				.condition(Condition.componentVisible(notTreatedObjectsPanel));
		BootstrapPopoverOptions popoverOptions = new BootstrapPopoverOptions();
		popoverOptions.setTitleModel(new ResourceModel("tasks.list.notTreatedObjects"));
		popoverOptions.setContentComponent(notTreatedObjectsPanel);
		popoverOptions.setHtml(true);
		notTreatedObjects.add(new BootstrapPopoverBehavior(popoverOptions));
		item.add(notTreatedObjectsPanel, notTreatedObjects);
		
		item.add(
				new CoreLabel("queueId", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().queueId())).showPlaceholder(new ResourceModel("tasks.queue.default")),
				new CoreLabel("type", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().taskType())).showPlaceholder(),
				new CoreLabel("name", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().name())).showPlaceholder(),
				new TaskStatusPanel("status", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().status())).hideIfEmpty(), //.add(new EnclosureBehavior().model(statusModel)),
				result,
				new DefaultPlaceholderPanel("resultPlaceholder").condition(Condition.componentVisible(result)),
				new DateLabel("creationDate", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().creationDate()), DatePattern.SHORT_DATETIME).showPlaceholder(),
				new DateLabel("startDate", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().startDate()), DatePattern.SHORT_DATETIME).showPlaceholder(),
				new DateLabel("endDate", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().endDate()), DatePattern.SHORT_DATETIME).showPlaceholder()
		);
	}

	@Override
	protected void doDeleteItem(IModel<? extends QueuedTaskHolder> itemModel) throws ServiceException, SecurityServiceException {
		
	}

	@Override
	protected boolean isActionAvailable() {
		return false;
	}

	@Override
	protected boolean isDeleteAvailable() {
		return false;
	}

	@Override
	protected boolean isEditAvailable() {
		return false;
	}

	@Override
	protected boolean hasWritePermissionOn(IModel<? extends QueuedTaskHolder> itemModel) {
		return true;
	}
}
