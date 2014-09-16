package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;

import fr.openwide.core.jpa.security.service.ISecurityService;

@SuppressWarnings("deprecation")
public abstract class AbstractWicketRendererServiceImpl extends AbstractNotificationPanelRendererServiceImpl {
	
	@Autowired
	private ISecurityService securityService;

	@Override
	protected abstract String getApplicationName();
	
	@Override
	protected String renderComponent(final Supplier<Component> componentSupplier, final Locale locale) {
		return securityService.runAsSystem(
			new Callable<String>() {
				@Override
				public String call() throws Exception {
					return AbstractWicketRendererServiceImpl.super.renderComponent(componentSupplier, locale);
				}
			}
		);
	}
	
	@Override
	protected String renderString(final String messageKey, final Locale locale, final Object parameter, final Object... positionalParameters) {
		return securityService.runAsSystem(
			new Callable<String>() {
				@Override
				public String call() throws Exception {
					return AbstractWicketRendererServiceImpl.super.renderString(messageKey, locale, parameter, positionalParameters);
				}
			}
		);
	}
}
