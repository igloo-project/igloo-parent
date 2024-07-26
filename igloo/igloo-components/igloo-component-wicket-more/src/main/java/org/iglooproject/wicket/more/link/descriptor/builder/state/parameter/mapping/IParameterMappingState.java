package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping;

import java.util.Collection;
import java.util.TreeSet;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A state where one may map model parameters ({@link IModel}) to link parameters ({@link
 * PageParameters}).
 *
 * <p><strong>Note:</strong> instead of using methods from this interface, please consider declaring
 * your model parameters using {@link IMappableParameterDeclarationState#model(Class)} and then map
 * those declared parameters using one of the methods defined in {@link IChosenParameterState} or
 * {@link IOneChosenParameterState}. This will allow you to build a {@link ILinkDescriptorMapper
 * link descriptor mapper}, suitable for use in declarative builders such as {@link
 * org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder}.
 */
public interface IParameterMappingState<TSelf extends IParameterMappingState<TSelf>> {

  /**
   * Map the HTTP query parameter with the given name to the given model, making sure that:
   *
   * <ul>
   *   <li>when {@link ILinkParametersExtractor extracting parameters} from a HTTP query, the
   *       parameter with the given name will get converted and stored in the given model
   *   <li>when {@link ILinkGenerator generating a link's URL}, the value stored in the given model
   *       will get converted and assigned to the HTTP query parameter with the given name.
   * </ul>
   *
   * <p><strong>Note:</strong> collection mapping requires calling {@link #mapCollection(String,
   * IModel, Class, Class)} instead.
   *
   * @param parameterName The HTTP query parameter name.
   * @param valueModel The mapped model.
   * @param valueType The expected type of values stored in the model (for use during conversion).
   * @return A builder state where you will decide whether the newly created mapping is either
   *     mandatory or optional.
   */
  <T> IAddedParameterMappingState<TSelf> map(
      String parameterName, IModel<T> valueModel, Class<T> valueType);

  /**
   * Map the HTTP query parameter with the given name to the given model.
   *
   * <p>This method should be used when the model is expected to contain a collection, since it
   * enables implementors to convert collection elements as well as the collection itself.
   *
   * @param parameterName The HTTP query parameter name.
   * @param valueModel The mapped model.
   * @param rawCollectionType The expected type of values stored in the model (for use during
   *     conversion).
   * @param elementType The expected type of elements stored in the collection (for use during
   *     conversion).
   * @see #map(String, IModel, Class)
   */
  @SuppressWarnings("rawtypes")
  <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf> mapCollection(
      String parameterName,
      IModel<C> valueModel,
      Class<RawC> rawCollectionType,
      Class<T> elementType);

  /**
   * Map the HTTP query parameter with the given name to the given model.
   *
   * <p>This method should be used when the model is expected to contain a <strong>collection of
   * collections</strong>, since it enables implementors to convert collection elements as well as
   * the collection itself.
   *
   * @param parameterName The HTTP query parameter name.
   * @param valueModel The mapped model.
   * @param rawCollectionType The expected type of values stored in the model (for use during
   *     conversion).
   * @param elementTypeDescriptor The expected type of elements stored in the collection (for use
   *     during conversion).
   * @see #map(String, IModel, Class)
   */
  @SuppressWarnings("rawtypes")
  <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf> mapCollection(
      String parameterName,
      IModel<C> valueModel,
      Class<RawC> rawCollectionType,
      TypeDescriptor elementTypeDescriptor);

  /**
   * Similar to {@link #mapCollection(String, IModel, Class, TypeDescriptor)}, but additionally
   * allows to define the exact collection implementation to use (for instance a {@link TreeSet}
   * even if the raw type is simply {@link Collection}).
   */
  @SuppressWarnings("rawtypes")
  <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf> mapCollection(
      String parameterName,
      IModel<C> valueModel,
      Class<RawC> rawCollectionType,
      TypeDescriptor elementTypeDescriptor,
      SerializableSupplier2<C> emptyCollectionSupplier);

  /**
   * Register a {@link ILinkParameterMappingEntry} that will handle the process of mapping HTTP
   * query parameters to its internal models.
   *
   * @param parameterMappingEntry The object responsible for mapping parameters.
   * @return A builder state where you will decide whether the newly created mapping is either
   *     mandatory or optional.
   */
  IAddedParameterMappingState<TSelf> map(ILinkParameterMappingEntry parameterMappingEntry);

  /**
   * Map HTTP query parameter with the given name to the given model, making sure that:
   *
   * <ul>
   *   <li>when {@link ILinkParametersExtractor extracting parameters} from a HTTP query, the
   *       parameter <strong>is not written to the given model</strong>.
   *   <li>when {@link ILinkGenerator generating a link's URL}, the value stored in the given model
   *       will get converted and assigned to the HTTP query parameter with the given name.
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
  <T> IAddedParameterMappingState<TSelf> renderInUrl(String parameterName, IModel<T> valueModel);

  /**
   * Shorthand for <code>renderInUrl(parameterName, BindingModel.of(rootModel, binding))</code>
   *
   * @see #renderInUrl(String, IModel)
   */
  <R, T> IAddedParameterMappingState<TSelf> renderInUrl(
      String parameterName, IModel<R> rootModel, BindingRoot<R, T> binding);
}
