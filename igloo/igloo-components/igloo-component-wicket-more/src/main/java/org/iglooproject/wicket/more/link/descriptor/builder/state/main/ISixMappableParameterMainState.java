package org.iglooproject.wicket.more.link.descriptor.builder.state.main;

import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic.IGenericSixMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.ISixOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.ISixParameterLinkDescriptorMapper;

/**
 * A builder state with six mappable parameters from which one may:
 *
 * <ul>
 *   <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *   <li>call {@link #pickFirst()} or {@link #pickSecond()} (etc.) to choose among mappable
 *       parameters and then use any of the {@link IChosenParameterState} methods, allowing to
 *       reference these mappable parameters without them being entirely defined (no model was
 *       provided yet).
 *   <li>end the build with one of the {@link ILateTargetDefinitionTerminalState} methods.
 * </ul>
 */
public interface ISixMappableParameterMainState<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends IGenericSixMappableParameterMainState<
            ISixMappableParameterMainState<
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5,
                TParam6,
                TLateTargetDefinitionPageLinkDescriptor,
                TLateTargetDefinitionResourceLinkDescriptor,
                TLateTargetDefinitionImageResourceLinkDescriptor>,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            TParam6,
            ISixParameterLinkDescriptorMapper<
                TLateTargetDefinitionPageLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5,
                TParam6>,
            ISixParameterLinkDescriptorMapper<
                TLateTargetDefinitionResourceLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5,
                TParam6>,
            ISixParameterLinkDescriptorMapper<
                TLateTargetDefinitionImageResourceLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5,
                TParam6>>,
        ISixOrMoreMappableParameterNoneChosenChoiceState {}
