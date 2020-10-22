package org.iglooproject.jpa.more.business.task.util;

/**
 * <strong>Warning</strong>: we use {@link Enum#ordinal()} to compare result's severity,
 * so be careful with declaring order.
 */
public enum TaskResult {

	SUCCESS,
	WARN,
	ERROR,
	FATAL;

}
