package fr.openwide.core.jpa.config.spring.convert.converter;

import java.io.Serializable;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IEntityService;

/**
 * Converts a String to a GenericEntity.
 * Only matches if String.class can be converted to the target GenericEntity key type.
 */
public class StringToGenericEntitySpringConverter implements ConditionalGenericConverter {
	
	private final static TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
	private final static TypeDescriptor GENERIC_ENTITY_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(GenericEntity.class);

	private final ConversionService conversionService;
	private final IEntityService entityService;
	
	public StringToGenericEntitySpringConverter(ConversionService conversionService, IEntityService entityService) {
		this.conversionService = conversionService;
		this.entityService = entityService;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (!sourceType.isAssignableTo(STRING_TYPE_DESCRIPTOR)) {
			return false;
		}
		if (!targetType.isAssignableTo(GENERIC_ENTITY_TYPE_DESCRIPTOR)) {
			return false;
		}
		
		return canConvertKey(sourceType.getType(), (Class<? extends GenericEntity<?,?>>) targetType.getType());
	}
	
	private <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			boolean canConvertKey(Class<?> sourceType, Class<E> targetType) {
		Class<K> keyType = GenericEntityTypeResolver.resolveKeyTypeParameter(targetType);
		
		if (keyType != null) {
			return this.conversionService.canConvert(sourceType, keyType);
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
		String string = (String) source;
		return convert(
				string,
				(Class<? extends GenericEntity<?,?>>) targetType.getType()
		);
	}

	private <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			E convert(String source, Class<E> targetType) {
		Class<K> keyType = GenericEntityTypeResolver.resolveKeyTypeParameter(targetType);
		K id = convertKey(source, keyType);
		E entity = entityService.getEntity(targetType, id);
		if (entity == null) {
			throw new RuntimeException("The entity for key " + id + " was not found");
		} else {
			return entity;
		}
	}
	
	private <K extends Serializable & Comparable<K>>
			K convertKey(String source, Class<K> targetKeyType) {
		K id = conversionService.convert(source, targetKeyType);
		if (id == null) {
			throw new RuntimeException("The converted entity id was null");
		} else {
			return id;
		}
	}
}
