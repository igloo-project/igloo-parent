package org.igloo.storage.integration.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler.RedirectPolicy;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.igloo.storage.integration.wicket.exception.AbstractWicketStorageException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFichierInvalidatedException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFichierMissingException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFichierUnavailableException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prevent stack logging, use customized log level and conditionally use a custom unavailable page
 * for wicket storage exceptions.
 */
public class WicketStorageExceptionRequestCycleListener implements IRequestCycleListener {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(WicketStorageExceptionRequestCycleListener.class);

  private final Class<? extends IRequestablePage> unavailablePageClass;

  public WicketStorageExceptionRequestCycleListener(
      Class<? extends IRequestablePage> unavailablePageClass) {
    this.unavailablePageClass = unavailablePageClass;
  }

  @Override
  public IRequestHandler onException(RequestCycle cycle, Exception ex) {
    // only display message; do not display stack for compact exception
    // use adequate logging level
    if (ex instanceof WicketStorageFichierUnavailableException) {
      // Fichier is expectedly not here, INFO level
      LOGGER.info(ex.getMessage());
    } else if (ex instanceof WicketStorageFichierInvalidatedException) {
      // Fichier is expected to be removed, and link should not be available
      LOGGER.warn(ex.getMessage());
    } else if (ex instanceof WicketStorageFichierMissingException) {
      // Entity is missing
      LOGGER.warn(ex.getMessage());
    } else if (ex instanceof WicketStorageFileNotFoundException) {
      // Fichier's file is missing
      LOGGER.warn(ex.getMessage());
    }

    // provide a custom handler to skip default handling (as it triggers exception logging)
    if (unavailablePageClass != null && ex instanceof WicketStorageFichierUnavailableException) {
      // file is unavailable, use custom page is present
      return new RenderPageRequestHandler(
          new PageProvider(unavailablePageClass), RedirectPolicy.NEVER_REDIRECT);
    } else if (ex instanceof AbstractWicketStorageException) {
      // fallback: use standard error page
      return new RenderPageRequestHandler(
          new PageProvider(Application.get().getApplicationSettings().getInternalErrorPage()),
          RedirectPolicy.NEVER_REDIRECT);
    }

    // ignore other exceptions
    return null;
  }
}
