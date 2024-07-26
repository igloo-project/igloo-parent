package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ConditionLinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import org.javatuples.Unit;

/**
 * A specialization of {@link IChosenParameterState} where only one mappable parameter has been
 * chosen.
 *
 * <p>On top of the methods provided by {@link IChosenParameterState}, this state provides several
 * mapping and validation methods that only make sense when applied to a single model.
 */
public interface IOneChosenParameterState<
        TInitialState,
        TChosenParam1,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IChosenParameterState<
        TInitialState,
        Unit<IModel<TChosenParam1>>,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult> {

  /**
   * Map the HTTP query parameter with the given name to the currently selected model, making sure
   * that:
   *
   * <ul>
   *   <li>when {@link ILinkParametersExtractor extracting parameters} from a HTTP query, the
   *       parameter with the given name will get converted and stored in the currently selected
   *       model
   *   <li>when {@link ILinkGenerator generating a link's URL}, the value stored in the currently
   *       selected model will get converted and assigned to the HTTP query parameter with the given
   *       name.
   * </ul>
   *
   * <p><strong>Note:</strong> collection mapping requires calling {@link #mapCollection(String,
   * Class)} instead.
   *
   * @param parameterName The HTTP query parameter name.
   * @return A builder state where you will decide whether the newly created mapping is either
   *     mandatory or optional.
   */
  IAddedParameterMappingState<TInitialState> map(String parameterName);

  /**
   * Map HTTP query parameter with the given name to the currently selected model, making sure that:
   *
   * <ul>
   *   <li>when {@link ILinkParametersExtractor extracting parameters} from a HTTP query, the
   *       parameter <strong>is not written to the currently selected model</strong>.
   *   <li>when {@link ILinkGenerator generating a link's URL}, the value stored in the currently
   *       selected model will get converted and assigned to the HTTP query parameter with the given
   *       name.
   * </ul>
   *
   * <p>This is useful when dealing with interrelated HTTP query parameters where one parameters
   * will fully determine another, in which case the latter will only be here for cosmetics.
   *
   * @param parameterName The HTTP query parameter name.
   * @param valueModel The mapped model.
   * @return A builder state where you will decide whether the newly created mapping is either
   *     mandatory or optional.
   */
  IAddedParameterMappingState<TInitialState> renderInUrl(String parameterName);

  /**
   * Similar to {@link #renderInUrl(String)}, but renders the object pointed to by the given binding
   * on the currently selected model's object, instead of simply rendering the currently selected
   * model's object.
   */
  IAddedParameterMappingState<TInitialState> renderInUrl(
      String parameterName, BindingRoot<? super TChosenParam1, ?> binding);

  /**
   * {@link #validator(ILinkParameterValidatorFactory) Add a validator to the resulting link
   * descriptor} that will pass validation if and only if the given predicate is <code>true</code>
   * when applied to the currently selected model's object, and raise an error otherwise.
   *
   * @param predicate The predicate on which the validation should pass.
   * @return The original state (without any chosen parameter) for chaining calls.
   * @see ConditionLinkParameterValidator
   */
  TInitialState validator(SerializablePredicate2<? super TChosenParam1> predicate);

  /**
   * {@link #validator(ILinkParameterValidator) Add a validator to the resulting link descriptor}
   * that will pass validation if and only if the authenticated user has the given permission on the
   * object contained in the currently selected model, and raise an error otherwise.
   *
   * @param permissionName The name of the permission that should be granted.
   * @return The original state (without any chosen parameter) for chaining calls.
   * @see ConditionLinkParameterValidator
   */
  TInitialState permission(String permissionName);

  /**
   * {@link #validator(ILinkParameterValidator) Add a validator to the resulting link descriptor}
   * that will pass validation if and only if the authenticated user has <strong>any</strong> of the
   * given permissions on the object contained in the currently selected model, and raise an error
   * otherwise.
   *
   * @param firstPermissionName The name of a permission that is sufficient to pass validation.
   * @param otherPermissionNames The name of other permissions that are each sufficient to pass
   *     validation.
   * @see ConditionLinkParameterValidator
   */
  TInitialState permission(String firstPermissionName, String... otherPermissionNames);

  /**
   * Similar to {@link #permission(String, String...)}, but checks permissions on the object pointed
   * to by the given binding on the currently selected model's object, instead of simply checking
   * them on the currently selected model's object.
   */
  TInitialState permission(
      BindingRoot<? super TChosenParam1, ?> binding,
      String firstPermissionName,
      String... otherPermissionNames);
}
