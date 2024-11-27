package igloo.vuedatepicker;

import igloo.bootstrap.js.statement.IJsDateList;
import igloo.bootstrap.js.statement.IJsObject;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.statement.JsBoolean;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;
import org.springframework.lang.Nullable;

@Value.Immutable
@Value.Style(typeImmutable = "*", typeAbstract = "I*")
public interface IJsDatePicker extends IJsObject, Serializable {

  IJsStatement dateModel();

  @Nullable
  IJsStatement minDate();

  @Nullable
  IJsStatement maxDate();

  @Nullable
  IJsStatement autoApply();

  @Nullable
  IJsStatement format();

  @Nullable
  IJsStatement multiCalendars();

  @Nullable
  IJsStatement range();

  @Nullable
  IJsStatement monthPicker();

  @Nullable
  IJsStatement yearPicker();

  @Nullable
  IJsStatement timePicker();

  @Nullable
  IJsStatement weekPicker();

  @Nullable
  IJsStatement placeholder();

  @Nullable
  IJsStatement textInput();

  @Nullable
  IJsStatement inline();

  @Nullable
  IJsStatement multiDates();

  @Nullable
  IJsStatement clearable();

  @Nullable
  IJsStatement flow();

  @Nullable
  IJsStatement partialFlow();

  @Nullable
  IJsStatement utc();

  @Nullable
  IJsStatement vertical();

  @Nullable
  IJsStatement disabledDates();

  @Nullable
  IJsStatement dark();

  @Nullable
  IJsStatement monthChangeOnScroll();

  @Nullable
  IJsStatement noToday();

  @Nullable
  IJsStatement disabled();

  @Nullable
  IJsStatement readonly();

  @Nullable
  IJsStatement required();

  @Nullable
  IJsStatement name();

  @Nullable
  IJsStatement disableYearSelect();

  @Nullable
  IJsStatement enableTimePicker();

  @Nullable
  IJsStatement loading();

  @Nullable
  IJsStatement onUpdateModel();

  @Override
  @Value.Derived
  default Map<String, IJsStatement> values() {
    Map<String, IJsStatement> result = new LinkedHashMap<>();

    Stream.<Map.Entry<String, Supplier<IJsStatement>>>of(
            Map.entry("v-model", this::dateModel),
            Map.entry(":min-date", this::minDate),
            Map.entry(":max-date", this::maxDate),
            Map.entry(":auto-apply", this::autoApply),
            Map.entry(":format", this::format),
            Map.entry(":multi-calendars", this::multiCalendars),
            Map.entry(
                ":range",
                () ->
                    dateModel() != null && dateModel() instanceof IJsDateList && range() == null
                        ? JsBoolean.of(true)
                        : range()),
            Map.entry(":range", this::range),
            Map.entry(":month-picker", this::monthPicker),
            Map.entry(":year-picker", this::yearPicker),
            Map.entry(":time-picker", this::timePicker),
            Map.entry(":week-picker", this::weekPicker),
            Map.entry(":placeholder", this::placeholder),
            Map.entry(":text-input", this::textInput),
            Map.entry(":inline", this::inline),
            Map.entry(":multi-dates", this::multiDates),
            Map.entry(":clearable", this::clearable),
            Map.entry(":flow", this::flow),
            Map.entry(":partial-flow", this::partialFlow),
            Map.entry(":utc", this::utc),
            Map.entry(":vertical", this::vertical),
            Map.entry(":disabled-dates", this::disabledDates),
            Map.entry(":dark", this::dark),
            Map.entry(":month-change-on-scroll", this::monthChangeOnScroll),
            Map.entry(":no-today", this::noToday),
            Map.entry(":disabled", this::disabled),
            Map.entry(":readonly", this::readonly),
            Map.entry(":required", this::required),
            Map.entry(":name", this::name),
            Map.entry(":disable-year-select", this::disableYearSelect),
            Map.entry(":enable-time-picker", this::enableTimePicker),
            Map.entry(":loading", this::loading),
            Map.entry("@update:model-value", this::onUpdateModel))
        .map(e -> Pair.of(e.getKey(), e.getValue().get()))
        .filter(e -> e.getRight() != null)
        .forEachOrdered(e -> result.put(e.getLeft(), e.getRight()));
    return result;
  }
}
