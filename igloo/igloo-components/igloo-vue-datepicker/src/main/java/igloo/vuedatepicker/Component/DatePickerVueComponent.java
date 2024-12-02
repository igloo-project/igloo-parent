package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.LocalDate;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.model.IModel;

public class DatePickerVueComponent extends AbstractDatePickerVueComponent<LocalDate> {

  private static final long serialVersionUID = 1L;

  public DatePickerVueComponent(String id, IModel<LocalDate> model) {
    this(id, model, builder -> {});
  }

  public DatePickerVueComponent(
      String id, IModel<LocalDate> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] value = getInputAsArray();
    if (ArrayUtils.isNotEmpty(value)) {
      setConvertedInput(LocalDate.parse(value[0], getFormat()));
    }
  }

  @Override
  protected JsDatePicker.Builder getDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.ofLocalDateModel(getModel()))
        .autoApply(JsHelpers.of(true))
        .enableTimePicker(JsHelpers.of(false))
        .format(JsHelpers.of("dd/MM/yyyy"))
        .textInput(JsHelpers.of(true));
  }
}
