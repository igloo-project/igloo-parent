package fr.openwide.core.wicket.more.console.maintenance.task.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import fr.openwide.core.wicket.more.console.maintenance.task.page.ConsoleMaintenanceTaskDescriptionPage;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.list.AbstractGenericItemListPanel;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;
import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBinding;

public class TaskResultsPanel extends AbstractGenericItemListPanel<QueuedTaskHolder> {

	private static final long serialVersionUID = 5519060838663253181L;

	public TaskResultsPanel(String id, final QueuedTaskHolderDataProvider queuedTaskHolderDataProvider, int itemsPerPage) {
		super(id, queuedTaskHolderDataProvider, itemsPerPage);
	}

	@Override
	protected void addItemColumns(final Item<QueuedTaskHolder> item, IModel<? extends QueuedTaskHolder> itemModel) {
		item.setOutputMarkupId(true);
		
		item.add(new TaskStatusPanel("status", BindingModel.of(itemModel, CoreWicketMoreBinding.queuedTaskHolderBinding().status())));

		item.add(ConsoleMaintenanceTaskDescriptionPage.linkDescriptor(ReadOnlyModel.of(itemModel))
				.link("nameLink")
				.setBody(BindingModel.of(itemModel, CoreWicketMoreBinding.queuedTaskHolderBinding().name())));
		
		Component queue = new HideableLabel("queue", BindingModel.of(itemModel, CoreWicketMoreBinding.queuedTaskHolderBinding().queueId()));
		item.add(queue, new PlaceholderContainer("defaultQueue").component(queue));

		item.add(new DateLabel("creationDate", BindingModel.of(itemModel, CoreWicketMoreBinding.queuedTaskHolderBinding().creationDate()),
				DatePattern.SHORT_DATETIME));
		item.add(new DateLabel("startDate", BindingModel.of(itemModel, CoreWicketMoreBinding.queuedTaskHolderBinding().startDate()),
				DatePattern.SHORT_DATETIME));
		item.add(new DateLabel("endDate", BindingModel.of(itemModel, CoreWicketMoreBinding.queuedTaskHolderBinding().endDate()),
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
