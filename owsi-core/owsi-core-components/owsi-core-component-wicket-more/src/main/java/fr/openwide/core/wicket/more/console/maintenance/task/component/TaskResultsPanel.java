package fr.openwide.core.wicket.more.console.maintenance.task.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import fr.openwide.core.wicket.more.console.maintenance.task.page.ConsoleMaintenanceTaskDescriptionPage;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.list.AbstractGenericItemListPanel;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class TaskResultsPanel extends AbstractGenericItemListPanel<QueuedTaskHolder> {

	private static final long serialVersionUID = 5519060838663253181L;

	public TaskResultsPanel(String id, final QueuedTaskHolderDataProvider queuedTaskHolderDataProvider, int itemsPerPage) {
		super(id, queuedTaskHolderDataProvider, itemsPerPage);
	}

	@Override
	protected void addItemColumns(final Item<QueuedTaskHolder> item, IModel<? extends QueuedTaskHolder> itemModel) {
		item.setOutputMarkupId(true);
		
		Component queue = new CoreLabel("queue", BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().queueId())).hideIfEmpty();
		item.add(
				new TaskStatusPanel("status", BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().status())),
				new TaskResultPanel("result", BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().result())),
				ConsoleMaintenanceTaskDescriptionPage.linkDescriptor(ReadOnlyModel.of(itemModel))
						.link("nameLink")
						.setBody(BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().name())),
				queue,
				new PlaceholderContainer("defaultQueue").condition(Condition.componentVisible(queue)),
				new DateLabel("creationDate", BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().creationDate()),
						DatePattern.SHORT_DATETIME),
				new DateLabel("startDate", BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().startDate()),
						DatePattern.SHORT_DATETIME),
				new DateLabel("endDate", BindingModel.of(itemModel, CoreJpaMoreBindings.queuedTaskHolder().endDate()),
						DatePattern.SHORT_DATETIME));
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new HideablePagingNavigator("topPager", getDataView()));
		add(new HideablePagingNavigator("bottomPager", getDataView()));
	}

	@Override
	protected void doDeleteItem(IModel<? extends QueuedTaskHolder> itemModel) throws ServiceException,
			SecurityServiceException {

	}

	@Override
	protected boolean hasWritePermissionOn(IModel<? extends QueuedTaskHolder> itemModel) {
		return false;
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
}
