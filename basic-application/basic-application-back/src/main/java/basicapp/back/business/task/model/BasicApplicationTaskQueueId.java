package basicapp.back.business.task.model;

import org.iglooproject.jpa.more.business.task.model.IQueueId;

public enum BasicApplicationTaskQueueId implements IQueueId {

	// Define queue IDs here.
	; 

	@Override
	public String getUniqueStringId() {
		return name();
	}

}
