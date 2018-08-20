package org.iglooproject.functional.converter;

import java.util.Objects;

import org.iglooproject.functional.SerializableFunction2;

import com.google.common.base.Converter;

public abstract class SerializableConverter2<A, B> extends Converter<A, B> implements SerializableFunction2<A, B> {

	private static final long serialVersionUID = 2071368919822452496L;

	public static final <A,B> SerializableConverter2<A, B> from(Converter<A, B> converter) {
		Objects.requireNonNull(converter);
		return new SerializableConverter2<A, B>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected B doForward(A a) {
				return converter.convert(a);
			}
			@Override
			protected A doBackward(B b) {
				return converter.reverse().convert(b);
			}
		};
	}

}
