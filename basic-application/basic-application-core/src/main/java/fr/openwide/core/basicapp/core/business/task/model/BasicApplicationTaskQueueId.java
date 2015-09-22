package fr.openwide.core.basicapp.core.business.task.model;

import fr.openwide.core.jpa.more.business.task.model.IQueueId;

public enum BasicApplicationTaskQueueId implements IQueueId {
	
	// Define queue IDs here.
	; 

	@Override
	public String getUniqueStringId() {
		return name();
	}

}
