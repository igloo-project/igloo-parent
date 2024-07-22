package org.iglooproject.wicket.more.markup.html.model;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

public class MultipleChoicesWrapperModel<T> implements IModel<List<T>> {

  private static final long serialVersionUID = -8579290778036203349L;

  private boolean selectedObjectForcedInChoices = false;

  private IModel<? extends Collection<? extends T>> selectedObjectCollectionModel;

  private IModel<? extends Collection<? extends T>> baseChoicesModel;

  public MultipleChoicesWrapperModel(
      IModel<? extends Collection<? extends T>> selectedObjectCollectionModel,
      IModel<? extends Collection<? extends T>> baseChoicesModel) {
    Args.notNull(selectedObjectCollectionModel, "selectedObjectCollectionModel");
    Args.notNull(baseChoicesModel, "baseChoicesModel");
    this.selectedObjectCollectionModel = selectedObjectCollectionModel;
    this.baseChoicesModel = baseChoicesModel;
  }

  @Override
  public List<T> getObject() {
    List<T> choices = Lists.newArrayList();

    Collection<? extends T> baseChoices = baseChoicesModel.getObject();
    Collection<? extends T> selectedObjectCollection = selectedObjectCollectionModel.getObject();

    // Si on force à inclure les objets sélectionnés et qu'ils ne sont pas dans la liste de choix,
    // on les inclut en premiers dans la liste.
    if (selectedObjectForcedInChoices && selectedObjectCollection != null && baseChoices != null) {
      for (T selectedObject : selectedObjectCollection) {
        if (!baseChoices.contains(selectedObject)) {
          choices.add(selectedObject);
        }
      }
    }
    if (baseChoices != null) {
      choices.addAll(baseChoices);
    }

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
    selectedObjectCollectionModel.detach();
    baseChoicesModel.detach();
  }
}
