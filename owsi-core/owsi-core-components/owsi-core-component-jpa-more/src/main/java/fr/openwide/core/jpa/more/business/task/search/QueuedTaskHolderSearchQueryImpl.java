package fr.openwide.core.jpa.more.business.task.search;

import java.util.Collection;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.util.TaskResult;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;

@Component
@Scope("prototype")
public class QueuedTaskHolderSearchQueryImpl extends AbstractHibernateSearchSearchQuery<QueuedTaskHolder, QueuedTaskHolderSort>
		implements IQueuedTaskHolderSearchQuery {

	public QueuedTaskHolderSearchQueryImpl() {
		super(QueuedTaskHolder.class, QueuedTaskHolderSort.CREATION_DATE);
	}

	@Override
	public IQueuedTaskHolderSearchQuery name(String name) {
		matchAllTermsIfGiven(name, CoreJpaMoreBindings.queuedTaskHolder().name());
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery statuses(Collection<TaskStatus> statuses) {
		matchOneIfGiven(CoreJpaMoreBindings.queuedTaskHolder().status(), statuses);
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery results(Collection<TaskResult> results) {
		matchOneIfGiven(CoreJpaMoreBindings.queuedTaskHolder().result(), results);
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery types(Collection<String> types) {
		matchOneIfGiven(CoreJpaMoreBindings.queuedTaskHolder().taskType(), types);
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery queueIds(Collection<String> queueIds) {
		matchOneIfGiven(CoreJpaMoreBindings.queuedTaskHolder().queueId(), queueIds);
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery creationDate(Date creationDate) {
		matchRangeMax(CoreJpaMoreBindings.queuedTaskHolder().creationDate(), creationDate);
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery startDate(Date startDate) {
		matchRangeMax(CoreJpaMoreBindings.queuedTaskHolder().startDate(), startDate);
		return this;
	}

	@Override
	public IQueuedTaskHolderSearchQuery endDate(Date endDate) {
		matchRangeMax(CoreJpaMoreBindings.queuedTaskHolder().endDate(), endDate);
		return this;
	}

}
