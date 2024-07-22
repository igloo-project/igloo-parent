package org.iglooproject.wicket.more.link.descriptor.builder.state.main;

import java.util.Collection;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic.IGenericTwoMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A builder state with two mappable parameters from which one may:
 *
 * <ul>
 *   <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *   <li>call {@link #pickFirst()} or {@link #pickSecond()} to choose among mappable parameters and
 *       then use any of the {@link IChosenParameterState} methods, allowing to reference these
 *       mappable parameters without them being entirely defined (no model was provided yet).
 *   <li>add another mappable parameter by calling the {@link #model(Class)} method.
 *   <li>end the build with one of the {@link ILateTargetDefinitionTerminalState} methods.
 * </ul>
 */
public interface ITwoMappableParameterMainState<
        TParam1,
        TParam2,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends IGenericTwoMappableParameterMainState<
            ITwoMappableParameterMainState<
                TParam1,
                TParam2,
                TLateTargetDefinitionPageLinkDescriptor,
                TLateTargetDefinitionResourceLinkDescriptor,
                TLateTargetDefinitionImageResourceLinkDescriptor>,
            TParam1,
            TParam2,
            ITwoParameterLinkDescriptorMapper<
                TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2>,
            ITwoParameterLinkDescriptorMapper<
                TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2>,
            ITwoParameterLinkDescriptorMapper<
                TLateTargetDefinitionImageResourceLinkDescriptor, TParam1, TParam2>>,
        IMappableParameterDeclarationState {

  @Override
  <TParam3>
      IThreeMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam3> clazz);

  @Override
  <TParam3 extends Collection<TElement>, TElement>
      IThreeMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam3> clazz, Class<TElement> elementType);

  @Override
  <TParam3 extends Collection<?>>
      IThreeMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam3> clazz, TypeDescriptor elementTypeDescriptor);

  @Override
  <TParam3 extends Collection<?>>
      IThreeMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam3> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam3> emptyCollectionSupplier);
}
