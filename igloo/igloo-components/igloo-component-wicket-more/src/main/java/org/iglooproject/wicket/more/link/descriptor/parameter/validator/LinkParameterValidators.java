package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Collection;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

public final class LinkParameterValidators {

  private LinkParameterValidators() {}

  /**
   * @return True if the parameters model is valid according to the validator, false otherwise.
   * @throws NullPointerException if <code>validator</code> is null.
   */
  public static boolean isModelValid(ILinkParameterValidator validator) {
    Args.notNull(validator, "validator");

    LinkParameterValidationErrorCollector collector = new LinkParameterValidationErrorCollector();
    validator.validateModel(collector);

    Collection<ILinkParameterValidationErrorDescription> errors = collector.getErrors();
    return errors.isEmpty();
  }

  /**
   * Checks that parameters model is valid according to a validator, throwing a {@link
   * LinkParameterModelValidationException} if it is not.
   *
   * @throws LinkParameterModelValidationException if the parameters model is invalid according to
   *     the validator.
   * @throws NullPointerException if <code>validator</code> is null.
   */
  public static void checkModel(ILinkParameterValidator validator)
      throws LinkParameterModelValidationException {
    Args.notNull(validator, "validator");

    LinkParameterValidationErrorCollector collector = new LinkParameterValidationErrorCollector();
    validator.validateModel(collector);

    Collection<ILinkParameterValidationErrorDescription> errors = collector.getErrors();
    if (!errors.isEmpty()) {
      throw new LinkParameterModelValidationException(errors);
    }
  }

  /**
   * @return True if the serialized form of the parameters is valid according to the validator,
   *     false otherwise.
   * @throws NullPointerException if <code>parameters</code> or <code>validator</code> is null.
   */
  public static boolean isSerializedValid(
      PageParameters parameters, ILinkParameterValidator validator) {
    Args.notNull(parameters, "parameters");
    Args.notNull(validator, "validator");

    LinkParameterValidationErrorCollector collector = new LinkParameterValidationErrorCollector();
    validator.validateSerialized(parameters, collector);

    Collection<ILinkParameterValidationErrorDescription> errors = collector.getErrors();
    return errors.isEmpty();
  }

  /**
   * Checks that the serialized form of the parameters is valid according to a validator, throwing a
   * {@link LinkParameterSerializedFormValidationException} if it is not.
   *
   * @throws LinkParameterSerializedFormValidationException if the serialized form of the parameters
   *     is invalid according to the validator.
   * @throws NullPointerException if <code>parameters</code> or <code>validator</code> is null.
   */
  public static void checkSerialized(PageParameters parameters, ILinkParameterValidator validator)
      throws LinkParameterSerializedFormValidationException {
    Args.notNull(parameters, "parameters");
    Args.notNull(validator, "validator");

    LinkParameterValidationErrorCollector collector = new LinkParameterValidationErrorCollector();
    validator.validateSerialized(parameters, collector);

    Collection<ILinkParameterValidationErrorDescription> errors = collector.getErrors();
    if (!errors.isEmpty()) {
      throw new LinkParameterSerializedFormValidationException(parameters, errors);
    }
  }

  /**
   * Creates a validator that performs the validation sequentially with each validator in the given
   * order.
   *
   * <p>The resulting validator is {@link Serializable} if <code>validators</code> is Serializable.
   *
   * @param validators The validators to be chained
   * @return A validator chaining the given validators. If empty, no validation will be performed
   *     and no {@link ILinkParameterValidationErrorDescription error} will ever be reported.
   * @throws NullPointerException if <code>validators</code> is null.
   */
  public static ILinkParameterValidator chain(
      Iterable<? extends ILinkParameterValidator> validators) {
    Args.notNull(validators, "validators");
    return new ChainedParameterValidator(validators);
  }

  private static class ChainedParameterValidator implements ILinkParameterValidator {
    private static final long serialVersionUID = 1L;

    private final Iterable<? extends ILinkParameterValidator> validators;

    public ChainedParameterValidator(Iterable<? extends ILinkParameterValidator> validators) {
      super();
      this.validators = ImmutableList.copyOf(validators);
    }

    @Override
    public void validateSerialized(
        PageParameters parameters, LinkParameterValidationErrorCollector collector) {
      for (ILinkParameterValidator validator : validators) {
        validator.validateSerialized(parameters, collector);
      }
    }

    @Override
    public void validateModel(LinkParameterValidationErrorCollector collector) {
      for (ILinkParameterValidator validator : validators) {
        validator.validateModel(collector);
      }
    }

    @Override
    public void detach() {
      for (ILinkParameterValidator validator : validators) {
        validator.detach();
      }
    }
  }
}
