package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<Result> extends ITerminalState<Result> {
	
	IValidatorState<Result> validator(ILinkParameterValidator validator);
	
	IValidatorState<Result> validator(Condition condition);
	
	IValidatorState<Result> permission(IModel<?> model, String permissionName);
	
	IValidatorState<Result> permission(IModel<?> model, String firstPermissionName, String ... otherPermissionNames);
	
	<R, T> IValidatorState<Result> permission(IModel<R> model, BindingRoot<R, T> binding,
					String firstPermissionName, String ... otherPermissionNames);

}
