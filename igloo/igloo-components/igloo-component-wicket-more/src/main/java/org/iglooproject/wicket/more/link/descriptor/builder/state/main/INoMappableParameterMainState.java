package org.iglooproject.wicket.more.link.descriptor.builder.state.main;

import java.util.Collection;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * An initial builder state from which one may:
 *
 * <ul>
 *   <li>build a LinkDescriptor directly, by calling any of the {@link IMainState} methods and then
 *       any of the {@link ILateTargetDefinitionTerminalState} method.
 *   <li>start building a LinkDescriptorMapper (for example {@link
 *       IOneParameterLinkDescriptorMapper} or {@link ITwoParameterLinkDescriptorMapper}), by
 *       calling the {@link #model(Class)} method.
 * </ul>
 */
public interface INoMappableParameterMainState<
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends IMainState<
            INoMappableParameterMainState<
                TLateTargetDefinitionPageLinkDescriptor,
                TLateTargetDefinitionResourceLinkDescriptor,
                TLateTargetDefinitionImageResourceLinkDescriptor>>,
        IMappableParameterDeclarationState,
        ILateTargetDefinitionTerminalState<
            TLateTargetDefinitionPageLinkDescriptor,
            TLateTargetDefinitionResourceLinkDescriptor,
            TLateTargetDefinitionImageResourceLinkDescriptor> {

  @Override
  <TParam1>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam1> clazz);

  @Override
  <TParam1 extends Collection<TElement>, TElement>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam1> clazz, Class<TElement> elementType);

  @Override
  <TParam1 extends Collection<?>>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam1> clazz, TypeDescriptor elementTypeDescriptor);

  @Override
  <TParam1 extends Collection<?>>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam1> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam1> emptyCollectionSupplier);
}
