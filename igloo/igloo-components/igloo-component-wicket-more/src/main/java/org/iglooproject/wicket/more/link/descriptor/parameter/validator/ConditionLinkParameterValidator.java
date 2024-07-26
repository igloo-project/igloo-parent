package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.model.BindingModel;
import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.AbstractLinkParameterValidatorFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import org.javatuples.Tuple;
import org.javatuples.Unit;

public class ConditionLinkParameterValidator implements ILinkParameterValidator {

  private static final long serialVersionUID = -6678335084190190566L;

  private final Condition condition;

  public static <T extends Tuple> ILinkParameterValidatorFactory<T> fromConditionFactory(
      final IDetachableFactory<T, ? extends Condition> conditionFactory) {
    Args.notNull(conditionFactory, "conditionFactory");
    return new AbstractLinkParameterValidatorFactory<T>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterValidator create(T parameters) {
        return new ConditionLinkParameterValidator(conditionFactory.create(parameters));
      }

      @Override
      public void detach() {
        super.detach();
        conditionFactory.detach();
      }
    };
  }

  public static <R> ILinkParameterValidatorFactory<Unit<IModel<? extends R>>> predicateFactory(
      final SerializablePredicate2<? super R> predicate) {
    Args.notNull(predicate, "predicate");
    return new AbstractLinkParameterValidatorFactory<Unit<IModel<? extends R>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterValidator create(Unit<IModel<? extends R>> parameters) {
        return new ConditionLinkParameterValidator(
            Condition.predicate(parameters.getValue0(), predicate));
      }
    };
  }

  public static <R> ILinkParameterValidatorFactory<Unit<IModel<? extends R>>> anyPermissionFactory(
      final Collection<String> permissions) {
    Args.notNull(permissions, "permissions");
    return new AbstractLinkParameterValidatorFactory<Unit<IModel<? extends R>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterValidator create(Unit<IModel<? extends R>> parameters) {
        return new ConditionLinkParameterValidator(
            Condition.anyPermission(parameters.getValue0(), permissions));
      }
    };
  }

  public static <R, T>
      ILinkParameterValidatorFactory<Unit<IModel<? extends R>>> anyPermissionFactory(
          final BindingRoot<R, T> bindingRoot, final Collection<String> permissions) {
    Args.notNull(bindingRoot, "bindingRoot");
    Args.notNull(permissions, "permissions");
    return new AbstractLinkParameterValidatorFactory<Unit<IModel<? extends R>>>() {
      private static final long serialVersionUID = 1L;

      @Override
      public ILinkParameterValidator create(Unit<IModel<? extends R>> parameters) {
        return new ConditionLinkParameterValidator(
            Condition.anyPermission(
                BindingModel.of(parameters.getValue0(), bindingRoot), permissions));
      }
    };
  }

  public ConditionLinkParameterValidator(Condition condition) {
    this.condition = condition;
  }

  @Override
  public void validateSerialized(
      PageParameters parameters, LinkParameterValidationErrorCollector collector) {
    // Nothing to do
  }

  @Override
  public void validateModel(LinkParameterValidationErrorCollector collector) {
    if (!condition.applies()) {
      collector.addError(String.format("Condition '%s' was false.", condition));
    }
  }

  @Override
  public void detach() {
    condition.detach();
  }
}
