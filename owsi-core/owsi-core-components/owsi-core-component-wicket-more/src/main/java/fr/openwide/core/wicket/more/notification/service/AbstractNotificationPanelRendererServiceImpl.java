package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Component;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Args;

import com.google.common.base.Supplier;

import fr.openwide.core.context.IContextualService;

/**
 * @deprecated You may instead :<ul>
 *   <li>Extend {@link AbstractNotificationContentDescriptorFactory}, if you need to render e-mail notifications.
 *   <li>Extend {@link AbstractWicketRendererServiceImpl}, if you only need to render components/strings
 * </ul>
 */
@Deprecated
public abstract class AbstractNotificationPanelRendererServiceImpl
		implements IContextualService {
	
	private IWicketContextExecutor wicketExecutor;

	public AbstractNotificationPanelRendererServiceImpl(IWicketContextExecutor wicketExecutor) {
		this.wicketExecutor = wicketExecutor;
	}
	
	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return wicketExecutor.runWithContext(callable);
	}
	
	/**
	 * @deprecated Use {@link #renderComponent(Supplier, Locale, String)} instead. Caught exceptions (if any) should be handled by the supplier.
	 */
	@Deprecated
	protected final String renderComponent(final Callable<Component> componentTask, Locale locale) {
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
		try {
			return wicketExecutor.runWithContext(
					new Callable<String>() {
						@Override
						public String call() {
							Component component = componentSupplier.get();
							Args.notNull(component, "component");
							
							String htmlBody = ComponentRenderer.renderComponent(component).toString();
							
							return postProcessHtml(component, locale, variation, htmlBody);
						}
					},
					locale
			);
		} catch (Exception e) {
			throw new IllegalStateException("Error rendering a panel with locale '" + locale + "'", e);
		}
	}

	protected String postProcessHtml(Component component, Locale locale, String variation, String htmlBodyToProcess) {
		return htmlBodyToProcess;
	}
	
	protected String renderString(final String messageKey, Locale locale, final Object parameter, final Object ... positionalParameters) {
		try {
			return wicketExecutor.runWithContext(
					new Callable<String>() {
						@Override
						public String call() {
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
								
								String result = new StringResourceModel(messageKey).setModel(modelParameter).setParameters(positionalParameters).getObject();
								return result;
						}
					},
					locale
			);
		} catch (Exception e) {
			throw new IllegalStateException("Error rendering string for key '" + messageKey + "' and locale '" + locale + "'", e);
		}
	}
}