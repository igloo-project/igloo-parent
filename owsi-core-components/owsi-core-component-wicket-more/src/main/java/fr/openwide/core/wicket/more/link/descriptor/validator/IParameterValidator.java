package fr.openwide.core.wicket.more.link.descriptor.validator;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.io.IClusterable;

public interface IParameterValidator extends IClusterable {

	void validate(PageParameters parameters, ParameterValidationErrorCollector collector);

}
