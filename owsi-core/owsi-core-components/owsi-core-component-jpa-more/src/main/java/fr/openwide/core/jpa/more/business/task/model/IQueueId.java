package fr.openwide.core.jpa.more.business.task.model;

import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;

/**
 * An interface implemented by objects that represent a queue ID.
 * <p>The queue IDs defined in an application must be defined in such a way that, given two non-null <code>IQueueId</code> <code>id1</code> and <code>id2</code>,
 * <code>id1.getUniqueStringId().equals(id2.getUniqueStringId())</code> is true if and only if <code>id1</code> and <code>id2</code>
 * refer to the same queue.
 * <p>This interface should generally be implemented as an enum, as in the following :
 * <pre>
 * public enum MyQueuId extends IQueueId {
 * 	QUEUE_1,
 * 	QUEUE_2;
 * 	public String getUniqueStringId() {
 * 		return name();
 * 	}
 * }
 * </pre>
 */
public interface IQueueId {
	
	/**
	 * A string that is reserved to {@link IQueuedTaskHolderManager} implementors.
	 */
	IQueueId RESERVED_DEFAULT_QUEUE_ID_STRING = DefaultQueueId.DEFAULT;
	
	/**
	 * Returns a string that uniquely identifies the queue.
	 * <p>This method should always return the same value when called multiple times.
	 * <p><strong>NOTE:</strong> implementors must never return a string equal to {@link #RESERVED_DEFAULT_QUEUE_ID_STRING}. 
	 * @return a non-null string identifying a queue
	 */
	String getUniqueStringId();

}
