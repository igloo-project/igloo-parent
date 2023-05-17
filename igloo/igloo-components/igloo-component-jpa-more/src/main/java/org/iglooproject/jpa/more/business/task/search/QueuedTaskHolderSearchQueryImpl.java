package org.iglooproject.jpa.more.business.task.search;

import java.util.Collection;
import java.util.Date;

import org.iglooproject.jpa.more.business.search.query.OldAbstractHibernateSearchSearchQuery;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;

public class QueuedTaskHolderSearchQueryImpl extends OldAbstractHibernateSearchSearchQuery<QueuedTaskHolder, QueuedTaskHolderSort>
		implements IQueuedTaskHolderSearchQuery {

	public QueuedTaskHolderSearchQueryImpl() {
		super(QueuedTaskHolder.class, QueuedTaskHolderSort.CREATION_DATE);
	}

	@Override
	public IQueuedTaskHolderSearchQuery name(String name) {
		must(matchAllTermsIfGiven(name, QueuedTaskHolder.NAME));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery statuses(Collection<TaskStatus> statuses) {
		must(matchOneIfGiven(QueuedTaskHolder.STATUS, statuses));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery results(Collection<TaskResult> results) {
		must(matchOneIfGiven(QueuedTaskHolder.RESULT, results));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery types(Collection<String> types) {
		must(matchOneIfGiven(QueuedTaskHolder.TASK_TYPE, types));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery queueIds(Collection<String> queueIds) {
		must(matchOneIfGiven(QueuedTaskHolder.QUEUE_ID, queueIds));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery creationDate(Date creationDate) {
		must(matchRangeMax(QueuedTaskHolder.CREATION_DATE, creationDate));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery startDate(Date startDate) {
		must(matchRangeMax(QueuedTaskHolder.START_DATE, startDate));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery endDate(Date endDate) {
		must(matchRangeMax(QueuedTaskHolder.END_DATE, endDate));
		return this;
	}

}
