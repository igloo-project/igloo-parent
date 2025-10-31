package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class LocalDateTimePickerVueField extends AbstractDatePickerVueField<LocalDateTime> {

  private static final long serialVersionUID = 1L;

  public LocalDateTimePickerVueField(String id, IModel<LocalDateTime> model) {
    this(id, model, builder -> {});
  }

  public LocalDateTimePickerVueField(
      String id,
      IModel<LocalDateTime> model,
      SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(id, model, jsVueDatePickerConsumer);
  }

  @Override
  protected LocalDateTime convertValue(String[] value) throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }
      return LocalDateTime.parse(trim(value[0]), getFormat());
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.localDateTime")
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
                    .model(JsHelpers.ofLocalDateTimeModel(getModel()))
                    // General configuration
                    .placeholder(JsHelpers.of(getString("datetime.pattern.dateTime.placeholder")))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.dateTime")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.dateTime.placeholder"))));
  }
}
