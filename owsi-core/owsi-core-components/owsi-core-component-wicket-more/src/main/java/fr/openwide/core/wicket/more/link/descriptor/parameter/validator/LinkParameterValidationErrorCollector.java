package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Lists;

public class LinkParameterValidationErrorCollector {
	
	private final Collection<ILinkParameterValidationError> errors = Lists.newArrayList();
	
	public void addError(String message) {
		errors.add(new SimpleParameterValidationErrorImpl(message));
	}
	
	public void addError(ILinkParameterValidationError error) {
		errors.add(error);
	}
	
	public Collection<ILinkParameterValidationError> getErrors() {
		return Collections.unmodifiableCollection(errors);
	}
	
	private static class SimpleParameterValidationErrorImpl implements ILinkParameterValidationError {
		
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
