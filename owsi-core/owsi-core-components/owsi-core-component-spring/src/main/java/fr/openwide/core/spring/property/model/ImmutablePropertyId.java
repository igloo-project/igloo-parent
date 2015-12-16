package fr.openwide.core.spring.property.model;

public class ImmutablePropertyId<T> extends PropertyId<T> implements ImmutablePropertyRegistryKey<T> {

	private static final long serialVersionUID = -582080164746829767L;

	public ImmutablePropertyId(String key) {
		super(key);
	}

	public ImmutablePropertyId(String key, ImmutablePropertyIdTemplate<T> propertyTemplateId) {
		super(key, propertyTemplateId);
	}

}
