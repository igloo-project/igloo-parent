package org.iglooproject.wicket.more.link.descriptor.impl;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

public abstract class AbstractCoreExplicitelyParameterizedLinkDescriptor
    implements ILinkDescriptor {

  private static final long serialVersionUID = 2474433766347554582L;

  protected final LinkParametersMapping parametersMapping;
  protected final ILinkParameterValidator parametersValidator;

  public AbstractCoreExplicitelyParameterizedLinkDescriptor(
      LinkParametersMapping parametersMapping, ILinkParameterValidator parametersValidator) {
    super();
    Args.notNull(parametersMapping, "parametersModel");
    Args.notNull(parametersValidator, "parametersValidator");
    this.parametersMapping = parametersMapping;
    this.parametersValidator = parametersValidator;
  }

  protected final PageParameters getValidatedParameters() {
    try {
      LinkParameterValidators.checkModel(parametersValidator);
      PageParameters parameters = parametersMapping.getObject();
      LinkParameterValidators.checkSerialized(parameters, parametersValidator);
      return parameters;
    } catch (LinkParameterValidationException e) {
      throw new LinkParameterValidationRuntimeException(e);
    }
  }

  @Override
  public String fullUrl() {
    return fullUrl(RequestCycle.get());
  }

  @Override
  public String fullUrl(RequestCycle requestCycle) {
    return requestCycle.getUrlRenderer().renderFullUrl(Url.parse(url(requestCycle)));
  }

  @Override
  public boolean isAccessible() {
    if (LinkParameterValidators.isModelValid(parametersValidator)) {
      PageParameters parameters = parametersMapping.getObject();
      if (LinkParameterValidators.isSerializedValid(parameters, parametersValidator)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void extract(PageParameters parameters)
      throws LinkParameterSerializedFormValidationException,
          LinkParameterExtractionRuntimeException,
          LinkParameterModelValidationException {
    LinkParameterValidators.checkSerialized(parameters, parametersValidator);
    parametersMapping.setObject(parameters);
    LinkParameterValidators.checkModel(parametersValidator);
  }

  @Override
  public void checkModels() throws LinkParameterModelValidationException {
    LinkParameterValidators.checkModel(parametersValidator);
  }

  @Override
  public void detach() {
    parametersMapping.detach();
    parametersValidator.detach();
  }
}
