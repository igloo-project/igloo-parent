package org.iglooproject.wicket.more.markup.html.select2;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public abstract class AbstractChoiceRendererChoiceProvider<T> extends ChoiceProvider<T> {

  private static final long serialVersionUID = -4654743240914954744L;

  protected final IChoiceRenderer<? super T> choiceRenderer;

  public AbstractChoiceRendererChoiceProvider(IChoiceRenderer<? super T> choiceRenderer) {
    super();
    Injector.get().inject(this);
    this.choiceRenderer = choiceRenderer;
  }

  @Override
  public String getIdValue(T object) {
    return choiceRenderer.getIdValue(object, 0 /* unused */);
  }

  @Override
  public String getDisplayValue(T object) {
    return (String) choiceRenderer.getDisplayValue(object);
  }
}
