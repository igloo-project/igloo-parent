package org.iglooproject.wicket.more.markup.html.select2;

import igloo.wicket.model.ConcreteCollectionToCollectionWrapperModel;
import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Select2MultiChoice;
import org.wicketstuff.select2.Settings;

public class GenericSelect2AjaxDropDownMultipleChoice<T> extends Select2MultiChoice<T> {

  private static final long serialVersionUID = 6355575209286187233L;

  protected <C extends Collection<T>> GenericSelect2AjaxDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      ChoiceProvider<T> choiceProvider) {
    super(
        id,
        new ConcreteCollectionToCollectionWrapperModel<T, C>(model, collectionSupplier),
        choiceProvider);

    fillSelect2Settings(getSettings());
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();

    if (isRequired()) {
      Select2Utils.setRequiredSettings(getSettings());
    } else {
      Select2Utils.setOptionalSettings(getSettings());
    }
  }

  protected void fillSelect2Settings(Settings settings) {
    Select2Utils.setDefaultAjaxSettings(settings);
  }

  public GenericSelect2AjaxDropDownMultipleChoice<T> setWidth(IDropDownChoiceWidth width) {
    getSettings().setWidth(width.getWidth());
    return this;
  }
}
