package fr.openwide.core.wicket.more.console.maintenance.task.component;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.wicket.more.console.common.util.LinkUtils;
import fr.openwide.core.wicket.more.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.list.AbstractGenericItemListPanel;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.util.DatePattern;

public class TaskResultsPanel extends AbstractGenericItemListPanel<QueuedTaskHolder> {

	private static final long serialVersionUID = 5519060838663253181L;

	public TaskResultsPanel(String id, final QueuedTaskHolderDataProvider queuedTaskHolderDataProvider, int itemsPerPage) {
		super(id, queuedTaskHolderDataProvider, itemsPerPage);
	}

	@Override
	protected void addItemColumns(final Item<QueuedTaskHolder> item, IModel<? extends QueuedTaskHolder> itemModel) {
		item.setOutputMarkupId(true);

		item.add(new TaskStatusPanel("status", Model.of(item.getModelObject().getStatus())));

		Link<QueuedTaskHolder> nameLink = LinkUtils.getQueuedTaskHolderLink("nameLink", itemModel);
		nameLink.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
		item.add(nameLink);

		item.add(new DateLabel("creationDate", new PropertyModel<Date>(item.getModel(), "creationDate"),
				DatePattern.SHORT_DATETIME));
		item.add(new DateLabel("startDate", new PropertyModel<Date>(item.getModel(), "startDate"),
				DatePattern.SHORT_DATETIME));
		item.add(new DateLabel("endDate", new PropertyModel<Date>(item.getModel(), "endDate"),
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
	protected boolean hasWritePermissionOn(IModel<?> itemModel) {
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
