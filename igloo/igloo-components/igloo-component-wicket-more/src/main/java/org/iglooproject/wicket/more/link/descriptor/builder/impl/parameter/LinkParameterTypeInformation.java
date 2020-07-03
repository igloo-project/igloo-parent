package org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter;

import java.util.Collection;

import javax.annotation.Nullable;

import org.iglooproject.functional.SerializableSupplier2;
import org.springframework.core.convert.TypeDescriptor;

public class LinkParameterTypeInformation<T> {
	
	/**
	 * We must wrap the typedescriptor in a supplier, since it is not serializable, and clients are serializable (so
	 * they cannot hold references to non-serializable objects).
	 */
	private final SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier;
	
	private final SerializableSupplier2<? extends T> emptyValueSupplier;
	
	public static <T> LinkParameterTypeInformation<T> valueOf(final Class<T> clazz) {
		return new LinkParameterTypeInformation<>(
				() -> TypeDescriptor.valueOf(clazz),
				null
		);
	}
	
	public static <T extends Collection<TElement>, TElement> LinkParameterTypeInformation<T> collection(
			final Class<? super T> clazz, final Class<TElement> elementType) {
		return new LinkParameterTypeInformation<>(
				() -> TypeDescriptor.collection(clazz, TypeDescriptor.valueOf(elementType)),
				null
		);
	}
	
	public static <T extends Collection<?>> LinkParameterTypeInformation<T> collection(
			final Class<? super T> clazz, final TypeDescriptor elementType) {
		return new LinkParameterTypeInformation<>(
				() -> TypeDescriptor.collection(clazz, elementType),
				null
		);
	}
	
	public static <T extends Collection<?>> LinkParameterTypeInformation<T> collection(
			final Class<? super T> clazz, final TypeDescriptor elementType, SerializableSupplier2<? extends T> emptyValueSupplier) {
		return new LinkParameterTypeInformation<>(
				() -> TypeDescriptor.collection(clazz, elementType),
				emptyValueSupplier
		);
	}

	private LinkParameterTypeInformation(SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier,
			SerializableSupplier2<? extends T> emptyValueSupplier) {
		super();
		this.typeDescriptorSupplier = typeDescriptorSupplier;
		this.emptyValueSupplier = emptyValueSupplier;
	}

	public SerializableSupplier2<? extends TypeDescriptor> getTypeDescriptorSupplier() {
		return typeDescriptorSupplier;
	}

	@Nullable
	public SerializableSupplier2<? extends T> getEmptyValueSupplier() {
		return emptyValueSupplier;
	}
	
}
