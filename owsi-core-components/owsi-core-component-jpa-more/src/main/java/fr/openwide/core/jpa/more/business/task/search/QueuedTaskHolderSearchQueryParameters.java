package fr.openwide.core.jpa.more.business.task.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

public class QueuedTaskHolderSearchQueryParameters implements Serializable {

	private static final long serialVersionUID = 123152452275164563L;

	private String name;

	private List<TaskStatus> statuses;

	private List<String> taskTypes;

	private List<String> queueIds;

	private Date creationDate;

	private Date startDate;

	private Date endDate;

	public QueuedTaskHolderSearchQueryParameters() {
	}

	public QueuedTaskHolderSearchQueryParameters(String name, Collection<TaskStatus> statuses, Collection<String> taskTypes, Collection<String> queueIds,
			Date creationDate, Date startDate, Date endDate) {
		this.name = name;
		this.statuses = statuses == null ? null : ImmutableList.copyOf(statuses);
		this.taskTypes = taskTypes == null ? null : ImmutableList.copyOf(taskTypes);
		this.queueIds = queueIds == null ? null : Lists.newArrayList(queueIds); // Null elements are allowed => not ImmutableList
		this.creationDate = CloneUtils.clone(creationDate);
		this.startDate = CloneUtils.clone(startDate);
		this.endDate = CloneUtils.clone(endDate);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TaskStatus> getStatuses() {
		return statuses;
	}

	public List<String> getTaskTypes() {
		return taskTypes;
	}
	
	public List<String> getQueueIds() {
		return queueIds;
	}

	public Date getCreationDate() {
		return CloneUtils.clone(creationDate);
	}

	public Date getStartDate() {
		return CloneUtils.clone(startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

}
