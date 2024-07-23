package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;

/**
 * A object implementing both {@link ILinkGenerator} and {@link ILinkParametersExtractor}.
 *
 * <p>Object implementing this interface, and its sub-interfaces ({@link IResourceLinkDescriptor},
 * {@link IPageLinkDescriptor}), can be instantiated using the {@link LinkDescriptorBuilder}.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 *
 * @see ILinkGenerator
 * @see ILinkParametersExtractor
 */
public interface ILinkDescriptor extends ILinkParametersExtractor, ILinkGenerator, IDetachable {

  /**
   * Executes validation checks on the underlying models, assuming they already have been populated.
   *
   * @throws LinkParameterModelValidationException if the validation of the parameters model
   *     returned an error
   */
  void checkModels() throws LinkParameterModelValidationException;
}
