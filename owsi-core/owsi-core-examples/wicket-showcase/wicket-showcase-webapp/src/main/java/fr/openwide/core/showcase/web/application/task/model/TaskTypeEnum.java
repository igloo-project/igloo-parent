package fr.openwide.core.showcase.web.application.task.model;

import fr.openwide.core.jpa.more.business.task.model.ITaskTypeProvider;

public enum TaskTypeEnum implements ITaskTypeProvider {

	SUCCESS,

	FAILED,

	;

	@Override
	public String getTaskType() {
		return name();
	}
}
