package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;


public final class LinkParameterValidators {

	private LinkParameterValidators() { }

	/**
	 * @return True if the parameters are valid according to the validator, false otherwise.
	 * @throws NullPointerException if <code>parameters</code> or <code>validator</code> is null.
	 */
	public static boolean isValid(PageParameters parameters, ILinkParameterValidator validator) {
		Args.notNull(parameters, "parameters");
		Args.notNull(validator, "validator");
		
		LinkParameterValidationErrorCollector collector = new LinkParameterValidationErrorCollector();
		validator.validate(parameters, collector);

		Collection<ILinkParameterValidationError> errors = collector.getErrors();
		return errors.isEmpty();
	}
	
	/**
	 * Checks that parameters are valid according to a validator, throwing a {@link LinkParameterValidationException} if they are not.
	 * @throws LinkParameterValidationException if the parameters are not valid according to the validator.
	 * @throws NullPointerException if <code>parameters</code> or <code>validator</code> is null.
	 */
	public static void check(PageParameters parameters, ILinkParameterValidator validator) throws LinkParameterValidationException {
		Args.notNull(parameters, "parameters");
		Args.notNull(validator, "validator");
		
		LinkParameterValidationErrorCollector collector = new LinkParameterValidationErrorCollector();
		validator.validate(parameters, collector);
		
		Collection<ILinkParameterValidationError> errors = collector.getErrors();
		if ( ! errors.isEmpty() ) {
			throw new LinkParameterValidationException(parameters, errors);
		}
	}
	
	/**
	 * Creates a validator that performs the validation sequentially with each validator in the given order.
	 * <p>The resulting validator is {@link Serializable} if <code>validators</code> is Serializable.
	 * @param validators The validators to be chained
	 * @return A validator chaining the given validators. If empty, no validation will be performed and no {@link ILinkParameterValidationError error} will ever occur.
	 * @throws NullPointerException if <code>validators</code> is null.
	 */
	public static ILinkParameterValidator chain(Iterable<ILinkParameterValidator> validators) {
		Args.notNull(validators, "validators");
		return new ChainedParameterValidator(validators);
	}
	
	private static class ChainedParameterValidator implements ILinkParameterValidator {
		private static final long serialVersionUID = 7397495696437744067L;
		
		private final Iterable<ILinkParameterValidator> validators;
		
		public ChainedParameterValidator(Iterable<ILinkParameterValidator> validators) {
			super();
			this.validators = validators;
		}
		
		@Override
		public void validate(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
			for (ILinkParameterValidator validator : validators) {
				validator.validate(parameters, collector);
			}
		}
	}

}
