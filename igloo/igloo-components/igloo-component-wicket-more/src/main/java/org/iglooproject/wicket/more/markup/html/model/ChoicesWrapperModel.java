package org.iglooproject.wicket.more.markup.html.model;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

public class ChoicesWrapperModel<T> implements IModel<List<T>> {

  private static final long serialVersionUID = -8579290778036203349L;

  private boolean selectedObjectForcedInChoices = true;

  private IModel<T> selectedObjectModel;

  private IModel<? extends Collection<? extends T>> baseChoicesModel;

  public ChoicesWrapperModel(
      IModel<T> selectedObjectModel, IModel<? extends Collection<? extends T>> baseChoicesModel) {
    Args.notNull(selectedObjectModel, "selectedObjectModel");
    Args.notNull(baseChoicesModel, "baseChoicesModel");
    this.selectedObjectModel = selectedObjectModel;
    this.baseChoicesModel = baseChoicesModel;
  }

  @Override
  public List<T> getObject() {
    List<T> choices = Lists.newArrayList();

    Collection<? extends T> baseChoices = baseChoicesModel.getObject();
    T selectedObject = selectedObjectModel.getObject();

    // Si on force à inclure l'objet sélectionné et qu'il n'est pas dans la liste de choix,
    // on l'inclut en premier dans la liste.
    if (selectedObjectForcedInChoices
        && selectedObject != null
        && !baseChoices.contains(selectedObject)) {
      choices.add(selectedObject);
    }
    choices.addAll(baseChoices);

    return choices;
  }

  public boolean isSelectedObjectForcedInChoices() {
    return selectedObjectForcedInChoices;
  }

  public void setSelectedObjectForcedInChoices(boolean selectedObjectForcedInChoices) {
    this.selectedObjectForcedInChoices = selectedObjectForcedInChoices;
  }

  @Override
  public void detach() {
    IModel.super.detach();
    selectedObjectModel.detach();
    baseChoicesModel.detach();
  }
}
