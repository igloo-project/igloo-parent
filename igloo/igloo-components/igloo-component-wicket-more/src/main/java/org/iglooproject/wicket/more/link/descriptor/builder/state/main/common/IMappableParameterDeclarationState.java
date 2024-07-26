package org.iglooproject.wicket.more.link.descriptor.builder.state.main.common;

import java.util.Collection;
import java.util.TreeSet;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.springframework.core.convert.TypeDescriptor;

/** A state from which one can declare a mappable parameter. */
public interface IMappableParameterDeclarationState {

  /**
   * Declare a mappable parameter that can be provided later through a {@link ILinkDescriptorMapper
   * link descriptor mapper}, but can still be referenced when building through methods such as
   * {@link IOneChosenParameterState#map(String)} or {@link
   * IChosenParameterState#map(org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory)}
   *
   * @param clazz The type of the parameter on the Java side, i.e. the type of objects that will be
   *     contained in models passed to the {@link ILinkDescriptorMapper link descriptor mapper}.
   * @return A builder state that allows for referencing the newly-added parameter. The exact type
   *     of this state is defined in extending interfaces such as {@link
   *     INoMappableParameterMainState}.
   */
  <TParam> IMainState<?> model(Class<TParam> clazz);

  /**
   * {@link #model(Class) Declare a mappable parameter of Collection type}.
   *
   * <p>This method should be used when the model is expected to contain an {@link Collection},
   * since it enables implementors to convert the collection's elements as well as the collection
   * itself.
   *
   * @param clazz The type of the parameter on the Java side, i.e. the type of objects that will be
   *     contained in models passed to the {@link
   *     IOneParameterLinkDescriptorMapper#map(org.apache.wicket.model.IModel) link descriptor
   *     mapper}.
   * @param elementType The expected type of elements stored in the parameter.
   * @return A builder state that allows for referencing the newly-added parameter. The exact type
   *     of this state is defined in extending interfaces such as {@link
   *     INoMappableParameterMainState}.
   * @see #model(Class)
   */
  <TParam extends Collection<TElement>, TElement> IMainState<?> model(
      Class<? super TParam> clazz, Class<TElement> elementType);

  /**
   * {@link #model(Class) Declare a mappable parameter of Collection type}.
   *
   * <p>This method should be used when the model is expected to contain an {@link Collection},
   * since it enables implementors to convert the collection's elements as well as the collection
   * itself.
   *
   * @param clazz The type of the parameter on the Java side, i.e. the type of objects that will be
   *     contained in models passed to the {@link
   *     IOneParameterLinkDescriptorMapper#map(org.apache.wicket.model.IModel) link descriptor
   *     mapper}.
   * @param elementTypeDescriptor The expected type of elements stored in the parameter.
   * @return A builder state that allows for referencing the newly-added parameter. The exact type
   *     of this state is defined in extending interfaces such as {@link
   *     INoMappableParameterMainState}.
   * @see #model(Class)
   */
  <TParam extends Collection<?>> IMainState<?> model(
      Class<? super TParam> clazz, TypeDescriptor elementTypeDescriptor);

  /**
   * Similar to {@link #model(Class, TypeDescriptor)}, but additionally allows to define the exact
   * collection implementation to use (for instance a {@link TreeSet} even if the raw type is simply
   * {@link Collection}).
   *
   * @see #model(Class, TypeDescriptor)
   */
  <TParam extends Collection<?>> IMainState<?> model(
      Class<? super TParam> clazz,
      TypeDescriptor elementTypeDescriptor,
      SerializableSupplier2<? extends TParam> emptyCollectionSupplier);
}
