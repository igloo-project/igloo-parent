package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import igloo.wicket.model.Detachables;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.function.Failable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.spring.util.StringUtils;
import org.javatuples.Pair;

public class LocalDateRangePickerVueField
    extends AbstractDatePickerVueField<Pair<LocalDate, LocalDate>> {

  private static final long serialVersionUID = 1L;

  public LocalDateRangePickerVueField(
      String id, IModel<LocalDate> dateMinModel, IModel<LocalDate> dateMaxModel) {
    this(id, dateMinModel, dateMaxModel, builder -> {});
  }

  public LocalDateRangePickerVueField(
      String id,
      IModel<LocalDate> dateMinModel,
      IModel<LocalDate> dateMaxModel,
      SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(
        id,
        new Model<>() {
          private static final long serialVersionUID = 1L;

          @Override
          public Pair<LocalDate, LocalDate> getObject() {
            LocalDate dateMin = dateMinModel.getObject();
            LocalDate dateMax = dateMaxModel.getObject();
            return (dateMin != null && dateMax != null) ? Pair.with(dateMin, dateMax) : null;
          }

          @Override
          public void setObject(Pair<LocalDate, LocalDate> object) {
            if (object == null) {
              return;
            }
            dateMinModel.setObject(object.getValue0());
            dateMaxModel.setObject(object.getValue1());
          }

          @Override
          public void detach() {
            super.detach();
            Detachables.detach(dateMinModel, dateMaxModel);
          }
        },
        jsVueDatePickerConsumer);
  }

  @Override
  protected Pair<LocalDate, LocalDate> convertValue(String[] value) throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }

      List<LocalDate> dates =
          Stream.of(trim(value[0]).replaceAll("[\\[\\]]", "").split(" - ", -1))
              .map(this::trim)
              .filter(StringUtils::hasText)
              .map(Failable.asFunction(dateInput -> LocalDate.parse(dateInput, getFormat())))
              .toList();

      if (dates.size() != 2) {
        throw new DateTimeException("2 dates required for range");
      }

      return Pair.fromCollection(dates);
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.localDate.range")
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
                    .model(JsHelpers.ofLocalDatePair(getModel()))
                    // Modes
                    .range(JsHelpers.ofLiteral("{ partialRange: false }"))
                    // General configuration
                    .autoApply(JsHelpers.of(true))
                    .placeholder(JsHelpers.of(getString("datetime.pattern.date.range.placeholder")))
                    // Time picker configuration
                    .enableTimePicker(JsHelpers.of(false))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.date")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.date.range.placeholder"))));
  }
}
