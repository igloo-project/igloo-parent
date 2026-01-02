package igloo.vuedatepicker.component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import igloo.wicket.model.Detachables;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.function.Failable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.spring.util.StringUtils;
import org.javatuples.Pair;

public class LocalDateTimeRangePickerVueField
    extends AbstractDatePickerVueField<Pair<LocalDateTime, LocalDateTime>> {

  private static final long serialVersionUID = 1L;

  public LocalDateTimeRangePickerVueField(
      String id, IModel<LocalDateTime> dateMinModel, IModel<LocalDateTime> dateMaxModel) {
    this(id, dateMinModel, dateMaxModel, builder -> {});
  }

  public LocalDateTimeRangePickerVueField(
      String id,
      IModel<LocalDateTime> dateMinModel,
      IModel<LocalDateTime> dateMaxModel,
      SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(
        id,
        new Model<>() {
          private static final long serialVersionUID = 1L;

          @Override
          public Pair<LocalDateTime, LocalDateTime> getObject() {
            LocalDateTime dateMin = dateMinModel.getObject();
            LocalDateTime dateMax = dateMaxModel.getObject();
            return (dateMin != null && dateMax != null) ? Pair.with(dateMin, dateMax) : null;
          }

          @Override
          public void setObject(Pair<LocalDateTime, LocalDateTime> object) {
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
  protected Pair<LocalDateTime, LocalDateTime> convertValue(String[] value)
      throws ConversionException {
    try {
      if (value == null || value.length == 0 || value[0] == null) {
        return null;
      }

      List<LocalDateTime> dates =
          Stream.of(trim(value[0]).replaceAll("[\\[\\]]", "").split(" - ", -1))
              .map(this::trim)
              .filter(StringUtils::hasText)
              .map(Failable.asFunction(dateInput -> LocalDateTime.parse(dateInput, getFormat())))
              .toList();

      if (dates.size() != 2) {
        throw new DateTimeException("2 dates with time required for range");
      }

      return Pair.fromCollection(dates);
    } catch (DateTimeException e) {
      throw new ConversionException("Cannot parse '" + value)
          .setResourceKey("conversion.error.localDateTime.range")
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
                    .model(JsHelpers.ofLocalDateTimePair(getModel()))
                    // Modes
                    .range(JsHelpers.ofLiteral("{ partialRange: false }"))
                    // General configuration
                    .placeholder(
                        JsHelpers.of(getString("datetime.pattern.dateTime.range.placeholder")))
                    // Formatting
                    .format(JsHelpers.of(getString("datetime.pattern.dateTime")))
                    .formatPlaceholder(
                        JsHelpers.of(getString("datetime.pattern.dateTime.range.placeholder"))));
  }
}
