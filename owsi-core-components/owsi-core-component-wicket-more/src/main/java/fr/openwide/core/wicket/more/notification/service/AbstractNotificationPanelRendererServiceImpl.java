package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Args;

import com.google.common.base.Supplier;

/**
 * @deprecated You may instead :<ul>
 *   <li>Extend {@link AbstractNotificationContentDescriptorFactory}, if you need to render e-mail notifications.
 *   <li>Extend {@link AbstractWicketRendererServiceImpl}, if you only need to render components/strings
 * </ul>
 */
@Deprecated
public abstract class AbstractNotificationPanelRendererServiceImpl extends AbstractBackgroundWicketThreadContextBuilder {
	
	/**
	 * @deprecated Use {@link #renderComponent(Supplier, Locale)} instead. Caught exceptions (if any) should be handled by the supplier.
	 */
	@Deprecated
	protected String renderComponent(final Callable<Component> componentTask, Locale locale) {
		return renderComponent(
				new Supplier<Component>() {
					@Override
					public Component get() {
						try {
							return componentTask.call();
						} catch (Exception e) {
							throw new RuntimeException(e); // Do wrap
						}
					}
				},
				locale
		);
	}
	
	protected String renderComponent(Supplier<Component> componentSupplier, Locale locale) {
		Args.notNull(componentSupplier, "componentTask");
		
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(getApplicationName());
			
			Session session = Session.get();
			
			Locale oldLocale = session.getLocale();
			if (locale != null) {
				session.setLocale(configurer.toAvailableLocale(locale));
			}
			
			Component component = componentSupplier.get();
			Args.notNull(component, "component");
			
			String panel = ComponentRenderer.renderComponent(component).toString();
			
			session.setLocale(oldLocale);
			
			return panel;
		} finally {
			if (requestCycleStatus != null) {
				detachRequestCycleIfNeeded(requestCycleStatus);
			}
		}
	}
	
	protected String renderString(String messageKey, Locale locale, final Object parameter, Object ... positionalParameters) {
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(getApplicationName());
			
			Session session = Session.get();
			
			Locale oldLocale = session.getLocale();
			if (locale != null) {
				session.setLocale(configurer.toAvailableLocale(locale));
			}
			
			IModel<?> modelParameter;
			if (parameter instanceof IModel) {
				modelParameter = (IModel<?>) parameter;
			} else {
				modelParameter = new AbstractReadOnlyModel<Object>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Object getObject() {
						return parameter;
					}
				};
			}
			
			String result = new StringResourceModel(messageKey, null, modelParameter, (Object[]) positionalParameters).getObject();
			
			session.setLocale(oldLocale);
			
			return result;
		} catch (Exception e) {
			throw new IllegalStateException("Error rendering string for key '" + messageKey + "' and locale '" + locale + "'", e);
		} finally {
			if (requestCycleStatus != null) {
				detachRequestCycleIfNeeded(requestCycleStatus);
			}
		}
	}
}