package org.iglooproject.wicket.more.console.maintenance.task.model;

import java.time.Instant;
import java.util.Collection;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.search.IQueuedTaskHolderSearchQuery;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSort;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

import com.google.common.collect.ImmutableMap;

import igloo.wicket.model.Detachables;

public class QueuedTaskHolderDataProvider extends AbstractSearchQueryDataProvider<QueuedTaskHolder, QueuedTaskHolderSort> {

	private static final long serialVersionUID = -1886156254057416250L;

	private final IModel<String> nameModel = new Model<>();

	private final IModel<Collection<TaskStatus>> statusesModel = new CollectionModel<>();

	private final IModel<Collection<TaskResult>> resultsModel = new CollectionModel<>();

	private final IModel<Collection<String>> taskTypesModel = new CollectionModel<>();

	private final IModel<Collection<String>> queueIdsModel = new CollectionModel<>();

	private final IModel<Instant> creationDateModel = new Model<>();

	private final IModel<Instant> startDateModel = new Model<>();

	private final IModel<Instant> endDateModel = new Model<>();

	private final CompositeSortModel<QueuedTaskHolderSort> sortModel = new CompositeSortModel<>(
			CompositingStrategy.LAST_ONLY,
			ImmutableMap.of(
				QueuedTaskHolderSort.CREATION_DATE, QueuedTaskHolderSort.CREATION_DATE.getDefaultOrder()
			),
			ImmutableMap.of(
				QueuedTaskHolderSort.CREATION_DATE, QueuedTaskHolderSort.CREATION_DATE.getDefaultOrder(),
				QueuedTaskHolderSort.ID, QueuedTaskHolderSort.ID.getDefaultOrder()
			)
	);

	public QueuedTaskHolderDataProvider() {
		super();
		Injector.get().inject(this);
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}

	public IModel<Collection<TaskStatus>> getStatusesModel() {
		return statusesModel;
	}

	public IModel<Collection<TaskResult>> getResultsModel() {
		return resultsModel;
	}

	public IModel<Collection<String>> getTaskTypesModel() {
		return taskTypesModel;
	}
	
	public IModel<Collection<String>> getQueueIdsModel() {
		return queueIdsModel;
	}

	public IModel<Instant> getCreationDateModel() {
		return creationDateModel;
	}

	public IModel<Instant> getStartDateModel() {
		return startDateModel;
	}

	public IModel<Instant> getCompletionDateModel() {
		return endDateModel;
	}

	@Override
	public IModel<QueuedTaskHolder> model(QueuedTaskHolder object) {
		return new GenericEntityModel<>(object);
	}

	@Override
	protected ISearchQuery<QueuedTaskHolder, QueuedTaskHolderSort> getSearchQuery() {
		return createSearchQuery(IQueuedTaskHolderSearchQuery.class)
			.name(nameModel.getObject())
			.statuses(statusesModel.getObject())
			.results(resultsModel.getObject())
			.types(taskTypesModel.getObject())
			.queueIds(queueIdsModel.getObject())
			.creationDate(creationDateModel.getObject())
			.startDate(startDateModel.getObject())
			.endDate(endDateModel.getObject())
			.sort(sortModel.getObject());
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			nameModel,
			statusesModel,
			resultsModel,
			taskTypesModel,
			queueIdsModel,
			creationDateModel,
			startDateModel,
			endDateModel
		);
	}

}
