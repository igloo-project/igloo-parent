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
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public final class LinkUtils {

	public static final String ID_PARAMETER = "id";

	private LinkUtils() {
	}

	public static Link<QueuedTaskHolder> getQueuedTaskHolderLink(String id,
			IModel<? extends QueuedTaskHolder> queuedTaskHolderModel) {
		return new BookmarkablePageLink<QueuedTaskHolder>(id, ConsoleMaintenanceTaskDescriptionPage.class,
				getGenericEntityIdPageParameters(queuedTaskHolderModel));
	}

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

	public static PageParameters getGenericEntityIdPageParameters(GenericEntity<?, ?> genericEntity) {
		PageParameters parameters = new PageParameters();
		if (genericEntity != null) {
			parameters.add(ID_PARAMETER, genericEntity.getId());
		}
		return parameters;
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> IModel<E> extractGenericEntityModelParameter(
			IGenericEntityService<K, E> entityService, PageParameters parameters, Class<K> keyClass) {
		return new GenericEntityModel<K, E>(extractGenericEntitySpecifiedParameter(entityService, parameters,
				LinkUtils.ID_PARAMETER, keyClass));
	}

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

	public static String getUrl(Class<? extends Page> clazz, PageParameters parameters) {
		return RequestCycle.get().getUrlRenderer()
				.renderFullUrl(Url.parse(RequestCycle.get().urlFor(clazz, parameters).toString()));
	}
}
