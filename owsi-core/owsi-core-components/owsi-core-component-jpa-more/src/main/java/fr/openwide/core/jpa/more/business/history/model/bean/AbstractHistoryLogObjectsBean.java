package fr.openwide.core.jpa.more.business.history.model.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;

/**
 * An abstract base for beans containing parameters used when creating an {@link AbstractHistoryLog}.
 */
public abstract class AbstractHistoryLogObjectsBean<T> {
	
	/**
	 * The main object of the action.
	 * <p>This is typically the object that will be modified as a result of the action.
	 */
	private T mainObject;
	
	/**
	 * Secondary objects of the action, i.e. parameters that may not be modified as a result of the action,
	 * but are at least used as an input.
	 * <p><strong>Caution: you should use this very rarely.</strong> Typically, the secondary objects are useful
	 * when recording a two-or-more-object operation, which is rare. When the only thing you need is recording
	 * contextual information, such as objects that are pointed to by links from the main object that can change
	 * over time, you should really consider adding typed fields to this bean and to the HistoryLog. 
	 */
	private Object[] secondaryObjects;

	/**
	 * @param mainObject see {@link #mainObject}
	 * @param secondaryObjects see {@link #secondaryObjects}
	 */
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
	
	/**
	 * Only compares the main object.
	 * <p>This is because, in a given database transaction, the secondary objects
	 * should be the same whenever the main object is the same. The secondary
	 * objects are only useful for history.
	 * @see Object#equals(Object)
	 */
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
	
	/**
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(mainObject)
				.toHashCode();
	}

}
