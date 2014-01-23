package fr.openwide.core.wicket.more.console.maintenance.task.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.search.QueuedTaskHolderSearchQueryParameters;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class QueuedTaskHolderDataProvider extends LoadableDetachableDataProvider<QueuedTaskHolder> {

	private static final long serialVersionUID = -1886156254057416250L;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedTaskHolderDataProvider.class);

	private final IModel<String> nameModel = new Model<String>();

	private final IModel<Collection<TaskStatus>> statusesModel = new CollectionModel<TaskStatus>();

	private final IModel<Collection<String>> taskTypesModel = new CollectionModel<String>();

	private final IModel<Collection<String>> queueIdsModel = new CollectionModel<String>();

	private final IModel<Date> creationDateModel = new Model<Date>();

	private final IModel<Date> startDateModel = new Model<Date>();

	private final IModel<Date> completionDateModel = new Model<Date>();

	@SpringBean
	private IQueuedTaskHolderService queuedTaskHolderService;

	public QueuedTaskHolderDataProvider() {
		super();
		Injector.get().inject(this);
	}

	public QueuedTaskHolderSearchQueryParameters getSearchParameters() {
		return new QueuedTaskHolderSearchQueryParameters(nameModel.getObject(), statusesModel.getObject(),
				taskTypesModel.getObject(), queueIdsModel.getObject(),
				creationDateModel.getObject(), startDateModel.getObject(), completionDateModel.getObject());
	}

	@Override
	protected List<QueuedTaskHolder> loadList(long first, long count) {
		try {
			return queuedTaskHolderService.search(getSearchParameters(), count, first);
		} catch (Exception e) {
			LOGGER.error("Error while searching tasks.", e);
			return Lists.newArrayList();
		}
	}

	@Override
	protected long loadSize() {
		try {
			return queuedTaskHolderService.count(getSearchParameters());
		} catch (Exception e) {
			LOGGER.error("Error while counting tasks.", e);
			return 0;
		}
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}

	public IModel<Collection<TaskStatus>> getStatusesModel() {
		return statusesModel;
	}

	public IModel<Collection<String>> getTaskTypesModel() {
		return taskTypesModel;
	}
	
	public IModel<Collection<String>> getQueueIdsModel() {
		return queueIdsModel;
	}

	public IModel<Date> getCreationDateModel() {
		return creationDateModel;
	}

	public IModel<Date> getStartDateModel() {
		return startDateModel;
	}

	public IModel<Date> getCompletionDateModel() {
		return completionDateModel;
	}

	@Override
	public IModel<QueuedTaskHolder> model(QueuedTaskHolder object) {
		return new GenericEntityModel<Long, QueuedTaskHolder>(object);
	}

	@Override
	public void detach() {
		super.detach();
		
		nameModel.detach();
		statusesModel.detach();
		taskTypesModel.detach();
		creationDateModel.detach();
		startDateModel.detach();
		completionDateModel.detach();
	}
}
