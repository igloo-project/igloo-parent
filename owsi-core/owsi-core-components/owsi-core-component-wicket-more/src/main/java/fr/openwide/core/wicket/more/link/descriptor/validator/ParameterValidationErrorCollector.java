package fr.openwide.core.wicket.more.link.descriptor.validator;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Lists;

public class ParameterValidationErrorCollector {
	
	private final Collection<IParameterValidationError> errors = Lists.newArrayList();
	
	public void addError(String message) {
		errors.add(new SimpleParameterValidationErrorImpl(message));
	}
	
	public void addError(IParameterValidationError error) {
		errors.add(error);
	}
	
	public Collection<IParameterValidationError> getErrors() {
		return Collections.unmodifiableCollection(errors);
	}
	
	private static class SimpleParameterValidationErrorImpl implements IParameterValidationError {
		
		private final String message;
		
		public SimpleParameterValidationErrorImpl(String message) {
			super();
			this.message = message;
		}
		
		@Override
		public String getMessage() {
			return message;
		}
		
		@Override
		public String toString() {
			return getMessage();
		}
	}

}
