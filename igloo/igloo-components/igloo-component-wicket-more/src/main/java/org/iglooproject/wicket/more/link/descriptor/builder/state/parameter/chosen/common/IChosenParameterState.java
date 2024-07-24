package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.ITwoOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ConditionLinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import org.javatuples.Tuple;

/**
 * A state where some mappable parameters were {@link IMappableParameterDeclarationState declared}
 * and some were {@link ITwoOrMoreMappableParameterNoneChosenChoiceState#pickFirst() chosen}.
 *
 * <p>From this state, one may:
 *
 * <ul>
 *   <li>map model parameters ({@link IModel}) to link parameters ({@link PageParameters})
 *   <li>add parameter-dependent validation to the link.
 *   <li>build a link descriptor mapper with a parameter-dependent target.
 * </ul>
 */
public interface IChosenParameterState<
    TInitialState,
    TTuple extends Tuple,
    TLateTargetDefinitionPageResult,
    TLateTargetDefinitionResourceResult,
    TLateTargetDefinitionImageResourceResult> {

  /**
   * Register a {@link ILinkParameterMappingEntry} that will handle the process of mapping HTTP
   * query parameters to its internal models.
   *
   * <p>The {@link ILinkParameterMappingEntry} will be created by the given factory when the model
   * parameters will be fully known.
   *
   * @param parameterMappingEntryFactory The factory for creating the object responsible for mapping
   *     parameters.
   * @return A builder state where you will decide whether the newly created mapping is either
   *     mandatory or optional.
   */
  IAddedParameterMappingState<TInitialState> map(
      ILinkParameterMappingEntryFactory<? super TTuple> parameterMappingEntryFactory);

  /**
   * Add a validator to the resulting link descriptor.
   *
   * <p>The validator will be created by the given factory when the model parameters will be fully
   * known.
   *
   * <p>The validator will be executed each time a link will get {@link ILinkGenerator generated} or
   * link parameters will get {@link ILinkParametersExtractor extracted}.
   *
   * <p>This method has the advantage over {@link #validator(IDetachableFactory)} that it allows for
   * validation of raw parameters, not only model parameters.
   *
   * @param parameterValidatorFactory The factory for creating the validator.
   * @return The original state (without any chosen parameter) for chaining calls.
   * @see ILinkParameterValidatorFactory
   * @see ILinkParameterValidator
   */
  TInitialState validator(ILinkParameterValidatorFactory<? super TTuple> parameterValidatorFactory);

  /**
   * {@link #validator(ILinkParameterValidatorFactory) Add a validator to the resulting link
   * descriptor} that will pass validation if and only if the given condition is <code>true</code>,
   * and raise an error otherwise.
   *
   * <p>The condition, and thus the validator, will be created by the given factory when the model
   * parameters will be fully known.
   *
   * @param condition The condition on which the validation should pass.
   * @return The original state (without any chosen parameter) for chaining calls.
   * @see ConditionLinkParameterValidator
   */
  TInitialState validator(IDetachableFactory<? super TTuple, ? extends Condition> conditionFactory);

  /**
   * A version of {@link ILateTargetDefinitionTerminalState#page(Class)} with a parameter-dependent
   * target.
   */
  TLateTargetDefinitionPageResult page(
      IDetachableFactory<? super TTuple, ? extends IModel<? extends Class<? extends Page>>>
          pageClassFactory);

  /**
   * A version of {@link ILateTargetDefinitionTerminalState#resource(ResourceReference)} with a
   * parameter-dependent target.
   */
  TLateTargetDefinitionResourceResult resource(
      IDetachableFactory<? super TTuple, ? extends IModel<? extends ResourceReference>>
          resourceReferenceFactory);

  /**
   * A version of {@link ILateTargetDefinitionTerminalState#imageResource(ResourceReference)} with a
   * parameter-dependent target.
   */
  TLateTargetDefinitionImageResourceResult imageResource(
      IDetachableFactory<? super TTuple, ? extends IModel<? extends ResourceReference>>
          resourceReferenceFactory);
}
