package igloo.bootstrap;

import java.util.Optional;
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

  public static final MetaDataKey<BootstrapVersion> VERSION_KEY =
      new MetaDataKey<BootstrapVersion>() {};

  /**
   * Get bootstrap version from request cycle cache, or resolve it from currently processed page.
   */
  public static BootstrapVersion getVersion() {
    IRequestHandler requestHandler = RequestCycle.get().getActiveRequestHandler();

    if (requestHandler instanceof IRequestHandlerDelegate requestHandlerDelegate) {
      requestHandler = requestHandlerDelegate.getDelegateHandler();
    }

    if (requestHandler.getClass().getSimpleName().contains("WebSocketRequestHandler")
        || requestHandler.getClass().getSimpleName().contains("WebSocketMessageBroadcastHandler")) {
      LOGGER.error("IRequestHandler: {}", requestHandler);
    }

    return Optional.ofNullable(RequestCycle.get().getMetaData(VERSION_KEY))
        .orElseGet(BootstrapRequestCycle::resolveVersion);
  }

  /** Set bootstrap version from component class */
  public static BootstrapVersion setVersion(Class<?> componentClass) {
    BootstrapVersion version = findVersion(componentClass);
    RequestCycle.get().setMetaData(VERSION_KEY, version);
    return version;
  }

  /** Clear bootstrap version */
  public static void clearVersion() {
    RequestCycle.get().setMetaData(VERSION_KEY, null);
  }

  /** Resolve version from page, cache on request cycle and return. */
  private static BootstrapVersion resolveVersion() {
    return setVersion(lookupPageClassFromPageRequestHandler());
  }

  /** Resolve bootstrap version from currently processed page. */
  private static Class<?> lookupPageClassFromPageRequestHandler() {
    IRequestHandler requestHandler = RequestCycle.get().getActiveRequestHandler();

    if (requestHandler == null) {
      throw new IllegalStateException("No IRequestHandler; version cannot be resolved");
    }

    if (requestHandler instanceof IRequestHandlerDelegate requestHandlerDelegate) {
      requestHandler = requestHandlerDelegate.getDelegateHandler();
    }

    LOGGER.error("IRequestHandler: {}", requestHandler);

    if (!(requestHandler instanceof IPageRequestHandler pageRequestHandler)) {
      throw new IllegalStateException(
          String.format(
              "IRequestHandler not a IPageRequestHandler; version cannot be resolved (%s)",
              requestHandler.getClass().getName()));
    }

    return pageRequestHandler.getPageClass();
  }

  private static BootstrapVersion findVersion(Class<?> componentClass) {
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
