package fr.openwide.core.test.jpa.more.business.task.model;

import fr.openwide.core.jpa.more.business.task.model.IQueueId;

public enum TestQueueId implements IQueueId {
	QUEUE_1,
	QUEUE_2;

	@Override
	public String getUniqueStringId() {
		return name();
	}

}
