package fr.openwide.core.wicket.more.console.common.util;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.wicket.more.console.maintenance.task.page.ConsoleMaintenanceTaskDescriptionPage;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.factory.AbstractLinkFactory;
import fr.openwide.core.wicket.more.link.utils.CoreLinkParameterUtils;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

/**
 * @deprecated Use {@link ConsoleMaintenanceTaskDescriptionPage#linkDescriptor(IModel)} or {@link CoreLinkParameterUtils} or a {@link AbstractLinkFactory LinkFactory} (depends on the method you need).
 */
@Deprecated
public final class LinkUtils {

	/**
	 * @deprecated Use {@link CoreLinkParameterUtils#ID_PARAMETER} instead.
	 */
	@Deprecated
	public static final String ID_PARAMETER = "id";

	private LinkUtils() {
	}
	
	/**
	 * @deprecated Use {@link ConsoleMaintenanceTaskDescriptionPage#linkDescriptor(IModel)} instead.
	 */
	@Deprecated
	public static Link<QueuedTaskHolder> getQueuedTaskHolderLink(String id, IModel<? extends QueuedTaskHolder> queuedTaskHolderModel) {
		return new BookmarkablePageLink<QueuedTaskHolder>(id, ConsoleMaintenanceTaskDescriptionPage.class,
				getGenericEntityIdPageParameters(queuedTaskHolderModel));
	}

	/**
	 * @deprecated Use a {@link AbstractLinkFactory LinkFactory} and {@link ILinkDescriptor link descriptors} instead.
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
	 * @deprecated Use a {@link AbstractLinkFactory LinkFactory} and {@link ILinkDescriptor link descriptors} instead.
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
	 * @deprecated Use {@link CoreLinkParameterUtils#extractGenericEntityParameterModel(IGenericEntityService, PageParameters, Class)} instead.
	 */
	@Deprecated
	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> IModel<E> extractGenericEntityModelParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, Class<K> keyClass) {
		return new GenericEntityModel<K, E>(extractGenericEntitySpecifiedParameter(entityService, parameters,
				LinkUtils.ID_PARAMETER, keyClass));
	}

	/**
	 * @deprecated Use {@link CoreLinkParameterUtils#extractGenericEntityParameter(IGenericEntityService, PageParameters, String, Class)} instead.
	 */
	@Deprecated
	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E extractGenericEntitySpecifiedParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, String parameterKey, Class<K> keyClass) {
		E entity;

		try {
			K entityId = parameters.get(parameterKey).to(keyClass);
			entity = entityService.getById(entityId);
		} catch (Exception e) {
			entity = null;
		}

		return entity;
	}

	/**
	 * @deprecated Use a {@link AbstractLinkFactory LinkFactory} and {@link ILinkDescriptor#fullUrl()} instead.
	 */
	@Deprecated
	public static String getUrl(Class<? extends Page> clazz, PageParameters parameters) {
		return RequestCycle.get().getUrlRenderer()
				.renderFullUrl(Url.parse(RequestCycle.get().urlFor(clazz, parameters).toString()));
	}
}
