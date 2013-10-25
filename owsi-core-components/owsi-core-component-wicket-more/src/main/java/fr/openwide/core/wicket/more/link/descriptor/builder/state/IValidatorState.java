package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<L extends ILinkParametersExtractor> extends ITerminalState<L> {
	
	IValidatorState<L> validator(ILinkParameterValidator validator);

}
