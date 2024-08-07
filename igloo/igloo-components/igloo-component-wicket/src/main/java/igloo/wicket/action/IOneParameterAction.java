package igloo.wicket.action;

import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface IOneParameterAction<T> extends IDetachable {

  public void execute(T parameter);

  @Override
  default void detach() {
    // nothing to do
  }
}
