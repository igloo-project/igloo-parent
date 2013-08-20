package fr.openwide.core.wicket.more.link.descriptor.validator;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;


public final class ParameterValidators {

	private ParameterValidators() { }

	/**
	 * @return True if the parameters are valid according to the validator, false otherwise.
	 * @throws NullPointerException if <code>parameters</code> or <code>validator</code> is null.
	 */
	public static boolean isValid(PageParameters parameters, IParameterValidator validator) {
		Args.notNull(parameters, "parameters");
		Args.notNull(validator, "validator");
		
		ParameterValidationErrorCollector collector = new ParameterValidationErrorCollector();
		validator.validate(parameters, collector);

		Collection<IParameterValidationError> errors = collector.getErrors();
		return errors.isEmpty();
	}
	
	/**
	 * Checks that parameters are valid according to a validator, throwing a {@link ParameterValidationException} if they are not.
	 * @throws ParameterValidationException if the parameters are not valid according to the validator.
	 * @throws NullPointerException if <code>parameters</code> or <code>validator</code> is null.
	 */
	public static void check(PageParameters parameters, IParameterValidator validator) throws ParameterValidationException {
		Args.notNull(parameters, "parameters");
		Args.notNull(validator, "validator");
		
		ParameterValidationErrorCollector collector = new ParameterValidationErrorCollector();
		validator.validate(parameters, collector);
		
		Collection<IParameterValidationError> errors = collector.getErrors();
		if ( ! errors.isEmpty() ) {
			throw new ParameterValidationException(parameters, errors);
		}
	}
	
	/**
	 * Creates a validator that performs the validation sequentially with each validator in the given order.
	 * <p>The resulting validator is {@link Serializable} if <code>validators</code> is Serializable.
	 * @param validators The validators to be chained
	 * @return A validator chaining the given validators. If empty, no validation will be performed and no {@link IParameterValidationError error} will ever occur.
	 * @throws NullPointerException if <code>validators</code> is null.
	 */
	public static IParameterValidator chain(Iterable<IParameterValidator> validators) {
		Args.notNull(validators, "validators");
		return new ChainedParameterValidator(validators);
	}
	
	private static class ChainedParameterValidator implements IParameterValidator {
		private static final long serialVersionUID = 7397495696437744067L;
		
		private final Iterable<IParameterValidator> validators;
		
		public ChainedParameterValidator(Iterable<IParameterValidator> validators) {
			super();
			this.validators = validators;
		}
		
		@Override
		public void validate(PageParameters parameters, ParameterValidationErrorCollector collector) {
			for (IParameterValidator validator : validators) {
				validator.validate(parameters, collector);
			}
		}
	}

}
