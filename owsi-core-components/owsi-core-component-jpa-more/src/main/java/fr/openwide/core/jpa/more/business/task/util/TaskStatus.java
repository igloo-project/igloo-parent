package fr.openwide.core.jpa.more.business.task.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TaskStatus {

	TO_RUN,
	RUNNING,
	COMPLETED,
	FAILED,
	INTERRUPTED,
	CANCELLED;
	
	public static List<String> getValuesAsStringList() {
		List<TaskStatus> taskStatusList = Arrays.asList(values());
		List<String> taskStatusStringList = new ArrayList<String>();

		for (TaskStatus taskStatus : taskStatusList) {
			taskStatusStringList.add(taskStatus.toString());
		}

		return taskStatusStringList;
	}
}