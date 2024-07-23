package org.iglooproject.wicket.more.markup.html.form.validation;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.IDetachable;

public interface IFormModelValidator extends IDetachable {

  /**
   * @return array of {@link FormComponent}s that this validator depends on
   */
  FormComponent<?>[] getDependentFormComponents();

  /**
   * This method is ran if all components returned by {@link
   * IFormValidator#getDependentFormComponents()} are valid.
   *
   * <p>To report validation error use {@link
   * FormComponent#error(org.apache.wicket.validation.IValidationError)} by using any of the
   * dependent form components or extend from AbstractFormValidator and use its {@link
   * AbstractFormValidator#error(FormComponent, String, java.util.Map)} method.
   *
   * @param form form this validator is added to
   */
  void validate(Form<?> form);
}
