package org.iglooproject.wicket.more.link.descriptor.builder.state.main;

import java.util.Collection;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic.IGenericFiveMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IFiveParameterLinkDescriptorMapper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A builder state with five mappable parameters from which one may:
 *
 * <ul>
 *   <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *   <li>call {@link #pickFirst()} or {@link #pickSecond()} (etc.) to choose among mappable
 *       parameters and then use any of the {@link IChosenParameterState} methods, allowing to
 *       reference these mappable parameters without them being entirely defined (no model was
 *       provided yet).
 *   <li>add another mappable parameter by calling the {@link #model(Class)} method.
 *   <li>end the build with one of the {@link ILateTargetDefinitionTerminalState} methods.
 * </ul>
 */
public interface IFiveMappableParameterMainState<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends IGenericFiveMappableParameterMainState<
            IFiveMappableParameterMainState<
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5,
                TLateTargetDefinitionPageLinkDescriptor,
                TLateTargetDefinitionResourceLinkDescriptor,
                TLateTargetDefinitionImageResourceLinkDescriptor>,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            IFiveParameterLinkDescriptorMapper<
                TLateTargetDefinitionPageLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5>,
            IFiveParameterLinkDescriptorMapper<
                TLateTargetDefinitionResourceLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5>,
            IFiveParameterLinkDescriptorMapper<
                TLateTargetDefinitionImageResourceLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TParam5>>,
        IMappableParameterDeclarationState {

  @Override
  <TParam6>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam6> clazz);

  @Override
  <TParam6 extends Collection<TElement>, TElement>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam6> clazz, Class<TElement> elementType);

  @Override
  <TParam6 extends Collection<?>>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam6> clazz, TypeDescriptor elementTypeDescriptor);

  @Override
  <TParam6 extends Collection<?>>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam6> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam6> emptyCollectionSupplier);
}
