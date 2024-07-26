package org.iglooproject.wicket.more.notification.service;

import igloo.wicket.offline.IOfflineComponentProvider;
import igloo.wicket.offline.OfflineComponentClassMetadataKey;
import java.util.Locale;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a common utility for "offline" page generation, for notifications or queued pdf rendering
 * for example.
 *
 * <p>Existing implementations are:
 *
 * <ul>
 *   <li>Extend {@link AbstractNotificationContentDescriptorFactory}, if you need to render e-mail
 *       notifications.
 *   <li>Extend {@link AbstractWicketRendererServiceImpl}, if you only need to render
 *       components/strings
 * </ul>
 */
abstract class AbstractOfflinePanelRendererServiceImpl {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractOfflinePanelRendererServiceImpl.class);

  private IWicketContextProvider wicketContextProvider;

  @Autowired private IRendererService rendererService;

  public AbstractOfflinePanelRendererServiceImpl(IWicketContextProvider wicketContextProvider) {
    this.wicketContextProvider = wicketContextProvider;
  }

  protected IWicketContextProvider getWicketContextProvider() {
    return wicketContextProvider;
  }

  protected final String renderComponent(
      final IOfflineComponentProvider<? extends Component> offlineComponent, Locale locale) {
    return renderComponent(offlineComponent, locale, null);
  }

  protected final String renderPage(
      final IOfflineComponentProvider<? extends Page> offlineComponent, Locale locale) {
    return renderPage(offlineComponent, locale, null);
  }

  /**
   * @param offlineComponent A supplier for the component to be rendered.
   * @param locale The locale to use when rendering.
   * @param variation A string identifier that will be passed to {@link #postProcessHtml(Component,
   *     Locale, String, String)}.
   * @return The component returned by <code>componenentSupplier</code>, rendered in HTML with the
   *     given <code>locale</code>
   */
  protected String renderComponent(
      final IOfflineComponentProvider<? extends Component> offlineComponent,
      final Locale locale,
      final String variation) {
    Args.notNull(offlineComponent, "offlineComponent");
    try (ITearDownHandle handle = context(locale).open()) {
      RequestCycle.get()
          .setMetaData(
              OfflineComponentClassMetadataKey.INSTANCE, offlineComponent.getComponentClass());

      Component component = offlineComponent.getComponent();
      Args.notNull(component, "component");
      String htmlBody = ComponentRenderer.renderComponent(component).toString();

      return postProcessHtml(component, locale, variation, htmlBody);
    }
  }

  /**
   * @param offlineComponent A supplier for the page to be rendered.
   * @param locale The locale to use when rendering.
   * @param variation A string identifier that will be passed to {@link #postProcessHtml(Component,
   *     Locale, String, String)}.
   * @return The component returned by <code>pageSupplier</code>, rendered in HTML with the given
   *     <code>locale</code>
   */
  protected String renderPage(
      final IOfflineComponentProvider<? extends Page> offlineComponent,
      final Locale locale,
      final String variation) {
    Args.notNull(offlineComponent, "offlineComponent");
    try (ITearDownHandle handle = context(locale).open()) {
      Pair<Page, CharSequence> result = renderPage(offlineComponent);
      if (!offlineComponent.getComponentClass().isInstance(result.getLeft())) {
        LOGGER.warn(
            "Component class {} does not match advertised class {}",
            result.getLeft().getClass().getName(),
            offlineComponent.getComponentClass().getName());
      }

      return postProcessHtml(result.getLeft(), locale, variation, result.getRight().toString());
    }
  }

  /**
   * Override this method if you need to customize your rendering context. A common usage is to bind
   * context with a customized {@link WebApplication}.
   */
  protected IExecutionContext context(final Locale locale) {
    return wicketContextProvider.context(locale);
  }

  /**
   * Alternative renderPage for {@link ComponentRenderer#renderPage(java.util.function.Supplier)}.
   *
   * <p>We need to mimic mix renderComponent behavior with renderPage arguments; renderComponent
   * overrides only response, as we already perform all the other initialization.
   *
   * <p>This method is made from {@link
   * ComponentRenderer#renderComponent(java.util.function.Supplier)} for context initialization code
   * and {@link ComponentRenderer#renderPage(java.util.function.Supplier)} for rendering
   */
  private static Pair<Page, CharSequence> renderPage(
      final IOfflineComponentProvider<? extends Page> offlineComponent) {
    RequestCycle requestCycle = RequestCycle.get();
    final Response originalResponse = requestCycle.getResponse();
    BufferedWebResponse tempResponse = new BufferedWebResponse(null);

    try {
      requestCycle.setResponse(tempResponse);
      requestCycle.setMetaData(
          OfflineComponentClassMetadataKey.INSTANCE, offlineComponent.getComponentClass());
      Page component = offlineComponent.getComponent();
      if (!offlineComponent.getComponentClass().isInstance(component)) {
        LOGGER.warn(
            "Component class {} does not match advertised class {}",
            component.getClass().getName(),
            offlineComponent.getComponentClass().getName());
      }
      component.renderPage();
      return Pair.of(component, tempResponse.getText());
    } finally {
      requestCycle.setResponse(originalResponse);
    }
  }

  protected abstract String postProcessHtml(
      Component component, Locale locale, String variation, String htmlBodyToProcess);

  protected IRendererService getRendererService() {
    return rendererService;
  }
}
