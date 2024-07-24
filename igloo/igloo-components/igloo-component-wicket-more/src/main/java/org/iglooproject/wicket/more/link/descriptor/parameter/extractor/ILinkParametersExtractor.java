package org.iglooproject.wicket.more.link.descriptor.parameter.extractor;

import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;

/**
 * An object mapped to {@link IModel models}, that allows for simple {@link PageParameters link
 * parameters} extraction to these models.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 */
public interface ILinkParametersExtractor extends IDetachable {

  /**
   * Extracts the given parameters to the underlying models.
   *
   * <p>The underlying model must handle the {@link IModel#setObject(Object)} operation, even if
   * they are non-wrapped-yet {@link IComponentAssignedModel}. Otherwise, the behavior is undefined.
   *
   * @throws LinkParameterSerializedFormValidationException if the validation of the parameters
   *     serialized form returned an error
   * @throws LinkParameterExtractionRuntimeException if an error occurred during parameters
   *     extraction (most probably during the conversion)
   * @throws LinkParameterModelValidationException if the validation of the parameters model
   *     returned an error
   */
  void extract(PageParameters parameters)
      throws LinkParameterSerializedFormValidationException,
          LinkParameterExtractionRuntimeException,
          LinkParameterModelValidationException;
}
