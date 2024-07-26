package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * A form that is submitted independently from its parent form: it is only submitted explicitly, and
 * not when its parent form is submitted.
 */
public class IndependentNestedForm<T> extends Form<T> {

  private static final long serialVersionUID = 5851982240353651488L;

  public IndependentNestedForm(String id, IModel<T> model) {
    super(id, model);
  }

  public IndependentNestedForm(String id) {
    super(id);
  }

  @Override
  protected boolean wantSubmitOnParentFormSubmit() {
    return false;
  }

  @Override
  public Form<?> getRootForm() {
    // Makes sure that only this form will be submitted when using AjaxFormSubmitBehavior (for
    // instance).
    // Wicket's default behavior would be to submit the whole root form, but only take this subform
    // into account (don't know why).
    return this;
  }
}
