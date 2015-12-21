package fr.openwide.core.jpa.more.business.history.model.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import fr.openwide.core.commons.util.CloneUtils;

public abstract class AbstractHistoryLogObjectsBean<T> {
	
	private T mainObject;
	
	private Object[] secondaryObjects;

	protected AbstractHistoryLogObjectsBean(T mainObject, Object... secondaryObjects) {
		this.mainObject = mainObject;
		this.secondaryObjects = CloneUtils.clone(secondaryObjects);
	}
	
	public T getMainObject() {
		return mainObject;
	}
	
	public Object[] getSecondaryObjects() {
		return CloneUtils.clone(secondaryObjects);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractHistoryLogObjectsBean) {
			AbstractHistoryLogObjectsBean<?> other = (AbstractHistoryLogObjectsBean<?>) obj;
			return new EqualsBuilder()
					.append(mainObject, other.mainObject)
					.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(mainObject)
				.toHashCode();
	}

}
