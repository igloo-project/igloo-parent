package org.iglooproject.wicket.more.link.descriptor.parameter.mapping;

import igloo.wicket.model.BindingModel;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.bindgen.BindingRoot;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.AbstractLinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.SimpleMandatoryLinkParameterValidator;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;
import org.javatuples.Unit;

public class InjectOnlyLinkParameterMappingEntry<T> extends AbstractLinkParameterMappingEntry {

  private static final long serialVersionUID = -5638280709363241126L;

  protected final String parameterName;
  protected final IModel<? extends T> mappedModel;

  public static <T> ILinkParameterMappingEntryFactory<Unit<IModel<? extends T>>> factory(
      final String parameterName) {
    Args.notNull(parameterName, "parameterName");
    return new AbstractLinkParameterMappingEntryFactory<Unit<IModel<? extends T>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterMappingEntry create(Unit<IModel<? extends T>> parameters) {
        return new InjectOnlyLinkParameterMappingEntry<T>(parameterName, parameters.getValue0());
      }
    };
  }

  public static <R, T> ILinkParameterMappingEntryFactory<Unit<IModel<? extends R>>> factory(
      final String parameterName, final BindingRoot<R, T> bindingRoot) {
    Args.notNull(parameterName, "parameterName");
    Args.notNull(bindingRoot, "bindingRoot");
    return new AbstractLinkParameterMappingEntryFactory<Unit<IModel<? extends R>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterMappingEntry create(Unit<IModel<? extends R>> parameters) {
        return new InjectOnlyLinkParameterMappingEntry<T>(
            parameterName, BindingModel.of(parameters.getValue0(), bindingRoot));
      }
    };
  }

  public InjectOnlyLinkParameterMappingEntry(
      String parameterName, IModel<? extends T> mappedModel) {
    this.parameterName = parameterName;
    this.mappedModel = mappedModel;
  }

  @Override
  public void inject(
      PageParameters targetParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterInjectionException {
    inject(targetParameters, conversionService, parameterName, mappedModel.getObject());
  }

  @Override
  public void extract(
      PageParameters sourceParameters, ILinkParameterConversionService conversionService) {
    // Nothing to do
  }

  @Override
  public ILinkParameterMappingEntry wrap(Component component) {
    IModel<? extends T> wrappedModel = wrap(mappedModel, component);
    return new InjectOnlyLinkParameterMappingEntry<T>(parameterName, wrappedModel);
  }

  @Override
  public ILinkParameterValidator mandatoryValidator() {
    return new SimpleMandatoryLinkParameterValidator(
        List.of(parameterName), List.of() // The model is not mandatory : we won't extract it anyway
        );
  }

  @Override
  public void detach() {
    mappedModel.detach();
  }
}
