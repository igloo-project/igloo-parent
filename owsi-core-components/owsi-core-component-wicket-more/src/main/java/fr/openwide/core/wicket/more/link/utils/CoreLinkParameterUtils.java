package fr.openwide.core.wicket.more.link.utils;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public final class CoreLinkParameterUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreLinkParameterUtils.class);

	public static final String ID_PARAMETER = "id";

	private CoreLinkParameterUtils() { }

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> IModel<E> extractGenericEntityParameterModel(
			IGenericEntityService<K, E> entityService, PageParameters parameters, String keyParameterName, Class<K> keyClass) {
		return new GenericEntityModel<K, E>(
				extractGenericEntityParameter(
						entityService, parameters,
						keyParameterName, keyClass
				)
		);
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> IModel<E> extractGenericEntityParameterModel(
			IGenericEntityService<K, E> entityService, PageParameters parameters, Class<K> keyClass) {
		return new GenericEntityModel<K, E>(
				extractGenericEntityParameter(
						entityService, parameters,
						keyClass
				)
		);
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E extractGenericEntityParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, Class<K> keyClass) {
		return extractGenericEntityParameter(
				entityService, parameters,
				ID_PARAMETER, keyClass
		);
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E extractGenericEntityParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, String keyParameterName, Class<K> keyClass) {
		Args.notNull(entityService, "entityService must not be null");
		Args.notNull(parameters, "parameters must not be null");
		Args.notNull(keyParameterName, "keyParameterName must not be null");
		Args.notNull(keyClass, "keyClass must not be null");
		
		E entity;
		
		try {
			K entityId = parameters.get(keyParameterName).to(keyClass);
			entity = entityService.getById(entityId);
		} catch (Exception e) {
			LOGGER.error("Error trying to extract a generic entity parameter (entityService: %s, parameters: %s, keyParameterName: %s, keyClass: %s)", e);
			entity = null;
		}

		return entity;
	}
}
