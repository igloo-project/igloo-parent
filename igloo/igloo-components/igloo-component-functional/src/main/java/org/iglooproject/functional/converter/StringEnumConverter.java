package org.iglooproject.functional.converter;

import org.iglooproject.functional.converter.SerializableConverter2;
import org.springframework.util.StringUtils;

public class StringEnumConverter<E extends Enum<E>> extends SerializableConverter2<String, E> {

	private static final long serialVersionUID = -7705243123017929598L;

	public static <E extends Enum<E>> StringEnumConverter<E> forType(Class<E> enumType) {
		return new StringEnumConverter<>(enumType);
	}

	private Class<E> enumType;

	private StringEnumConverter(Class<E> enumType) {
		this.enumType = enumType;
	}

	@Override
	protected E doForward(String a) {
		if (!StringUtils.hasText(a)) {
			throw new IllegalArgumentException("Enum value cannot be null");
		}
		
		try {
			return Enum.valueOf(enumType, a);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid enum value " + a, e);
		}
	}

	@Override
	protected String doBackward(E b) {
		return b.name();
	}

	/**
	 * Workaround sonar/findbugs - https://github.com/google/guava/issues/1858
	 * Guava Converter overrides only equals to add javadoc, but findbugs warns about non coherent equals/hashcode
	 * possible issue.
	 */
	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	/**
	 * Workaround sonar/findbugs - see #equals(Object)
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
