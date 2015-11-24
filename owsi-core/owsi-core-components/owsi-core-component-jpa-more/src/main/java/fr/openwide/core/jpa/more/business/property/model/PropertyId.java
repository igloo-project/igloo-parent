package fr.openwide.core.jpa.more.business.property.model;

import java.io.Serializable;

import com.google.common.base.Preconditions;

public abstract class PropertyId<T> implements Serializable {

	private static final long serialVersionUID = 995641080772195895L;

	private final String key;

	protected PropertyId(String key) {
		Preconditions.checkNotNull(key);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
