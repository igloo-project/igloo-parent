package org.iglooproject.wicket.more.markup.html.form.impl;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.model.IModel;

public class ComponentChainingModel<T> implements IModel<T> {

  private static final long serialVersionUID = -2756357374251245085L;

  private final IGenericComponent<T, ? extends IGenericComponent<? super T, ?>> component;

  public ComponentChainingModel(
      IGenericComponent<T, ? extends IGenericComponent<? super T, ?>> component) {
    super();
    this.component = component;
  }

  private IModel<T> getChainedModel() {
    return component.getModel();
  }

  @Override
  public T getObject() {
    return getChainedModel().getObject();
  }

  @Override
  public void setObject(T object) {
    getChainedModel().setObject(object);
  }

  @Override
  public void detach() {
    getChainedModel().detach();
  }
}
