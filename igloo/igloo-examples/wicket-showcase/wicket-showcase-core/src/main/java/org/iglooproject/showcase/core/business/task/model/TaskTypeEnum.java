package org.iglooproject.showcase.core.business.task.model;

import org.iglooproject.jpa.more.business.task.model.ITaskTypeProvider;

public enum TaskTypeEnum implements ITaskTypeProvider {

	SUCCESS,
	FAILED;

	@Override
	public String getTaskType() {
		return name();
	}
}
