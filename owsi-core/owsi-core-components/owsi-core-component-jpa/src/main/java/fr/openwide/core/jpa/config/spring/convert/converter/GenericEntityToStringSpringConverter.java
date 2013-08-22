package fr.openwide.core.jpa.config.spring.convert.converter;

import java.io.Serializable;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * Converts a GenericEntity to a String.
 * Only matches if the target GenericEntity key type can be converted to String.class.
 */
public class GenericEntityToStringSpringConverter implements ConditionalGenericConverter {
	
	private final static TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
	private final static TypeDescriptor GENERIC_ENTITY_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(GenericEntity.class);

	private final ConversionService conversionService;
	
	public GenericEntityToStringSpringConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (!sourceType.isAssignableTo(GENERIC_ENTITY_TYPE_DESCRIPTOR)) {
			return false;
		}
		if (!targetType.isAssignableTo(STRING_TYPE_DESCRIPTOR)) {
			return false;
		}
		return canConvertKey((Class<? extends GenericEntity<?, ?>>)sourceType.getType(), targetType.getType());
	}
	
	private <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			boolean canConvertKey(Class<E> sourceType, Class<?> targetType) {
		Class<K> keyType = GenericEntityTypeResolver.resolveKeyTypeParameter(sourceType);
		
		if (keyType != null) {
			return this.conversionService.canConvert(keyType, targetType);
		} else {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		return convertKey((GenericEntity<?, ?>) source, (Class<? extends String>) targetType.getType());
	}

	private <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			String convertKey(E source, Class<? extends String> targetType) {
		K id = source.getId();
		if (id == null) {
			throw new RuntimeException("The entity id was null");
		} else {
			return conversionService.convert(id, targetType);
		}
	}
}
