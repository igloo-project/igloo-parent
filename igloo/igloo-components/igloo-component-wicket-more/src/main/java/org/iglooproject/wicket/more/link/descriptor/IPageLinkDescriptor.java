package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.Page;
import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.IPageLinkParametersExtractor;
import org.slf4j.Logger;

/**
 * An {@link ILinkDescriptor} pointing to a {@link Page}.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 *
 * @see ILinkDescriptor
 * @see IPageLinkGenerator
 * @see IPageLinkParametersExtractor
 */
public interface IPageLinkDescriptor
    extends ILinkDescriptor, IPageLinkGenerator, IPageLinkParametersExtractor, IDetachable {

  /**
   * Attempts to validate the underlying models, {@link #newRestartResponseException() throwing a
   * RestartResponseException} with the provided fallback link if any {@link Exception} is caught.
   *
   * <p>If an exception is caught, it is {@link Logger logged} at error level on the {@link
   * IPageLinkParametersExtractor} class logger.
   *
   * @see #checkModels()
   */
  void checkModelsSafely(IPageLinkGenerator fallbackLink);

  /**
   * Attempts to validate the underlying models, {@link #newRestartResponseException() throwing a
   * RestartResponseException} with the provided fallback link if any {@link Exception} is caught.
   * The provided error message is added to the session.
   *
   * <p>If an exception is caught, it is {@link Logger logged} at error level on the {@link
   * IPageLinkParametersExtractor} class logger.
   *
   * @see #checkModels()
   */
  void checkModelsSafely(IPageLinkGenerator fallbackLink, String errorMessage);
}
