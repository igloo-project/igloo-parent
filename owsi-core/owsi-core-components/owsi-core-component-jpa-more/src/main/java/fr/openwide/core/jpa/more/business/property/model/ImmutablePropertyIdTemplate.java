package fr.openwide.core.jpa.more.business.property.model;

public class ImmutablePropertyIdTemplate<T> extends PropertyIdTemplate<T, ImmutablePropertyId<T>> implements ImmutablePropertyRegistryKey<T> {

	private static final long serialVersionUID = -4239136517475520257L;

	public ImmutablePropertyIdTemplate(String format) {
		super(format);
	}

	@Override
	protected ImmutablePropertyId<T> create(String key) {
		return new ImmutablePropertyId<T>(key, this);
	}

}
