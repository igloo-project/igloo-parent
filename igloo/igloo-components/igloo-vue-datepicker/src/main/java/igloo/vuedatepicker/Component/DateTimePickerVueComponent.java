package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsDatePicker;
import igloo.vuedatepicker.behavior.JsDatePicker;
import igloo.vuedatepicker.behavior.VueBehavior;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;

public class DateTimePickerVueComponent extends AbstractTextComponent<LocalDateTime> {

  private static final long serialVersionUID = 1L;

  private final VueBehavior vueBehavior;

  public DateTimePickerVueComponent(String id, IModel<LocalDateTime> model) {
    this(id, model, builder -> {});
  }

  public DateTimePickerVueComponent(
      String id, IModel<LocalDateTime> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model);
    setType(Date.class);

    JsDatePicker.Builder builder =
        JsDatePicker.builder()
            .dateModel(JsHelpers.ofLocalDateTimeModel(getModel()))
            .textInput(JsHelpers.of(true))
            .onUpdateModel(JsHelpers.ofLiteral("value => console.log(value)"));
    jsDatePickerConsumer.accept(builder);

    vueBehavior = new VueBehavior(builder.build());
    add(vueBehavior);
  }

  @Override
  public void convertInput() {
    IJsDatePicker jsDatePicker = vueBehavior.getJsDatePicker();
    String[] value = getInputAsArray();
    if (ArrayUtils.isNotEmpty(value)) {
      DateTimeFormatter format =
          DateTimeFormatter.ofPattern(
              (jsDatePicker == null || jsDatePicker.format() == null)
                  ? "MM/dd/yyyy, HH:mm"
                  : jsDatePicker.format().render().replace("\"", ""));

      setConvertedInput(LocalDateTime.parse(value[0], format));
    }
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    // Must be attached to an vue-date-picker tag
    checkComponentTag(tag, "vue-date-picker");

    // Default handling for component tag
    super.onComponentTag(tag);
  }

  public String getVModelVarName() {
    return vueBehavior.getVmodelVarName(this);
  }

  public String getVueOnChangeVarName() {
    return vueBehavior.getVueOnChangeVarName(this);
  }
}
