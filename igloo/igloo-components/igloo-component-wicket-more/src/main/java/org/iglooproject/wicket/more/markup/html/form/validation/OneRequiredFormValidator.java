package org.iglooproject.wicket.more.markup.html.form.validation;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import java.util.Collection;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.util.string.Strings;

public class OneRequiredFormValidator extends AbstractFormValidator {

  private static final long serialVersionUID = -7019352590059451557L;

  protected Collection<FormComponent<?>> requiredFormComponents;
  protected OneRequiredMode mode = OneRequiredMode.ONE_OR_MORE;

  protected OneRequiredFormValidator() {
    // Permet d'hériter de ce validator pour des cas spécifiques quand on ne dispose pas des champs
    // tout de suite.
  }

  public OneRequiredFormValidator(
      FormComponent<?> first, FormComponent<?> second, FormComponent<?>... rest) {
    this.requiredFormComponents =
        ImmutableList.<FormComponent<?>>builder().add(first, second).add(rest).build();
  }

  public OneRequiredFormValidator setMode(OneRequiredMode mode) {
    this.mode = mode;
    return this;
  }

  @Override
  public FormComponent<?>[] getDependentFormComponents() {
    return Iterables.toArray(
        Iterables.concat(requiredFormComponents, getAdditionalDependentFormComponents()),
        FormComponent.class);
  }

  protected Iterable<? extends FormComponent<?>> getAdditionalDependentFormComponents() {
    return ImmutableList.of();
  }

  @Override
  public void validate(Form<?> form) {
    if (isEnabled(form)) {
      switch (mode) {
        case ONE_ONLY:
          boolean oneFilled = false;
          for (FormComponent<?> formComponent : requiredFormComponents) {
            if (checkRequired(formComponent)) {
              if (oneFilled) {
                onError(form);
                break;
              } else {
                oneFilled = true;
              }
            }
          }
          if (!oneFilled) {
            onError(form);
          }
          break;
        case ONE_OR_MORE:
          for (FormComponent<?> formComponent : requiredFormComponents) {
            if (checkRequired(formComponent)) {
              return;
            }
          }
          onError(form);
          break;
      }
    }
  }

  protected void onError(Form<?> form) {
    Joiner labelJoiner = Joiner.on(form.getString("common.validator.oneRequired.labels.separator"));
    String labels =
        labelJoiner.join(
            Iterables.transform(requiredFormComponents, input -> input.getLabel().getObject()));

    if (mode == OneRequiredMode.ONE_ONLY) {
      error(
          requiredFormComponents.iterator().next(),
          "common.validator.oneRequired.oneOnly",
          ImmutableMap.<String, Object>of("labels", labels));
    } else if (mode == OneRequiredMode.ONE_OR_MORE) {
      error(
          requiredFormComponents.iterator().next(),
          "common.validator.oneRequired.oneOrMore",
          ImmutableMap.<String, Object>of("labels", labels));
    } else {
      error(requiredFormComponents.iterator().next(), "common.error.unexpected");
    }
  }

  /**
   * Code copied from FormComponent#checkRequired, which is unfortunately useless as long as
   * FormComponent#required is false.
   */
  protected boolean checkRequired(FormComponent<?> formComponent) {
    final String input = formComponent.getInput();

    // when null, check whether this is natural for that component, or
    // whether - as is the case with text fields - this can only happen
    // when the component was disabled
    if (input == null
        && !formComponent.isInputNullable()
        && !formComponent.isEnabledInHierarchy()) {
      // this value must have come from a disabled field
      // do not perform validation
      return true;
    }

    // peform validation by looking whether the value is null or empty
    return !Strings.isEmpty(input);
  }

  public enum OneRequiredMode {
    ONE_OR_MORE,
    ONE_ONLY
  }
}
