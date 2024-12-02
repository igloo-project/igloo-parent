package igloo.vuedatepicker.Component;

import igloo.vuedatepicker.behavior.IJsDatePicker;
import igloo.vuedatepicker.behavior.JsDatePicker;
import igloo.vuedatepicker.behavior.VueBehavior;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;

public abstract class AbstractDatePickerVueComponent<T> extends AbstractTextComponent<T> {

  private static final long serialVersionUID = -1;

  private final VueBehavior vueBehavior;

  public AbstractDatePickerVueComponent(
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
}
