package fr.openwide.core.spring.property.model;

public class MutablePropertyIdTemplate<T> extends PropertyIdTemplate<T, MutablePropertyId<T>> implements MutablePropertyRegistryKey<T> {

	private static final long serialVersionUID = -4239136517475520257L;

	public MutablePropertyIdTemplate(String format) {
		super(format);
	}

	@Override
	protected MutablePropertyId<T> create(String key) {
		return new MutablePropertyId<T>(key, this);
	}

}
