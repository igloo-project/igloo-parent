package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator;

public interface IValidatorState<T extends ILinkDescriptor> extends ITerminalState<T> {
	
	ITerminalState<T> validator(IParameterValidator validator);

}
