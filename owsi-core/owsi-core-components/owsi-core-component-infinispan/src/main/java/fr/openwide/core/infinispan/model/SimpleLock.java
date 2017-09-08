package fr.openwide.core.infinispan.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SimpleLock implements ILock {

	private static final long serialVersionUID = -2703012259547691866L;

	private final String key;

	private final String type;

	private SimpleLock(String key, String type) {
		super();
		this.key = key;
		this.type = type;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("Lock<%s, %s>", key, type);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(key)
				.append(type)
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
		if (!(obj instanceof SimpleLock)) {
			return false;
		}
		return new EqualsBuilder()
				.append(key, ((ILock) obj).getKey())
				.append(type, ((ILock) obj).getType())
				.isEquals();
	}

	public static final SimpleLock from(String lock, String type) {
		return new SimpleLock(lock, type);
	}


}
