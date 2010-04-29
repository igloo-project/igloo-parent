package fr.openwide.hibernate.business.generic.model;

import java.io.Serializable;

public abstract class GenericEntity<T> implements Serializable, Comparable<T> {

	private static final long serialVersionUID = -3988499137919577054L;
	
	public abstract Integer getId();

	public abstract void setId(Integer id);
	
	public boolean isNew() {
		return getId() == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object object) {
		if(this == object) {
			return true;
		}
		if(!this.getClass().isInstance(object)) {
			return false;
		}
		
		GenericEntity<T> entity = (GenericEntity<T>) object;
		Number id = getId();
		
		if(id == null) {
			return false;
		}
		
		return id.equals(entity.getId());
	}

	@Override
	public int hashCode() {
		int hash = 7;
		
		Integer id = getId();
		hash = 31 * hash + ((id == null) ? 0 : id.hashCode());

		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("wombat.");
		builder.append(getClass().getSimpleName());
		builder.append("<");
		builder.append(getId());
		builder.append("-");
		builder.append(getNameForToString());
		builder.append(">");
		
		return builder.toString();
	}
	
	public abstract String getNameForToString();
	
	public abstract String getDisplayName();
	
}
