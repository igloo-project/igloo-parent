package org.iglooproject.spring.property.model;

public final class MutablePropertyIdTemplate<T> extends PropertyIdTemplate<T, MutablePropertyId<T>>
		implements IMutablePropertyRegistryKey<T> {

	private static final long serialVersionUID = -4239136517475520257L;

	/**
	 * This constructor is package-protected.
	 * Use {@link AbstractPropertyIds#mutableTemplate(String)} for building this type of property ID template.
	 */
	/*package*/ MutablePropertyIdTemplate(IPropertyRegistryKeyDeclaration declaration, String format) {
		super(declaration, format);
	}

	@Override
	protected MutablePropertyId<T> create(String key) {
		return new MutablePropertyId<>(this, key);
	}

}
