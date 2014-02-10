package fr.openwide.core.basicapp.web.application.console.notification.demo.template;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.console.notification.demo.page.ConsoleNotificationDemoIndexPage;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public abstract class NotificationDemoTemplate extends WebPage {
	
	private static final long serialVersionUID = -1929048481327622623L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDemoTemplate.class);
	
	@SpringBean
	protected IEntityService entityService;
	
	public NotificationDemoTemplate(PageParameters parameters) {
		super(parameters);
		
		MarkupContainer htmlRootElement = new TransparentWebMarkupContainer("htmlRootElement");
		htmlRootElement.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage()));
		add(htmlRootElement);
		
		add(new Label("headPageTitle", new ResourceModel("console.notifications")));
		
		add(buildNotificationPanel("htmlPanel", parameters));
	}
	
	protected abstract Component buildNotificationPanel(String wicketId, PageParameters parameters);

	protected final <E extends GenericEntity<Long, ?>> IModel<E> getFirstInRange(Class<E> clazz, long minId, long maxId) {
		return getFirstInRange(clazz, Range.closed(minId, maxId));
	}

	protected final <E extends GenericEntity<Long, ?>> IModel<E> getFirstInRange(Class<E> clazz, Range<Long> range) {
		return getFirstInRange(clazz, range, Predicates.<E>alwaysTrue());
	}
	
	protected final <E extends GenericEntity<Long, ?>> IModel<E> getFirstInRange(Class<E> clazz, Range<Long> range, Predicate<E> predicate) {
		IModel<E> entityModel = getFirstWithId(clazz, ContiguousSet.create(range, DiscreteDomain.longs()), predicate);
		
		if (entityModel != null) {
			return entityModel;
		} else {
			LOGGER.error("A demo object is missing for demo " + Classes.simpleName(getClass()));
			Session.get().error(getString("console.notifications.demo.noDataAvailable"));
			
			throw ConsoleNotificationDemoIndexPage.linkDescriptor().newRestartResponseException();
		}
	}
	
	protected final <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>> IModel<E> getFirstWithId(Class<E> clazz, Collection<K> ids, Predicate<E> predicate) {
		for (K id : ids) {
			E entity = entityService.getEntity(clazz, id);
			if (entity != null && predicate.apply(entity)) {
				return GenericEntityModel.of(entity);
			}
		}
		
		return null;
	}
	
	protected final <E extends GenericEntity<Long, ?>> IModel<E> getNonNullModel(E object) {
		if (object == null) {
			LOGGER.error("A demo object is missing for demo " + Classes.simpleName(getClass()));
			Session.get().error(getString("console.notifications.demo.noDataAvailable"));
			
			throw ConsoleNotificationDemoIndexPage.linkDescriptor().newRestartResponseException();
		} else {
			return new GenericEntityModel<Long, E>(object);
		}
	}
}
