package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.condition.Condition;

public class ConditionLinkParameterValidator implements ILinkParameterValidator {
	
	private static final long serialVersionUID = -6678335084190190566L;

	private final Condition condition;
	
	public ConditionLinkParameterValidator(Condition condition) {
		this.condition = condition;
	}

	@Override
	public void validateSerialized(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
		// Nothing to do
	}

	@Override
	public void validateModel(LinkParameterValidationErrorCollector collector) {
		if (!condition.applies()) {
			collector.addError(String.format("Condition '%s' was false.", condition));
		}
	}

	@Override
	public void detach() {
		condition.detach();
	}

}
