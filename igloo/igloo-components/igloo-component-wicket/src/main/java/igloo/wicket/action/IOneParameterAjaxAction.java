package igloo.wicket.action;

import igloo.wicket.condition.Condition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface IOneParameterAjaxAction<T> extends IDetachable {

  void execute(AjaxRequestTarget target, T parameter);

  default void updateAjaxAttributes(AjaxRequestAttributes attributes, T model) {
    // nothing to do
  }

  default Condition getActionAvailableCondition(T parameter) {
    return Condition.alwaysTrue();
  }

  @Override
  default void detach() {
    // nothing to do
  }
}
