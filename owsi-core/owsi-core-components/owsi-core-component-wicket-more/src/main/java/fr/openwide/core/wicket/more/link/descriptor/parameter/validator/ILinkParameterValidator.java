package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.io.IClusterable;

public interface ILinkParameterValidator extends IClusterable {

	void validate(PageParameters parameters, LinkParameterValidationErrorCollector collector);

}
