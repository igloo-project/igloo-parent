package org.iglooproject.jpa.more.business.task.search;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.compress.utils.Lists;
import org.bindgen.Bindable;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class QueuedTaskHolderSearchQueryData implements ISearchQueryData<QueuedTaskHolder> {

	private String name;

	private Collection<TaskStatus> statuses = Lists.newArrayList();

	private Collection<TaskResult> results = Lists.newArrayList();

	private Collection<String> taskTypes = Lists.newArrayList();

	private Collection<String> queueIds = Lists.newArrayList();

	private Instant creationDate;

	private Instant startDate;

	private Instant endDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<TaskStatus> getStatuses() {
		return Collections.unmodifiableCollection(statuses);
	}

	public void setStatuses(Collection<TaskStatus> statuses) {
		CollectionUtils.replaceAll(this.statuses, statuses);
	}

	public Collection<TaskResult> getResults() {
		return Collections.unmodifiableCollection(results);
	}

	public void setResults(Collection<TaskResult> results) {
		CollectionUtils.replaceAll(this.results, results);
	}

	public Collection<String> getTaskTypes() {
		return Collections.unmodifiableCollection(taskTypes);
	}

	public void setTaskTypes(Collection<String> taskTypes) {
		CollectionUtils.replaceAll(this.taskTypes, taskTypes);
	}

	public Collection<String> getQueueIds() {
		return Collections.unmodifiableCollection(queueIds);
	}

	public void setQueueIds(Collection<String> queueIds) {
		CollectionUtils.replaceAll(this.queueIds, queueIds);
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

}
