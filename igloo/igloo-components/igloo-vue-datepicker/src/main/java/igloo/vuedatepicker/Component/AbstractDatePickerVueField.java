package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsDatePicker;
import igloo.vuedatepicker.behavior.JsDatePicker;
import igloo.vuedatepicker.behavior.VueBehavior;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.form.IVueComponent;

public abstract class AbstractDatePickerVueField<T> extends AbstractTextComponent<T>
    implements IVueComponent {

  private static final long serialVersionUID = -1;

  private final VueBehavior vueBehavior;

  public AbstractDatePickerVueField(
      String id, IModel<T> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model);
    JsDatePicker.Builder builder = getDefaultJsDatePickerBuilder();
    jsDatePickerConsumer.accept(builder);
    vueBehavior = new VueBehavior(builder.build());
    add(vueBehavior);
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    // Must be attached to an vue-date-picker tag
    checkComponentTag(tag, "vue-date-picker");

    // Default handling for component tag
    super.onComponentTag(tag);
  }

  protected DateTimeFormatter getFormat() {
    IJsDatePicker jsDatePicker = vueBehavior.getJsDatePicker();
    return DateTimeFormatter.ofPattern(
        (jsDatePicker == null || jsDatePicker.format() == null)
            ? "MM/dd/yyyy, HH:mm"
            : jsDatePicker.format().render().replace("\"", ""));
  }

  protected abstract JsDatePicker.Builder getDefaultJsDatePickerBuilder();

  protected VueBehavior getVueBehavior() {
    return vueBehavior;
  }

  public String getVModelVarName() {
    return vueBehavior.getVmodelVarName(this);
  }

  public String getVueOnChangeVarName() {
    return vueBehavior.getVueOnChangeVarName(this);
  }

  public static Consumer<JsDatePicker.Builder> dateMinConsumer(
      AbstractDatePickerVueField<?> datePicker) {
    return builder -> builder.minDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName()));
  }

  public static Consumer<JsDatePicker.Builder> dateMaxConsumer(
      AbstractDatePickerVueField<?> datePicker) {
    return builder -> builder.maxDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName()));
  }

  @Override
  public void appendCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "vueInit.appendInputCssClass('%s', '%s')".formatted(getMarkupId(), cssClass));
  }

  @Override
  public void removeCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "vueInit.removeInputCssClass('%s', '%s')".formatted(getMarkupId(), cssClass));
  }
}
