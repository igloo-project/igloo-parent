package org.iglooproject.wicket.more.link.descriptor.builder.state.main.common;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.validator.IValidatorState;

/**
 * One of the main states, from which one may:
 *
 * <ul>
 *   <li>map already-defined model parameters, by calling methods from {@link
 *       IParameterMappingState}.
 *   <li>add link validators, by calling methods from {@link IValidatorState}.
 * </ul>
 */
public interface IMainState<TSelf extends IMainState<TSelf>>
    extends IParameterMappingState<TSelf>, IValidatorState<TSelf> {}
