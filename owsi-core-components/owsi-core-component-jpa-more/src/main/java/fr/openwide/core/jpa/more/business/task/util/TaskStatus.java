package fr.openwide.core.jpa.more.business.task.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public enum TaskStatus {

	TO_RUN,
	RUNNING,
	COMPLETED,
	FAILED,
	INTERRUPTED,
	CANCELLED;
	
	public static final List<TaskStatus> CONSUMABLE_TASK_STATUS = Lists.newArrayList(TaskStatus.TO_RUN,
			TaskStatus.RUNNING, TaskStatus.FAILED, TaskStatus.INTERRUPTED);

	public static final List<TaskStatus> RELOADABLE_TASK_STATUS = Lists.newArrayList(TaskStatus.CANCELLED,
			TaskStatus.FAILED, TaskStatus.INTERRUPTED);
	
	public static final List<TaskStatus> CANCELLABLE_TASK_STATUS = Lists.newArrayList(TaskStatus.FAILED,
			TaskStatus.INTERRUPTED);
	
	public static List<String> getValuesAsStringList() {
		List<TaskStatus> taskStatusList = Arrays.asList(values());
		List<String> taskStatusStringList = new ArrayList<String>();

		for (TaskStatus taskStatus : taskStatusList) {
			taskStatusStringList.add(taskStatus.toString());
		}

		return taskStatusStringList;
	}

}