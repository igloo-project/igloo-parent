package org.iglooproject.wicket.more.link.descriptor.builder.state.main;

import java.util.Collection;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic.IGenericOneMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A builder state with one mappable parameter from which one may:
 *
 * <ul>
 *   <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *   <li>call any of the {@link IChosenParameterState} and {@link IOneChosenParameterState} methods,
 *       allowing to reference the newly-added mappable parameters without it being entirely defined
 *       (no model was provided yet).
 *   <li>add another mappable parameter by calling the {@link #model(Class)} method.
 *   <li>end the build with one of the {@link ILateTargetDefinitionTerminalState} methods.
 * </ul>
 */
public interface IOneMappableParameterMainState<
        TParam1,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends IGenericOneMappableParameterMainState<
            IOneMappableParameterMainState<
                TParam1,
                TLateTargetDefinitionPageLinkDescriptor,
                TLateTargetDefinitionResourceLinkDescriptor,
                TLateTargetDefinitionImageResourceLinkDescriptor>,
            TParam1,
            IOneParameterLinkDescriptorMapper<TLateTargetDefinitionPageLinkDescriptor, TParam1>,
            IOneParameterLinkDescriptorMapper<TLateTargetDefinitionResourceLinkDescriptor, TParam1>,
            IOneParameterLinkDescriptorMapper<
                TLateTargetDefinitionImageResourceLinkDescriptor, TParam1>>,
        IMappableParameterDeclarationState {

  @Override
  <TParam2>
      ITwoMappableParameterMainState<
              TParam1,
              TParam2,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam2> clazz);

  @Override
  <TParam2 extends Collection<TElement>, TElement>
      ITwoMappableParameterMainState<
              TParam1,
              TParam2,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam2> clazz, Class<TElement> elementType);

  @Override
  <TParam2 extends Collection<?>>
      ITwoMappableParameterMainState<
              TParam1,
              TParam2,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam2> clazz, TypeDescriptor elementTypeDescriptor);

  @Override
  <TParam2 extends Collection<?>>
      ITwoMappableParameterMainState<
              TParam1,
              TParam2,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam2> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam2> emptyCollectionSupplier);
}
