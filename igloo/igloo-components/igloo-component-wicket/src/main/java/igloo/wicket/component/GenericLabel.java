package igloo.wicket.component;

import org.apache.wicket.model.IModel;

public class GenericLabel<E> extends AbstractGenericLabel<E, GenericLabel<E>> {

  private static final long serialVersionUID = -6956425366331256600L;

  public GenericLabel(String id, IModel<E> model) {
    super(id, model);
  }

  @Override
  protected GenericLabel<E> thisAsT() {
    return this;
  }
}
