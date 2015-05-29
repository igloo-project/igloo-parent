package fr.openwide.core.jpa.more.business.task.search;

import java.util.Collection;
import java.util.Date;

import org.springframework.context.annotation.Scope;

import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.util.TaskResult;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

@Scope("prototype")
public interface IQueuedTaskHolderSearchQuery extends ISearchQuery<QueuedTaskHolder, QueuedTaskHolderSort> {

	IQueuedTaskHolderSearchQuery name(String name);
	
	IQueuedTaskHolderSearchQuery statuses(Collection<TaskStatus> statuses);
	
	IQueuedTaskHolderSearchQuery results(Collection<TaskResult> results);
	
	IQueuedTaskHolderSearchQuery types(Collection<String> types);
	
	IQueuedTaskHolderSearchQuery queueIds(Collection<String> queueIds);
	
	IQueuedTaskHolderSearchQuery creationDate(Date creationDate);
	
	IQueuedTaskHolderSearchQuery startDate(Date startDate);
	
	IQueuedTaskHolderSearchQuery endDate(Date completionDate);
	
}