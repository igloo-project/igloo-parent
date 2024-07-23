package org.iglooproject.wicket.more.link.descriptor.parameter.extractor;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.slf4j.Logger;

/**
 * An {@link ILinkParametersExtractor} that also allows for "safe" extraction, redirecting to a
 * given page if an {@link Exception} is caught during the extraction.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 */
public interface IPageLinkParametersExtractor extends ILinkParametersExtractor {

  /**
   * Attempts to extract the page parameters, {@link #newRestartResponseException() throwing a
   * RestartResponseException} with the provided fallback link if any {@link Exception} is caught.
   *
   * <p>If an exception is caught, it is {@link Logger logged} at error level on the {@link
   * IPageLinkParametersExtractor} class logger.
   *
   * @see #extract(PageParameters)
   */
  void extractSafely(PageParameters parameters, IPageLinkGenerator fallbackLink)
      throws RestartResponseException;

  /**
   * Attempts to extract the page parameters, {@link #newRestartResponseException() throwing a
   * RestartResponseException} with the provided fallback link if any {@link Exception} is caught.
   * The provided error message is added to the session.
   *
   * <p>If an exception is caught, it is {@link Logger logged} at error level on the {@link
   * IPageLinkParametersExtractor} class logger.
   *
   * @see #extract(PageParameters)
   */
  void extractSafely(
      PageParameters parameters, IPageLinkGenerator fallbackLink, String errorMessage)
      throws RestartResponseException;
}
