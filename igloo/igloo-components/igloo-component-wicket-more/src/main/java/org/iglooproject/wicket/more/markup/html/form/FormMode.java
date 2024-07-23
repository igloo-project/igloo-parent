package org.iglooproject.wicket.more.markup.html.form;

import igloo.wicket.condition.Condition;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public enum FormMode {
  ADD,
  EDIT;

  public Condition condition(IModel<FormMode> formModeModel) {
    return Condition.isEqual(Model.of(this), formModeModel);
  }
}
