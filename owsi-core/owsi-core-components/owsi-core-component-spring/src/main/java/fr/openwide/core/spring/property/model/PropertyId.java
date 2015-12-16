package fr.openwide.core.spring.property.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Preconditions;

public abstract class PropertyId<T> implements PropertyRegistryKey<T> {

	private static final long serialVersionUID = 995641080772195895L;

	private final String key;

	private final PropertyIdTemplate<T, ?> template;

	protected PropertyId(String key) {
		this(key, null);
	}

	protected PropertyId(String key, PropertyIdTemplate<T, ?> template) {
		Preconditions.checkNotNull(key);
		this.key = key;
		this.template = template;
	}

	public String getKey() {
		return key;
	}

	public PropertyIdTemplate<T, ?> getTemplate() {
		return template;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof PropertyId)) {
			return false;
		}
		PropertyId<?> other = (PropertyId<?>) obj;
		return new EqualsBuilder()
				.append(getClass(), obj.getClass())
				.append(key, other.key)
				.append(template, other.template)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getClass())
				.append(key)
				.append(template)
				.build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(getClass())
				.append(key)
				.append(template)
				.build();
	}

}
