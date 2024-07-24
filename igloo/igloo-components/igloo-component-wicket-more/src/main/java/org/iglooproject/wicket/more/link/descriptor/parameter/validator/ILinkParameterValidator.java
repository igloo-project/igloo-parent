package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * An interface for business-related objects aimed at validating the parameters of a link.
 *
 * <ul>
 *   <li>The parameters model will be validated before parameters injection and after parameters
 *       extraction.
 *   <li>The serialized form of the parameters ({@link PageParameters}) will be validated after
 *       parameters injection and before parameters extraction.
 * </ul>
 *
 * <p><strong>Note:</strong> if a model validation is to be performed, the validator must hold its
 * own reference to the model. It must therefore take care of {@link IDetachable#detach() detaching}
 * the model when required.
 */
public interface ILinkParameterValidator extends IDetachable {

  void validateModel(LinkParameterValidationErrorCollector collector);

  void validateSerialized(
      PageParameters parameters, LinkParameterValidationErrorCollector collector);

  /*
   * Overridden here just so that it appears at the end of the Eclipse class template when implementing.
   */
  @Override
  public void detach();
}
