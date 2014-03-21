package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<L extends ILinkParametersExtractor> extends ITerminalState<L> {
	
	IValidatorState<L> validator(ILinkParameterValidator validator);
	
	IValidatorState<L> permission(IModel<? extends GenericEntity<?, ?>> model, String firstPermissionName, String ... otherPermissionNames);
	
	<R, T extends GenericEntity<?, ?>>
			IValidatorState<L> permission(IModel<R> model, BindingRoot<R, T> binding, String firstPermissionName, String ... otherPermissionNames);

}
