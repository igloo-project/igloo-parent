package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<L extends ILinkParametersExtractor> extends ITerminalState<L> {
	
	IValidatorState<L> validator(ILinkParameterValidator validator);
	
	IValidatorState<L> validator(Condition condition);
	
	IValidatorState<L> permission(IModel<?> model, String permissionName);
	
	IValidatorState<L> permission(IModel<?> model, String firstPermissionName, String ... otherPermissionNames);
	
	<R, T> IValidatorState<L> permission(IModel<R> model, BindingRoot<R, T> binding,
					String firstPermissionName, String ... otherPermissionNames);

}
