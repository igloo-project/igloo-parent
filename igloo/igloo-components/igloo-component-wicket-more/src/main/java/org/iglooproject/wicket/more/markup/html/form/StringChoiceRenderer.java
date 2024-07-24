package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class StringChoiceRenderer extends ChoiceRenderer<String> {
  private static final long serialVersionUID = 1L;

  @Override
  public Object getDisplayValue(String object) {
    return object;
  }

  @Override
  public String getIdValue(String object, int index) {
    return object;
  }
}
