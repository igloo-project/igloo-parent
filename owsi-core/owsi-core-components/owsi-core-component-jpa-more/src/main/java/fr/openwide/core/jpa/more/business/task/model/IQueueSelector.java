package fr.openwide.core.jpa.more.business.task.model;

import java.util.Collection;

public interface IQueueSelector<TQueueId> {
	
	TQueueId select(Collection<TQueueId> availableQueues);

}
