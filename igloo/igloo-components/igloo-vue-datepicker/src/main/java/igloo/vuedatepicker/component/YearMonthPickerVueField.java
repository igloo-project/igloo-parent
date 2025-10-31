package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import java.time.DateTimeException;
import java.time.YearMonth;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class YearMonthPickerVueField extends AbstractDatePickerVueField<YearMonth> {

  private static final long serialVersionUID = 1L;

  public YearMonthPickerVueField(String id, IModel<YearMonth> model) {
    this(id, model, builder -> {});
  }

  public YearMonthPickerVueField(
      String id, IModel<YearMonth> model, SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(id, model, jsVueDatePickerConsumer);
  }

  @Override
  protected YearMonth convertValue(String[] value) throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }
      return YearMonth.parse(trim(value[0]), getFormat());
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.yearMonth")
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
                    .model(JsHelpers.ofYearMonthModel(getModel()))
                    // Modes
                    .monthPicker(JsHelpers.of(true))
                    // General configuration
                    .autoApply(JsHelpers.of(true))
                    .placeholder(JsHelpers.of(getString("datetime.pattern.yearMonth.placeholder")))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.yearMonth")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.yearMonth.placeholder"))));
  }
}
