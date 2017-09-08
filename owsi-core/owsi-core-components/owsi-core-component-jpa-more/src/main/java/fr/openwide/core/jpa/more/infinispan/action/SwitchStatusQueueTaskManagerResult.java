package fr.openwide.core.jpa.more.infinispan.action;

public enum SwitchStatusQueueTaskManagerResult {

	/**
	 * QueueTaskManager is started
	 */
	STARTED,

	/**
	 * QueueTaskManager is stopped
	 */
	STOPPED,

	/**
	 * QueueTaskManager is already started
	 */
	ALREADY_STARTED,

	/**
	 * QueueTaskManager is already stopped
	 */
	ALREADY_STOPPED,

	/**
	 * TimeOut
	 */
	TIME_OUT
}
