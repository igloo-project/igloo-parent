package fr.openwide.core.jpa.more.business.property.model;

public class MutablePropertyId<T> extends PropertyId<T> implements MutablePropertyRegistryKey<T> {

	private static final long serialVersionUID = 7635126176447950686L;

	public MutablePropertyId(String key) {
		super(key);
	}

	public MutablePropertyId(String key, MutablePropertyIdTemplate<T> propertyTemplateId) {
		super(key, propertyTemplateId);
	}

}
