package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import java.time.DateTimeException;
import java.time.LocalDate;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class LocalDatePickerVueField extends AbstractDatePickerVueField<LocalDate> {

  private static final long serialVersionUID = 1L;

  public LocalDatePickerVueField(String id, IModel<LocalDate> model) {
    this(id, model, builder -> {});
  }

  public LocalDatePickerVueField(
      String id, IModel<LocalDate> model, SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(id, model, jsVueDatePickerConsumer);
  }

  @Override
  protected LocalDate convertValue(String[] value) throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }
      return LocalDate.parse(trim(value[0]), getFormat());
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.localDate")
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
                    .model(JsHelpers.ofLocalDateModel(getModel()))
                    // General configuration
                    .autoApply(JsHelpers.of(true))
                    .placeholder(JsHelpers.of(getString("datetime.pattern.date.placeholder")))
                    // Time picker configuration
                    .enableTimePicker(JsHelpers.of(false))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.date")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.date.placeholder"))));
  }
}
