package org.iglooproject.showcase.core.business.task.model.search;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSort;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.showcase.core.business.task.model.ShowcaseTaskQueueId;
import org.iglooproject.showcase.core.business.task.model.TaskTypeEnum;

public class TaskSearchQueryParameters implements Serializable {

	private static final long serialVersionUID = 8066107805687486489L;

	private ShowcaseTaskQueueId queueId;

	private TaskTypeEnum type;

	private String name;

	private TaskStatus status;

	private TaskResult result;

	private Date dateMin;

	private Date dateMax;

	private Map<QueuedTaskHolderSort, SortOrder> sort;

	protected TaskSearchQueryParameters() {
	}

	public TaskSearchQueryParameters(ShowcaseTaskQueueId queueId, TaskTypeEnum type, String name,
			TaskStatus status, TaskResult result,
			Date dateMin, Date dateMax, Map<QueuedTaskHolderSort, SortOrder> sort) {
		this.queueId = queueId;
		this.type = type;
		this.name = name;
		this.status = status;
		this.result = result;
		this.dateMin = dateMin;
		this.dateMax = dateMax;
		this.sort = sort;
	}

	public ShowcaseTaskQueueId getQueueId() {
		return queueId;
	}

	public TaskTypeEnum getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public TaskResult getResult() {
		return result;
	}

	public Date getDateMin() {
		return CloneUtils.clone(dateMin);
	}

	public Date getDateMax() {
		return CloneUtils.clone(dateMax);
	}

	public Map<QueuedTaskHolderSort, SortOrder> getSort() {
		return sort;
	}

}
