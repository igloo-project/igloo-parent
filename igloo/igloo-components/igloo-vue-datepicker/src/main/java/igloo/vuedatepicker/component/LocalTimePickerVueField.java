package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import java.time.DateTimeException;
import java.time.LocalTime;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class LocalTimePickerVueField extends AbstractDatePickerVueField<LocalTime> {

  private static final long serialVersionUID = 1L;

  public LocalTimePickerVueField(String id, IModel<LocalTime> model) {
    this(id, model, builder -> {});
  }

  public LocalTimePickerVueField(
      String id, IModel<LocalTime> model, SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(id, model, jsVueDatePickerConsumer);
  }

  @Override
  protected LocalTime convertValue(String[] value) throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }
      return LocalTime.parse(trim(value[0]), getFormat());
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.localTime")
          .setSourceValue(value)
          .setLocale(getLocale());
    }
  }

  @Override
  protected SerializableConsumer<Builder> initDefaultJsVueDatePickerBuilder() {
    return super.initDefaultJsVueDatePickerBuilder()
        .andThen(
            builder ->
                builder
                    .model(JsHelpers.ofLocalTimeModel(getModel()))
                    // Modes
                    .timePicker(JsHelpers.of(true))
                    // General configuration
                    .placeholder(JsHelpers.of(getString("datetime.pattern.time.placeholder")))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.time")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.time.placeholder"))));
  }
}
