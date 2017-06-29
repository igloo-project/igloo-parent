package fr.openwide.core.jpa.more.infinispan.model;

import java.io.Serializable;
import java.util.Map;

import org.bindgen.Bindable;

import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.business.task.model.IQueueId;

@Bindable
public class QueueTaskManagerStatus implements Serializable{

	private static final long serialVersionUID = -8778531594490781393L;

	private boolean queueManagerActive;
	private Map<String, IQueueId> queuesById = Maps.newHashMap();
	private Map<String, TaskQueueStatus> taskQueueStatusById = Maps.newHashMap();

	public QueueTaskManagerStatus(){
	}

	public boolean isQueueManagerActive() {
		return queueManagerActive;
	}

	public void setQueueManagerActive(boolean queueManagerActive) {
		this.queueManagerActive = queueManagerActive;
	}

	public Map<String, IQueueId> getQueuesById() {
		return queuesById;
	}

	public void setQueuesById(Map<String, IQueueId> queuesById) {
		this.queuesById = queuesById;
	}

	public Map<String, TaskQueueStatus> getTaskQueueStatusById() {
		return taskQueueStatusById;
	}

	public void setTaskQueueStatusById(Map<String, TaskQueueStatus> taskQueueStatusById) {
		this.taskQueueStatusById = taskQueueStatusById;
	}

}

