package fr.openwide.core.infinispan.model.impl;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import fr.openwide.core.infinispan.model.IPriorityQueue;

public class SimplePriorityQueue implements Serializable, IPriorityQueue {

	private static final long serialVersionUID = -2703012259547691866L;

	private final String key;

	private SimplePriorityQueue(String key) {
		super();
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return String.format("PriorityQueue<%s>", key);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(key)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SimplePriorityQueue)) {
			return false;
		}
		return new EqualsBuilder()
				.append(key, ((IPriorityQueue) obj).getKey())
				.isEquals();
	}

	public static final SimplePriorityQueue from(String lock) {
		return new SimplePriorityQueue(lock);
	}


}
