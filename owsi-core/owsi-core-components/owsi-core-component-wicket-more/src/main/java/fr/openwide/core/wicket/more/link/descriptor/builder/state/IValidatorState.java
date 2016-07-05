package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<TResult> extends ITerminalState<TResult> {
	
	IValidatorState<TResult> validator(ILinkParameterValidator validator);
	
	IValidatorState<TResult> validator(Condition condition);
	
	IValidatorState<TResult> permission(IModel<?> model, String permissionName);
	
	IValidatorState<TResult> permission(IModel<?> model, String firstPermissionName, String ... otherPermissionNames);
	
	<R, T> IValidatorState<TResult> permission(IModel<R> model, BindingRoot<R, T> binding,
					String firstPermissionName, String ... otherPermissionNames);

}
