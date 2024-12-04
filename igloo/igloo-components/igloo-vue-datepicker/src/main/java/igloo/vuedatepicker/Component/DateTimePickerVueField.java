package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;

public class DateTimePickerVueField extends AbstractDatePickerVueField<LocalDateTime> {

  private static final long serialVersionUID = 1L;

  public DateTimePickerVueField(String id, IModel<LocalDateTime> model) {
    this(id, model, builder -> {});
  }

  public DateTimePickerVueField(
      String id, IModel<LocalDateTime> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] values = getInputAsArray();
    if (ArrayUtils.isNotEmpty(values)) {
      String value = values[0];
      setConvertedInput(
          StringUtils.isBlank(value) ? null : LocalDateTime.parse(value, getFormat()));
    }
  }

  @Override
  protected JsDatePicker.Builder getDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.ofLocalDateTimeModel(getModel()))
        .textInput(JsHelpers.of(true))
        .onUpdateModel(JsHelpers.ofLiteral("value => console.log(value)"));
  }
}
