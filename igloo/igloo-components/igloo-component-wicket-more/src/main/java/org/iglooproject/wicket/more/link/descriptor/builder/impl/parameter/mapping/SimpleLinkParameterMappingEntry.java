package org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.mapping;

import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.AbstractLinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.AbstractLinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.SimpleMandatoryLinkParameterValidator;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

public class SimpleLinkParameterMappingEntry<T> extends AbstractLinkParameterMappingEntry {

  private static final long serialVersionUID = -8490340879965229874L;

  public static <T> ILinkParameterMappingEntryFactory<Unit<IModel<T>>> factory(
      final String parameterName,
      final SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier) {
    Args.notNull(parameterName, "parameterName");
    Args.notNull(typeDescriptorSupplier, "typeDescriptorSupplier");

    return new AbstractLinkParameterMappingEntryFactory<Unit<IModel<T>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterMappingEntry create(Unit<IModel<T>> parameters) {
        return new SimpleLinkParameterMappingEntry<T>(
            parameterName, parameters.getValue0(), typeDescriptorSupplier);
      }
    };
  }

  protected final String parameterName;
  protected final IModel<T> mappedModel;
  protected final SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier;

  public SimpleLinkParameterMappingEntry(
      String parameterName,
      IModel<T> mappedModel,
      SerializableSupplier2<? extends TypeDescriptor> typeDescriptorSupplier) {
    this.parameterName = parameterName;
    this.mappedModel = mappedModel;
    this.typeDescriptorSupplier = typeDescriptorSupplier;
  }

  @Override
  public void inject(
      PageParameters targetParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterInjectionException {
    inject(targetParameters, conversionService, parameterName, mappedModel.getObject());
  }

  @Override
  @SuppressWarnings("unchecked")
  public void extract(
      PageParameters sourceParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterExtractionException {
    TypeDescriptor mappedType = typeDescriptorSupplier.get();
    mappedModel.setObject(
        (T)
            mappedType
                .getType()
                .cast(extract(sourceParameters, conversionService, parameterName, mappedType)));
  }

  @Override
  public ILinkParameterMappingEntry wrap(Component component) {
    IModel<T> wrappedModel = wrap(mappedModel, component);
    return new SimpleLinkParameterMappingEntry<T>(
        parameterName, wrappedModel, typeDescriptorSupplier);
  }

  @Override
  public ILinkParameterValidator mandatoryValidator() {
    return new SimpleMandatoryLinkParameterValidator(List.of(parameterName), List.of(mappedModel));
  }

  @Override
  public void detach() {
    mappedModel.detach();
  }
}
