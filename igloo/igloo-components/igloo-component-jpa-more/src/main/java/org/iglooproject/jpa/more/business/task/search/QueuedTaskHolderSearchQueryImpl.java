package org.iglooproject.jpa.more.business.task.search;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;

// TODO: igloo-boot
public class QueuedTaskHolderSearchQueryImpl implements IQueuedTaskHolderSearchQuery {

	public QueuedTaskHolderSearchQueryImpl() {
//		super(QueuedTaskHolder.class, QueuedTaskHolderSort.CREATION_DATE);
	}

	@Override
	public IQueuedTaskHolderSearchQuery name(String name) {
//		must(matchAllTermsIfGiven(name, QueuedTaskHolder.NAME));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery statuses(Collection<TaskStatus> statuses) {
//		must(matchOneIfGiven(QueuedTaskHolder.STATUS, statuses));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery results(Collection<TaskResult> results) {
//		must(matchOneIfGiven(QueuedTaskHolder.RESULT, results));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery types(Collection<String> types) {
//		must(matchOneIfGiven(QueuedTaskHolder.TASK_TYPE, types));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery queueIds(Collection<String> queueIds) {
//		must(matchOneIfGiven(QueuedTaskHolder.QUEUE_ID, queueIds));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery creationDate(Date creationDate) {
//		must(matchRangeMax(QueuedTaskHolder.CREATION_DATE, creationDate));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery startDate(Date startDate) {
//		must(matchRangeMax(QueuedTaskHolder.START_DATE, startDate));
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery endDate(Date endDate) {
//		must(matchRangeMax(QueuedTaskHolder.END_DATE, endDate));
		return this;
	}

	@Override
	public ISearchQuery<QueuedTaskHolder, QueuedTaskHolderSort> sort(Map<QueuedTaskHolderSort, SortOrder> sortMap) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public List<QueuedTaskHolder> fullList() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public List<QueuedTaskHolder> list(long offset, long limit) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

}
