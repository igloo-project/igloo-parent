package fr.openwide.core.jpa.more.infinispan.model;

import java.io.Serializable;

public class TaskQueueStatus implements Serializable{

	private static final long serialVersionUID = -2939382653559667093L;

	private String id;
	private boolean active;
	private int numberOfRunningTasks;
	private int numberOfWaitingTasks;
	private int numberOfThreads;

	public TaskQueueStatus(String id, boolean active, int numberOfRunningTasks, int numberOfWaitingTasks, int numberOfThread){
		this.id = id;
		this.active = active;
		this.numberOfRunningTasks = numberOfRunningTasks;
		this.numberOfWaitingTasks = numberOfWaitingTasks;
		this.numberOfThreads = numberOfThread;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getNumberOfRunningTasks() {
		return numberOfRunningTasks;
	}

	public void setNumberOfRunningTasks(int numberOfRunningTasks) {
		this.numberOfRunningTasks = numberOfRunningTasks;
	}

	public int getNumberOfWaitingTasks() {
		return numberOfWaitingTasks;
	}

	public void setNumberOfWaitingTasks(int numberOfWaitingTasks) {
		this.numberOfWaitingTasks = numberOfWaitingTasks;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

}
