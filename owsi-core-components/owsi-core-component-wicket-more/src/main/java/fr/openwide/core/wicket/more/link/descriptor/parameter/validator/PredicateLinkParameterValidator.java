package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.base.Predicate;

/**
 * @deprecated Use {@link fr.openwide.core.wicket.more.link.descriptor.builder.state.IValidatorState#validator(fr.openwide.core.wicket.more.condition.Condition)} instead
 */
@Deprecated
public class PredicateLinkParameterValidator<T> implements ILinkParameterValidator {
	
	private static final long serialVersionUID = 7015800524943994171L;
	
	private final Predicate<T> predicate;
	
	private final IModel<T> model;
	
	public PredicateLinkParameterValidator(Predicate<T> predicate, IModel<T> model) {
		this.predicate = predicate;
		this.model = model;
	}

	@Override
	public void validateSerialized(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
		// Nothing to do
	}

	@Override
	public void validateModel(LinkParameterValidationErrorCollector collector) {
		T object = model.getObject();
		if (!predicate.apply(object)) {
			collector.addError(String.format("Predicate '%s' did not apply to model object '%s'.", predicate, object));
		}
	}

	@Override
	public void detach() {
		model.detach();
	}

}
