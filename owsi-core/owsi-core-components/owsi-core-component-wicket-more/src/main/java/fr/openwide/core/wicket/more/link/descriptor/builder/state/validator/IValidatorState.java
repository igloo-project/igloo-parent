package fr.openwide.core.wicket.more.link.descriptor.builder.state.validator;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IValidatorState<TSelf extends IValidatorState<TSelf>> {
	
	TSelf validator(ILinkParameterValidator validator);
	
	TSelf validator(Condition condition);

	TSelf permission(IModel<?> model, String permissionName);
	
	TSelf permission(IModel<?> model, String firstPermissionName, String ... otherPermissionNames);
	
	<R, T> TSelf permission(IModel<R> model, BindingRoot<R, T> binding, String firstPermissionName,
					String ... otherPermissionNames);

}
