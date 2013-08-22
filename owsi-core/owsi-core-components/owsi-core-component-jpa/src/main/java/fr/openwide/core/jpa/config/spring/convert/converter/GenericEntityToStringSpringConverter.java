package fr.openwide.core.jpa.config.spring.convert.converter;

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
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (!sourceType.isAssignableTo(GENERIC_ENTITY_TYPE_DESCRIPTOR)) {
			return false;
		}
		if (!targetType.isAssignableTo(STRING_TYPE_DESCRIPTOR)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Class<?> keyType = GenericEntityTypeResolver.resolveTypeParameters((Class<? extends GenericEntity<?,?>>) sourceType.getType()).getLeft();
		
		if (keyType != null) {
			return this.conversionService.canConvert(keyType, STRING_TYPE_DESCRIPTOR.getType());
		} else {
			return false;
		}
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		GenericEntity<?, ?> entity = (GenericEntity<?,?>) source;
		Object id = entity.getId();
		if (id == null) {
			throw new RuntimeException("The entity id was null");
		} else {
			return conversionService.convert(id, String.class);
		}
	}
}
