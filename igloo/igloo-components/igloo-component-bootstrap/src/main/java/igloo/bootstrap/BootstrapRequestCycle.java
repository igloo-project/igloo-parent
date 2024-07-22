package igloo.bootstrap;

import igloo.wicket.offline.OfflineComponentClassMetadataKey;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestHandlerDelegate;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapRequestCycle {

  private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapRequestCycle.class);

  private BootstrapRequestCycle() {}

  @SuppressWarnings("serial")
  public static final MetaDataKey<BootstrapVersion> VERSION_KEY =
      new MetaDataKey<BootstrapVersion>() {};

  /**
   * Get bootstrap version from request cycle cache, or resolve it from currently processed page.
   */
  private static BootstrapVersion getVersion() {
    return Optional.ofNullable(RequestCycle.get().getMetaData(VERSION_KEY))
        .orElse(resolveVersion());
  }

  /** Resolve version from page, cache on request cycle and return. */
  private static BootstrapVersion resolveVersion() {
    BootstrapVersion version = findVersion();
    RequestCycle.get().setMetaData(VERSION_KEY, version);
    return version;
  }

  /** Resolve bootstrap version from currently processed page. */
  private static BootstrapVersion findVersion() {
    IRequestHandler requestHandler = RequestCycle.get().getActiveRequestHandler();
    final Class<?> componentClass;
    if (requestHandler != null) {
      componentClass = lookupPageFromPageRequestHandler(requestHandler);
    } else if (RequestCycle.get().getMetaData(OfflineComponentClassMetadataKey.INSTANCE) != null) {
      // beware that for notifications, it may be a Component and not a page !
      componentClass = RequestCycle.get().getMetaData(OfflineComponentClassMetadataKey.INSTANCE);
    } else {
      throw new IllegalStateException(
          "No IPageRequestHandler and no OfflinePageMetadataKey metadata found; Page class retrieval fails.");
    }
    boolean bootstrap4 = IBootstrap4Component.class.isAssignableFrom(componentClass);
    boolean bootstrap5 = IBootstrap5Component.class.isAssignableFrom(componentClass);
    if (bootstrap4 && bootstrap5) {
      LOGGER.warn(
          "Both bootstrap 4 and bootstrap 5 enabled on {}, fallback to default {}; please use only one version",
          componentClass.getName(),
          BootstrapVersion.BOOTSTRAP_5.name());
    }
    if (bootstrap5) {
      return BootstrapVersion.BOOTSTRAP_5;
    } else if (bootstrap4) {
      return BootstrapVersion.BOOTSTRAP_4;
    } else {
      return ((IBootstrapApplication) Application.get()).getBootstrapSettings().getDefaultVersion();
    }
  }

  private static Class<?> lookupPageFromPageRequestHandler(
      @Nonnull IRequestHandler requestHandler) {
    if (requestHandler instanceof IRequestHandlerDelegate) {
      requestHandler = ((IRequestHandlerDelegate) requestHandler).getDelegateHandler();
    }
    if (!(requestHandler instanceof IPageRequestHandler)) {
      throw new IllegalStateException(
          String.format(
              "requestHandler not a IPageRequestHandler; version cannot be resolved (%s)",
              requestHandler.getClass().getName()));
    } else {
      IPageRequestHandler pageRequestHandler = (IPageRequestHandler) requestHandler;
      return pageRequestHandler.getPageClass();
    }
  }

  public static IBootstrapProvider getSettings() {
    switch (getVersion()) {
      case BOOTSTRAP_4:
        return ((IBootstrapApplication) Application.get())
            .getBootstrapSettings()
            .getBootstrap4Provider();
      case BOOTSTRAP_5:
        return ((IBootstrapApplication) Application.get())
            .getBootstrapSettings()
            .getBootstrap5Provider();
      default:
        throw new IllegalStateException();
    }
  }

  public static String getVariation() {
    switch (getVersion()) {
      case BOOTSTRAP_4:
        return "bs4";
      case BOOTSTRAP_5:
        return null;
      default:
        throw new IllegalStateException();
    }
  }
}
