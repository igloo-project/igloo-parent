package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import java.util.Collection;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

@SuppressWarnings("rawtypes")
public class SimpleMandatoryCollectionLinkParameterValidator implements ILinkParameterValidator {

  private static final long serialVersionUID = 7015800524943994171L;

  private final Collection<String> parameterNames;

  private final Collection<? extends IModel<? extends Collection>> parameterModels;

  public SimpleMandatoryCollectionLinkParameterValidator(
      String parameterName, IModel<? extends Collection> parameterModel) {
    this(List.of(parameterName), List.of(parameterModel));
  }

  public SimpleMandatoryCollectionLinkParameterValidator(
      Collection<String> parameterNames,
      Collection<? extends IModel<? extends Collection>> parameterModels) {
    Args.notNull(parameterNames, "parameterNames");
    Args.notNull(parameterModels, "parameterModels");
    this.parameterNames = List.copyOf(parameterNames);
    this.parameterModels = List.copyOf(parameterModels);
  }

  @Override
  public void validateSerialized(
      PageParameters parameters, LinkParameterValidationErrorCollector collector) {
    for (String parameterName : parameterNames) {
      if (parameters.get(parameterName).isNull()) {
        collector.addError(String.format("Mandatory parameter '%s' was missing.", parameterName));
      }
    }
  }

  @Override
  public void validateModel(LinkParameterValidationErrorCollector collector) {
    for (IModel<? extends Collection> parameterModel : parameterModels) {
      Collection<?> object = parameterModel.getObject();
      if (object == null || object.isEmpty()) {
        collector.addError(
            String.format(
                "A mandatory parameter model object for parameter names '%s' was null or empty.",
                parameterNames));
      }
    }
  }

  @Override
  public void detach() {
    for (IModel<?> parameterModel : parameterModels) {
      parameterModel.detach();
    }
  }
}
