package fr.openwide.core.jpa.more.business.task.model;

/**
 * An interface implemented by objects that represent a queue ID.
 * <p>Should generally be implemented as an enum, as in the following :
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
	
	String getUniqueStringId();

}
