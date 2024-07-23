package org.iglooproject.jpa.more.business.task.search;

import java.util.Collection;
import java.util.Date;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public interface IQueuedTaskHolderSearchQuery
    extends ISearchQuery<QueuedTaskHolder, QueuedTaskHolderSort> {

  IQueuedTaskHolderSearchQuery name(String name);

  IQueuedTaskHolderSearchQuery statuses(Collection<TaskStatus> statuses);

  IQueuedTaskHolderSearchQuery results(Collection<TaskResult> results);

  IQueuedTaskHolderSearchQuery types(Collection<String> types);

  IQueuedTaskHolderSearchQuery queueIds(Collection<String> queueIds);

  IQueuedTaskHolderSearchQuery creationDate(Date creationDate);

  IQueuedTaskHolderSearchQuery startDate(Date startDate);

  IQueuedTaskHolderSearchQuery endDate(Date completionDate);
}
