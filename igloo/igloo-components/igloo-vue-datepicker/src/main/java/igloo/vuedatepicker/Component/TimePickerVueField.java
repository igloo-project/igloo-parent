package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsDatePicker;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;

public class TimePickerVueField extends AbstractDatePickerVueField<LocalTime> {

  private static final long serialVersionUID = 1L;

  public TimePickerVueField(String id, IModel<LocalTime> model) {
    this(id, model, builder -> {});
  }

  public TimePickerVueField(
      String id, IModel<LocalTime> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] values = getInputAsArray();
    if (ArrayUtils.isNotEmpty(values)) {
      String value = values[0];
      setConvertedInput(StringUtils.isBlank(value) ? null : LocalTime.parse(value, getFormat()));
    }
  }

  @Override
  protected DateTimeFormatter getFormat() {
    IJsDatePicker jsDatePicker = getVueBehavior().getJsDatePicker();
    return DateTimeFormatter.ofPattern(
        (jsDatePicker == null || jsDatePicker.format() == null)
            ? "HH:mm"
            : jsDatePicker.format().render().replace("\"", ""));
  }

  @Override
  protected JsDatePicker.Builder initDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.ofLocalTimeModel(getModel()))
        .timePicker(JsHelpers.of(true))
        .textInput(JsHelpers.of(true))
        // TODO RFO faire une clef de conf ? Override dans projet pour recuperer clef de trad ?
        .placeholder(JsHelpers.of("hh:mm"))
        .onUpdateModel(JsHelpers.ofLiteral("value => console.log(value)"))
        .ui(JsHelpers.ofLiteral("{ input: 'form-control' }"));
  }
}
