package org.iglooproject.showcase.web.application.task.model;

import java.util.Date;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSort;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.showcase.core.business.task.model.ShowcaseTaskQueueId;
import org.iglooproject.showcase.core.business.task.model.TaskTypeEnum;
import org.iglooproject.showcase.core.business.task.model.search.TaskSearchQueryParameters;
import org.iglooproject.showcase.core.business.task.service.IShowcaseTaskService;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class TaskDataProvider extends LoadableDetachableDataProvider<QueuedTaskHolder> {
	
	private static final long serialVersionUID = -5506738601502647625L;
	
	@SpringBean
	private IShowcaseTaskService showcaseTaskService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskDataProvider.class);
	
	private final IModel<ShowcaseTaskQueueId> queueIdModel = new Model<ShowcaseTaskQueueId>();
	
	private final IModel<TaskTypeEnum> typeModel = new Model<TaskTypeEnum>();
	
	private final IModel<String> nameModel = new Model<String>();
	
	private final IModel<TaskStatus> statusModel = new Model<TaskStatus>();
	
	private final IModel<TaskResult> resultModel = new Model<TaskResult>();
	
	private final IModel<Date> dateMinModel = new Model<Date>();
	
	private final IModel<Date> dateMaxModel = new Model<Date>();
	
	private final CompositeSortModel<QueuedTaskHolderSort> sortModel = new CompositeSortModel<>(
			CompositingStrategy.LAST_ONLY,
			ImmutableMap.of(QueuedTaskHolderSort.CREATION_DATE, QueuedTaskHolderSort.CREATION_DATE.getDefaultOrder()),
			ImmutableMap.of(
					QueuedTaskHolderSort.CREATION_DATE, QueuedTaskHolderSort.CREATION_DATE.getDefaultOrder(),
					QueuedTaskHolderSort.NAME, QueuedTaskHolderSort.NAME.getDefaultOrder(),
					QueuedTaskHolderSort.ID, QueuedTaskHolderSort.ID.getDefaultOrder()
			)
	);
	
	public TaskDataProvider() {
		super();
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		super.detach();
		typeModel.detach();
		nameModel.detach();
		statusModel.detach();
		resultModel.detach();
		dateMinModel.detach();
		dateMaxModel.detach();
	}

	@Override
	public IModel<QueuedTaskHolder> model(QueuedTaskHolder object) {
		return new GenericEntityModel<Long, QueuedTaskHolder>(object);
	}

	public TaskSearchQueryParameters getSearchParameters() {
		return new TaskSearchQueryParameters(
				queueIdModel.getObject(), typeModel.getObject(), nameModel.getObject(),
				statusModel.getObject(), resultModel.getObject(),
				dateMinModel.getObject(), dateMaxModel.getObject(),
				sortModel.getObject());
	}

	@Override
	protected List<QueuedTaskHolder> loadList(long first, long count) {
		try {
			return showcaseTaskService.search(getSearchParameters(), count, first);
		} catch (Exception e) {
			LOGGER.error("Error while searching tasks.", e);
			return Lists.newArrayList();
		}
	}

	@Override
	protected long loadSize() {
		try {
			return showcaseTaskService.count(getSearchParameters());
		} catch (Exception e) {
			LOGGER.error("Error while counting tasks.", e);
			return 0;
		}
	}

	public IModel<ShowcaseTaskQueueId> getQueueIdModel() {
		return queueIdModel;
	}

	public IModel<TaskTypeEnum> getTypeModel() {
		return typeModel;
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}

	public IModel<TaskStatus> getStatusModel() {
		return statusModel;
	}

	public IModel<TaskResult> getResultModel() {
		return resultModel;
	}

	public IModel<Date> getDateMinModel() {
		return dateMinModel;
	}

	public IModel<Date> getDateMaxModel() {
		return dateMaxModel;
	}

	public CompositeSortModel<QueuedTaskHolderSort> getSortModel() {
		return sortModel;
	}

}
