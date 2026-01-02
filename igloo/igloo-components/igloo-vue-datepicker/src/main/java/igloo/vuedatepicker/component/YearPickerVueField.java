package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import java.time.DateTimeException;
import java.time.Year;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class YearPickerVueField extends AbstractDatePickerVueField<Year> {

  private static final long serialVersionUID = 1L;

  public YearPickerVueField(String id, IModel<Year> model) {
    this(id, model, builder -> {});
  }

  public YearPickerVueField(
      String id, IModel<Year> model, SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(id, model, jsVueDatePickerConsumer);
  }

  @Override
  protected Year convertValue(String[] value) throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }
      return Year.parse(trim(value[0]), getFormat());
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.year")
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
                    .model(JsHelpers.ofYearModel(getModel()))
                    // Modes
                    .yearPicker(JsHelpers.of(true))
                    // General configuration
                    .autoApply(JsHelpers.of(true))
                    .placeholder(JsHelpers.of(getString("datetime.pattern.year.placeholder")))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.year")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.year.placeholder"))));
  }
}
