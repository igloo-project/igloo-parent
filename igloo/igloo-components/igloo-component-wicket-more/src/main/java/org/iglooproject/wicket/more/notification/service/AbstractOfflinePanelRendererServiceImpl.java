package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.beans.factory.annotation.Autowired;

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
	
	protected final String renderComponent(final SerializableSupplier2<Component> componentSupplier, Locale locale) {
		return renderComponent(componentSupplier, locale, null);
	}
	
	protected final String renderPage(final SerializableSupplier2<Page> pageSupplier, Locale locale) {
		return renderPage(pageSupplier, locale, null);
	}
	
	/**
	 * @param componentSupplier A supplier for the component to be rendered.
	 * @param locale The locale to use when rendering.
	 * @param variation A string identifier that will be passed to {@link #postProcessHtml(Component, Locale, String, String)}.
	 * @return The component returned by <code>componenentSupplier</code>, rendered in HTML with the given <code>locale</code>
	 */
	protected String renderComponent(final SerializableSupplier2<Component> componentSupplier, final Locale locale, final String variation) {
		Args.notNull(componentSupplier, "componentSupplier");
		try (ITearDownHandle handle = wicketContextProvider.context(locale).open()) {
			Component component = componentSupplier.get();
			Args.notNull(component, "component");
			
			String htmlBody = ComponentRenderer.renderComponent(component).toString();
			
			return postProcessHtml(component, locale, variation, htmlBody);
		}
	}
	
	/**
	 * @param pageSupplier A supplier for the page to be rendered.
	 * @param locale The locale to use when rendering.
	 * @param variation A string identifier that will be passed to {@link #postProcessHtml(Component, Locale, String, String)}.
	 * @return The component returned by <code>pageSupplier</code>, rendered in HTML with the given <code>locale</code>
	 */
	protected String renderPage(final SerializableSupplier2<? extends Page> pageSupplier, final Locale locale, final String variation) {
		Args.notNull(pageSupplier, "pageSupplier");
		try (ITearDownHandle handle = wicketContextProvider.context(locale).open()) {
			Page page = pageSupplier.get();
			String htmlBody = renderPage(new PageProvider(pageSupplier.get())).toString();
			
			return postProcessHtml(page, locale, variation, htmlBody);
		}
	}

	/**
	 * Alternative renderPage for {@link ComponentRenderer#renderPage(java.util.function.Supplier)}.
	 * 
	 * We need to mimic mix renderComponent behavior with renderPage arguments; renderComponent overrides only
	 * response, as we already perform all the other initialization.
	 * 
	 * This method is made from {@link ComponentRenderer#renderComponent(java.util.function.Supplier)} for context
	 * initialization code and {@link ComponentRenderer#renderPage(java.util.function.Supplier)} for rendering
	 */
	private static CharSequence renderPage(final PageProvider pageProvider) {
		RequestCycle requestCycle = RequestCycle.get();
		final Response originalResponse = requestCycle.getResponse();
		BufferedWebResponse tempResponse = new BufferedWebResponse(null);
		
		try {
			requestCycle.setResponse(tempResponse);
			pageProvider.getPageInstance().renderPage();
		} finally {
			requestCycle.setResponse(originalResponse);
		}
		
		return tempResponse.getText();
	}
	
	protected abstract String postProcessHtml(Component component, Locale locale, String variation, String htmlBodyToProcess);
	
	protected IRendererService getRendererService() {
		return rendererService;
	}
}