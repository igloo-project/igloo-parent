package org.iglooproject.wicket.bootstrap3.console.common.util;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.model.GenericEntityModel;

/**
 * @deprecated Use the {@link ILinkDescriptor link} subsystem instead.
 */
@Deprecated
public final class LinkUtils {

	/**
	 * @deprecated Use the {@link org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters#ID} subsystem instead.
	 */
	@Deprecated
	public static final String ID_PARAMETER = "id";

	private LinkUtils() {
	}

	/**
	 * @deprecated Use the {@link ILinkDescriptor link} subsystem instead.
	 */
	@Deprecated
	public static PageParameters getGenericEntityIdPageParameters(
			IModel<? extends GenericEntity<?, ?>> genericEntityModel) {
		PageParameters parameters;
		if (genericEntityModel != null) {
			parameters = getGenericEntityIdPageParameters(genericEntityModel.getObject());
		} else {
			parameters = new PageParameters();
		}
		return parameters;
	}

	/**
	 * @deprecated Use the {@link ILinkDescriptor link} subsystem instead.
	 */
	@Deprecated
	public static PageParameters getGenericEntityIdPageParameters(GenericEntity<?, ?> genericEntity) {
		PageParameters parameters = new PageParameters();
		if (genericEntity != null) {
			parameters.add(ID_PARAMETER, genericEntity.getId());
		}
		return parameters;
	}

	/**
	 * @deprecated Use the {@link ILinkDescriptor link} subsystem instead, in particular the {@link ILinkDescriptor#extract(PageParameters)} method.
	 */
	@Deprecated
	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> IModel<E> extractGenericEntityModelParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, Class<K> keyClass) {
		return new GenericEntityModel<>(extractGenericEntitySpecifiedParameter(entityService, parameters,
				LinkUtils.ID_PARAMETER, keyClass));
	}

	/**
	 * @deprecated Use the {@link ILinkDescriptor link} subsystem instead, in particular the {@link ILinkDescriptor#extract(PageParameters)} method.
	 */
	@Deprecated
	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E extractGenericEntitySpecifiedParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, String parameterKey, Class<K> keyClass) {
		E entity;

		try {
			K entityId = parameters.get(parameterKey).to(keyClass);
			entity = entityService.getById(entityId);
		} catch (RuntimeException e) {
			entity = null;
		}

		return entity;
	}

	/**
	 * @deprecated Use the {@link ILinkDescriptor link} subsystem instead, in particular the {@link ILinkDescriptor#fullUrl()} method.
	 */
	@Deprecated
	public static String getUrl(Class<? extends Page> clazz, PageParameters parameters) {
		return RequestCycle.get().getUrlRenderer()
				.renderFullUrl(Url.parse(RequestCycle.get().urlFor(clazz, parameters).toString()));
	}
}
