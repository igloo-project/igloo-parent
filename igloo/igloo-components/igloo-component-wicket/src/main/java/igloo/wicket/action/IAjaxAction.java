package igloo.wicket.action;

import igloo.wicket.condition.Condition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface IAjaxAction extends IDetachable {

  void execute(AjaxRequestTarget target);

  default void updateAjaxAttributes(AjaxRequestAttributes attributes) {
    // nothing to do
  }

  default Condition getActionAvailableCondition() {
    return Condition.alwaysTrue();
  }

  @Override
  default void detach() {
    // nothing to do
  }
}
