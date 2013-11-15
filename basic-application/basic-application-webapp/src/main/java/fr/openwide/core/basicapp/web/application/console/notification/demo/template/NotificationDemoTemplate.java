package fr.openwide.core.basicapp.web.application.console.notification.demo.template;

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
		return getFirstInRange(clazz, minId, maxId, Predicates.<E>alwaysTrue());
	}
	
	protected final <E extends GenericEntity<Long, ?>> IModel<E> getFirstInRange(Class<E> clazz, long minId, long maxId, Predicate<E> predicate) {
		for (long id = minId ; id < maxId  ; ++id) {
			E entity = entityService.getEntity(clazz, id);
			if (entity != null && predicate.apply(entity)) {
				return new GenericEntityModel<Long, E>(entity);
			}
		}
		
		LOGGER.error("A demo object is missing for demo " + Classes.simpleName(getClass()));
		Session.get().error(getString("console.notifications.demo.noDataAvailable"));
		
		throw ConsoleNotificationDemoIndexPage.linkDescriptor().newRestartResponseException();
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
