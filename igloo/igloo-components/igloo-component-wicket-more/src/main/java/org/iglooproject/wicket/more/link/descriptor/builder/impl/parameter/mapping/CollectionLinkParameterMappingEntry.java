package org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.mapping;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.AbstractLinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.AbstractLinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.SimpleMandatoryCollectionLinkParameterValidator;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

@SuppressWarnings("rawtypes")
public class CollectionLinkParameterMappingEntry<C extends Collection>
    extends AbstractLinkParameterMappingEntry {

  private static final long serialVersionUID = 2126702467532153474L;

  public static <C extends Collection> ILinkParameterMappingEntryFactory<Unit<IModel<C>>> factory(
      final String parameterName,
      final SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier,
      final SerializableSupplier2<C> emptyCollectionSupplier) {
    Args.notNull(parameterName, "parameterName");

    return new AbstractLinkParameterMappingEntryFactory<Unit<IModel<C>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterMappingEntry create(Unit<IModel<C>> parameters) {
        return new CollectionLinkParameterMappingEntry<>(
            parameterName, parameters.getValue0(), typeDescriptorSupplier, emptyCollectionSupplier);
      }
    };
  }

  protected final String parameterName;
  protected final IModel<C> mappedModel;
  protected final SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier;
  protected final SerializableFunction2<? super C, ? extends C> collectionCustomizerFunction;

  public CollectionLinkParameterMappingEntry(
      String parameterName,
      IModel<C> mappedModel,
      SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier) {
    this(parameterName, mappedModel, typeDescriptorSupplier, Functions2.<C>identity());
  }

  /**
   * @param emptyCollectionSupplier Allows to perform fine-tuned customization on the actual
   *     collection instance that cannot be performed by the conversionService itself (for example,
   *     instantiation of TreeSet with a specific comparator)
   */
  public CollectionLinkParameterMappingEntry(
      String parameterName,
      IModel<C> mappedModel,
      SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier,
      final SerializableSupplier2<? extends C> emptyCollectionSupplier) {
    this(
        parameterName,
        mappedModel,
        typeDescriptorSupplier,
        emptyCollectionSupplier == null
            ? Functions2.<C>identity()
            : new SerializableFunction2<C, C>() {
              private static final long serialVersionUID = 1L;

              @Override
              @SuppressWarnings("unchecked")
              public C apply(C input) {
                C newCollection = emptyCollectionSupplier.get();
                newCollection.addAll(input);
                return newCollection;
              }
            });
  }

  public CollectionLinkParameterMappingEntry(
      String parameterName,
      IModel<C> mappedModel,
      SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier,
      SerializableFunction2<? super C, ? extends C> collectionCustomizerFunction) {
    checkNotNull(parameterName);
    checkNotNull(mappedModel);
    checkNotNull(typeDescriptorSupplier);
    checkNotNull(typeDescriptorSupplier.get());
    checkNotNull(collectionCustomizerFunction);
    checkArgument(
        typeDescriptorSupplier.get().isCollection(),
        "typeDescriptorSupplier must be a collection type");
    checkNotNull(
        typeDescriptorSupplier.get().getElementTypeDescriptor(),
        "typeDescriptorSupplier's element type must be defined");
    this.parameterName = parameterName;
    this.mappedModel = mappedModel;
    this.typeDescriptorSupplier = typeDescriptorSupplier;
    this.collectionCustomizerFunction = collectionCustomizerFunction;
  }

  @Override
  public void inject(
      PageParameters targetParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterInjectionException {
    C collection = mappedModel.getObject();
    if (collection != null && collection.isEmpty()) {
      collection =
          null; // Just make sure that the default spring converter for collections won't translate
      // this to an empty string.
    }
    inject(targetParameters, conversionService, parameterName, collection);
  }

  @Override
  public void extract(
      PageParameters sourceParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterExtractionException {
    TypeDescriptor typeDescriptor = typeDescriptorSupplier.get();
    Object extractedCollection =
        extract(sourceParameters, conversionService, parameterName, typeDescriptor);
    @SuppressWarnings("unchecked")
    C castedCollection = (C) typeDescriptor.getType().cast(extractedCollection);
    C finalCollection = collectionCustomizerFunction.apply(castedCollection);
    mappedModel.setObject(finalCollection);
  }

  @Override
  public ILinkParameterMappingEntry wrap(Component component) {
    IModel<C> wrappedModel = wrap(mappedModel, component);
    return new CollectionLinkParameterMappingEntry<>(
        parameterName, wrappedModel, typeDescriptorSupplier, collectionCustomizerFunction);
  }

  @Override
  public ILinkParameterValidator mandatoryValidator() {
    return new SimpleMandatoryCollectionLinkParameterValidator(parameterName, mappedModel);
  }

  @Override
  public void detach() {
    mappedModel.detach();
  }
}
