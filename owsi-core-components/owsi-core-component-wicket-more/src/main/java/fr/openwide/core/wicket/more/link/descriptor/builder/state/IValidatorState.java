package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<T extends ILinkDescriptor> extends ITerminalState<T> {
	
	IValidatorState<T> validator(ILinkParameterValidator validator);

}
