package org.iglooproject.wicket.more.link.descriptor.builder.state.main;

import java.util.Collection;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic.IGenericFourMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A builder state with four mappable parameters from which one may:
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
public interface IFourMappableParameterMainState<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends IGenericFourMappableParameterMainState<
            IFourMappableParameterMainState<
                TParam1,
                TParam2,
                TParam3,
                TParam4,
                TLateTargetDefinitionPageLinkDescriptor,
                TLateTargetDefinitionResourceLinkDescriptor,
                TLateTargetDefinitionImageResourceLinkDescriptor>,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            IFourParameterLinkDescriptorMapper<
                TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4>,
            IFourParameterLinkDescriptorMapper<
                TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>,
            IFourParameterLinkDescriptorMapper<
                TLateTargetDefinitionImageResourceLinkDescriptor,
                TParam1,
                TParam2,
                TParam3,
                TParam4>>,
        IMappableParameterDeclarationState {

  @Override
  <TParam5>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam5> clazz);

  @Override
  <TParam5 extends Collection<TElement>, TElement>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam5> clazz, Class<TElement> elementType);

  @Override
  <TParam5 extends Collection<?>>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam5> clazz, TypeDescriptor elementTypeDescriptor);

  @Override
  <TParam5 extends Collection<?>>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam5> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam5> emptyCollectionSupplier);
}
