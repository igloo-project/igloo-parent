package org.iglooproject.wicket.more.markup.html.select2;

import igloo.wicket.model.Detachables;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.model.ChoicesWrapperModel;
import org.iglooproject.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.wicketstuff.select2.Select2Behavior;
import org.wicketstuff.select2.Settings;

public class GenericSelect2DropDownSingleChoice<T> extends DropDownChoice<T> {

  private static final long serialVersionUID = -3776700270762640109L;

  private ChoicesWrapperModel<T> choicesWrapperModel;

  private final Select2Behavior select2Behavior;

  protected GenericSelect2DropDownSingleChoice(
      String id,
      IModel<T> model,
      IModel<? extends Collection<? extends T>> choicesModel,
      IChoiceRenderer<? super T> renderer) {
    super(id);

    setModel(model);
    choicesWrapperModel = new ChoicesWrapperModel<>(model, choicesModel);
    setChoices(choicesWrapperModel);
    setChoiceRenderer(renderer);
    setNullValid(true);

    select2Behavior = Select2Behavior.forSingleChoice();
    fillSelect2Settings(select2Behavior.getSettings());
    add(select2Behavior);
  }

  @SuppressWarnings("unchecked")
  protected void ensureChoicesModelIsWrapped() {
    /*
     * Ideally this wrapping should be done in setChoices(IModel) or in wrap(IModel),
     * but those methods cannot be overriden...
     */
    IModel<? extends List<? extends T>> choicesModel = getChoicesModel();
    if (choicesModel != choicesWrapperModel) {
      if (choicesModel instanceof ChoicesWrapperModel) {
        this.choicesWrapperModel = (ChoicesWrapperModel<T>) choicesModel;
      } else {
        this.choicesWrapperModel = new ChoicesWrapperModel<>(getModel(), choicesModel);
        setChoices(choicesWrapperModel);
      }
    }
  }

  @Override
  protected void onConfigure() {
    ensureChoicesModelIsWrapped();

    super.onConfigure();

    if (isRequired()) {
      Select2Utils.setRequiredSettings(getSettings());
    } else {
      Select2Utils.setOptionalSettings(getSettings());
    }
  }

  protected void fillSelect2Settings(Settings settings) {
    Select2Utils.setDefaultSettings(settings);
  }

  protected final Settings getSettings() {
    return select2Behavior.getSettings();
  }

  public GenericSelect2DropDownSingleChoice<T> setWidth(IDropDownChoiceWidth width) {
    getSettings().setWidth(width.getWidth());
    return this;
  }

  protected String getRootKey() {
    return GenericSelect2DropDownSingleChoice.class.getSimpleName() + "." + this.getId();
  }

  @Override
  protected String getNullKey() {
    return getRootKey() + ".null";
  }

  @Override
  protected String getNullValidKey() {
    return getRootKey() + ".nullValid";
  }

  public boolean isSelectedObjectForcedInChoices() {
    return choicesWrapperModel.isSelectedObjectForcedInChoices();
  }

  public GenericSelect2DropDownSingleChoice<T> setSelectedObjectForcedInChoices(
      boolean selectedObjectForcedInChoices) {
    choicesWrapperModel.setSelectedObjectForcedInChoices(selectedObjectForcedInChoices);
    return this;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(choicesWrapperModel);
  }
}
