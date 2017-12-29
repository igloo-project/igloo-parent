package org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter;

import java.util.Collection;

import javax.annotation.Nullable;

import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;

import org.iglooproject.commons.util.functional.SerializableSupplier;

public class LinkParameterTypeInformation<T> {
	
	/**
	 * We must wrap the typedescriptor in a supplier, since it is not serializable, and clients are serializable (so
	 * they cannot hold references to non-serializable objects).
	 */
	private final Supplier<? extends TypeDescriptor> typeDescriptorSupplier;
	
	private final Supplier<? extends T> emptyValueSupplier;
	
	public static <T> LinkParameterTypeInformation<T> valueOf(final Class<T> clazz) {
		return new LinkParameterTypeInformation<T>(
				new SerializableSupplier<TypeDescriptor>() {
					private static final long serialVersionUID = 1L;
					@Override
					public TypeDescriptor get() {
						return TypeDescriptor.valueOf(clazz);
					}
				},
				null
		);
	}
	
	public static <T extends Collection<TElement>, TElement> LinkParameterTypeInformation<T> collection(
			final Class<? super T> clazz, final Class<TElement> elementType) {
		return new LinkParameterTypeInformation<T>(
				new SerializableSupplier<TypeDescriptor>() {
					private static final long serialVersionUID = 1L;
					@Override
					public TypeDescriptor get() {
						return TypeDescriptor.collection(clazz, TypeDescriptor.valueOf(elementType));
					}
				},
				null
		);
	}
	
	public static <T extends Collection<?>> LinkParameterTypeInformation<T> collection(
			final Class<? super T> clazz, final TypeDescriptor elementType) {
		return new LinkParameterTypeInformation<T>(
				new SerializableSupplier<TypeDescriptor>() {
					private static final long serialVersionUID = 1L;
					@Override
					public TypeDescriptor get() {
						return TypeDescriptor.collection(clazz, elementType);
					}
				},
				null
		);
	}
	
	public static <T extends Collection<?>> LinkParameterTypeInformation<T> collection(
			final Class<? super T> clazz, final TypeDescriptor elementType, Supplier<? extends T> emptyValueSupplier) {
		return new LinkParameterTypeInformation<T>(
				new SerializableSupplier<TypeDescriptor>() {
					private static final long serialVersionUID = 1L;
					@Override
					public TypeDescriptor get() {
						return TypeDescriptor.collection(clazz, elementType);
					}
				},
				emptyValueSupplier
		);
	}

	private LinkParameterTypeInformation(Supplier<? extends TypeDescriptor> typeDescriptorSupplier,
			Supplier<? extends T> emptyValueSupplier) {
		super();
		this.typeDescriptorSupplier = typeDescriptorSupplier;
		this.emptyValueSupplier = emptyValueSupplier;
	}

	public Supplier<? extends TypeDescriptor> getTypeDescriptorSupplier() {
		return typeDescriptorSupplier;
	}

	@Nullable
	public Supplier<? extends T> getEmptyValueSupplier() {
		return emptyValueSupplier;
	}
	
}
