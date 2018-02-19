package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;

/**
 * <p>This is a common utility for "offline" page generation, for notifications or queued pdf rendering
 * for example.</p> 
 * 
 * <p>
 * Existing implementations are:
 * <ul>
 *   <li>Extend {@link AbstractNotificationContentDescriptorFactory}, if you need to render e-mail notifications.
 *   <li>Extend {@link AbstractWicketRendererServiceImpl}, if you only need to render components/strings
 * </ul>
 * </p>
 */
abstract class AbstractOfflinePanelRendererServiceImpl {
	
	private IWicketContextProvider wicketContextProvider;
	
	@Autowired
	private IRendererService rendererService;

	public AbstractOfflinePanelRendererServiceImpl(IWicketContextProvider wicketContextProvider) {
		this.wicketContextProvider = wicketContextProvider;
	}
	
	protected IWicketContextProvider getWicketContextProvider() {
		return wicketContextProvider;
	}
	
	protected final String renderComponent(final Supplier<Component> componentSupplier, Locale locale) {
		return renderComponent(componentSupplier, locale, null);
	}
	
	/**
	 * @param componentSupplier A supplier for the component to be rendered.
	 * @param locale The locale to use when rendering.
	 * @param variation A string identifier that will be passed to {@link #postProcessHtml(Component, Locale, String, String)}.
	 * @return The component returned by <code>componenentSupplier</code>, rendered in HTML with the given <code>locale</code>
	 */
	protected String renderComponent(final Supplier<Component> componentSupplier, final Locale locale, final String variation) {
		Args.notNull(componentSupplier, "componentTask");
		try (ITearDownHandle handle = wicketContextProvider.context(locale).open()) {
			Component component = componentSupplier.get();
			Args.notNull(component, "component");
			
			String htmlBody = ComponentRenderer.renderComponent(component).toString();
			
			return postProcessHtml(component, locale, variation, htmlBody);
		}
	}
	
	protected abstract String postProcessHtml(Component component, Locale locale, String variation, String htmlBodyToProcess);
	
	protected IRendererService getRendererService() {
		return rendererService;
	}
}