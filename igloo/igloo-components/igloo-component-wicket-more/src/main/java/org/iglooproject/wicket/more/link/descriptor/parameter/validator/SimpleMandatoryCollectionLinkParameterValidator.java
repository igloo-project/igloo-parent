package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
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
    this(ImmutableList.of(parameterName), ImmutableList.of(parameterModel));
  }

  public SimpleMandatoryCollectionLinkParameterValidator(
      Collection<String> parameterNames,
      Collection<? extends IModel<? extends Collection>> parameterModels) {
    Args.notNull(parameterNames, "parameterNames");
    Args.notNull(parameterModels, "parameterModels");
    this.parameterNames = ImmutableList.copyOf(parameterNames);
    this.parameterModels = ImmutableList.copyOf(parameterModels);
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
