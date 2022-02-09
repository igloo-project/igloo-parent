package org.iglooproject.wicket.more.link.descriptor.builder.state.validator;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ConditionLinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

/**
 * A state where one may add validation to the link.
 * <p>"Validation" here means any type of condition, which among others may be parameter-based (check that a parameter
 * is in a certain state), session-based (check that the user has required roles, or both (check that the user has
 * a certain permission on a parameter).
 * <p><strong>Note:</strong> if you're planning on adding parameter-based validation, please consider declaring your
 * model parameters using {@link IMappableParameterDeclarationState#model(Class)}, mapping those declared parameters
 * using one of the methods defined in {@link IChosenParameterState} or {@link IOneChosenParameterState} and adding
 * validation using the same interfaces. This will allow you to build a
 * {@link ILinkDescriptorMapper link descriptor mapper}, suitable for use in declarative builders such
 * as {@link org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder}.
 */
public interface IValidatorState<TSelf extends IValidatorState<TSelf>> {
	
	/**
	 * Add the given validator to the resulting link descriptor.
	 * <p>The validator will be executed each time a link will get {@link ILinkGenerator generated} or link parameters
	 * will get {@link ILinkParametersExtractor extracted}.
	 * <p><strong>Note:</strong> if your validator depends on one of the parameters, and you're building a
	 * {@link ILinkDescriptorMapper link descriptor mapper}, please consider calling
	 * {@link IChosenParameterState#validator(org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory)}
	 * instead.
	 * @param validator
	 * @return The same builder for chaining calls.
	 * @see ILinkParameterValidator
	 */
	TSelf validator(ILinkParameterValidator validator);
	
	/**
	 * {@link #validator(ILinkParameterValidator) Add a validator to the resulting link descriptor} that will pass
	 * validation if and only if the given condition is <code>true</code>, and raise an error otherwise.
	 * <p><strong>Note:</strong> if your condition depends on one of the parameters, and you're building a
	 * {@link ILinkDescriptorMapper link descriptor mapper}, please consider calling
	 * {@link IChosenParameterState#validator(org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory)}
	 * instead.
	 * @param condition The condition on which the validation should pass.
	 * @return The same builder for chaining calls.
	 * @see ConditionLinkParameterValidator
	 */
	TSelf validator(Condition condition);

	/**
	 * {@link #validator(ILinkParameterValidator) Add a validator to the resulting link descriptor} that will pass
	 * validation if and only if the authenticated user has the given permission on the object contained in the
	 * given model, and raise an error otherwise.
	 * <p><strong>Note:</strong> if the given model is one of the link parameters, and you're building a
	 * {@link ILinkDescriptorMapper link descriptor mapper}, please consider calling
	 * {@link IOneChosenParameterState#permission(String)} instead.
	 * @param model The object on which the permission should be granted.
	 * @param permissionName The name of the permission that should be granted.
	 * @return The same builder for chaining calls.
	 * @see Condition#permission(IModel, String)
	 */
	TSelf permission(IModel<?> model, String permissionName);
	
	/**
	 * {@link #validator(ILinkParameterValidator) Add a validator to the resulting link descriptor} that will pass
	 * validation if and only if the authenticated user has <strong>any</strong> of the given permissions on the object
	 * contained in the given model, and raise an error otherwise.
	 * <p><strong>Note:</strong> if the given model is one of the link parameters, and you're building a
	 * {@link ILinkDescriptorMapper link descriptor mapper}, please consider calling
	 * {@link IOneChosenParameterState#permission(String, String...)} instead.
	 * @param model The object on which one of the permissions should be granted.
	 * @param firstPermissionName The name of a permission that is sufficient to pass validation.
	 * @param otherPermissionNames The name of other permissions that are each sufficient to pass validation.
	 * @return The same builder for chaining calls.
	 * @see Condition#anyPermission(IModel, String, String...)
	 */
	TSelf permission(IModel<?> model, String firstPermissionName, String ... otherPermissionNames);

	/**
	 * Shorthand for
	 * <code>permission(BindingModel.of(model, binding), firstPermissionName, otherPermissionNames)</code>
	 * @see #permission(IModel, String, String...)
	 */
	<R, T> TSelf permission(IModel<R> model, BindingRoot<R, T> binding, String firstPermissionName,
					String ... otherPermissionNames);

}
