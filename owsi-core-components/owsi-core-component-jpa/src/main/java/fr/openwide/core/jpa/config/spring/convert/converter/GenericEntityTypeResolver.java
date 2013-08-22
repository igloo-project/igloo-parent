package fr.openwide.core.jpa.config.spring.convert.converter;

import java.io.Serializable;

import org.springframework.core.GenericTypeResolver;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityTypeResolver {

	private GenericEntityTypeResolver() { }
	
	/**
	 * @return the resolved entity key type parameter (K) and entity type parameter (E), or {@code null} if not resolvable
	 */
	@SuppressWarnings("unchecked")
	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			Class<K> resolveKeyTypeParameter(Class<E> clazz) {
		Class<?>[] typeParameters = GenericTypeResolver.resolveTypeArguments(clazz, GenericEntity.class);
		return typeParameters == null ? null : (Class<K>) typeParameters[0];
	}

}
