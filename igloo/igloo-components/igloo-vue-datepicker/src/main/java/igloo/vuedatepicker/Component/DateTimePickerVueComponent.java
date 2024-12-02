package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.model.IModel;

public class DateTimePickerVueComponent extends AbstractDatePickerVueComponent<LocalDateTime> {

  private static final long serialVersionUID = 1L;

  public DateTimePickerVueComponent(String id, IModel<LocalDateTime> model) {
    this(id, model, builder -> {});
  }

  public DateTimePickerVueComponent(
      String id, IModel<LocalDateTime> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] value = getInputAsArray();
    if (ArrayUtils.isNotEmpty(value)) {
      setConvertedInput(LocalDateTime.parse(value[0], getFormat()));
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
