package igloo.vuedatepicker.behavior;

import igloo.bootstrap.js.statement.IJsObject;
import igloo.bootstrap.js.statement.IJsStatement;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;
import org.springframework.lang.Nullable;

/**
 * Options applies to Vue.js Vue Datepicker. @see <a href="https://vue3datepicker.com/">Vue
 * Datepicker documentation</a>
 */
@Value.Immutable()
@Value.Style(typeImmutable = "*", typeAbstract = "I*")
public interface IJsVueDatePicker extends IJsObject, Serializable {

  IJsStatement model();

  @Nullable
  IJsStatement vif();

  @Nullable
  IJsStatement velse();

  //
  // Modes
  //

  @Nullable
  IJsStatement range();

  @Nullable
  IJsStatement multiCalendars();

  @Nullable
  IJsStatement monthPicker();

  @Nullable
  IJsStatement timePicker();

  @Nullable
  IJsStatement yearPicker();

  @Nullable
  IJsStatement weekPicker();

  @Nullable
  IJsStatement textInput();

  @Nullable
  IJsStatement inline();

  @Nullable
  IJsStatement multiDates();

  @Nullable
  IJsStatement flow();

  @Nullable
  IJsStatement utc();

  @Nullable
  IJsStatement vertical();

  //
  // Modes configuration
  //

  @Nullable
  IJsStatement partialFlow();

  //
  // General configuration
  //

  @Nullable
  IJsStatement monthChangeOnScroll();

  @Nullable
  IJsStatement clearable();

  @Nullable
  IJsStatement autoApply();

  @Nullable
  IJsStatement placeholder();

  @Nullable
  IJsStatement noToday();

  @Nullable
  IJsStatement markers();

  @Nullable
  IJsStatement disabled();

  @Nullable
  IJsStatement readonly();

  @Nullable
  IJsStatement name();

  @Nullable
  IJsStatement disableYearSelect();

  //
  // Calendar configuration
  //

  @Nullable
  IJsStatement minDate();

  @Nullable
  IJsStatement maxDate();

  @Nullable
  IJsStatement disabledDates();

  @Nullable
  IJsStatement disableWeekDays();

  //
  // Time picker configuration
  //

  @Nullable
  IJsStatement enableTimePicker();

  //
  // Formatting
  //

  @Nullable
  IJsStatement format();

  @Nullable
  IJsStatement formatPlaceholder();

  //
  // Localization
  //

  @Nullable
  IJsStatement locale();

  @Nullable
  IJsStatement selectText();

  @Nullable
  IJsStatement cancelText();

  @Nullable
  IJsStatement ariaLabels();

  //
  // Positioning
  //

  @Nullable
  IJsStatement teleport();

  //
  // Look and feel
  //

  @Nullable
  IJsStatement sixWeeks();

  @Nullable
  IJsStatement ui();

  //
  // Events
  //

  @Nullable
  IJsStatement onUpdateModel();

  @Nullable
  IJsStatement onTextSubmit();

  /** create map to build tag key/value apply on datepicker input */
  @Override
  @Value.Derived
  default Map<String, IJsStatement> values() {
    Map<String, IJsStatement> result = new LinkedHashMap<>();

    Stream.<Map.Entry<String, Supplier<IJsStatement>>>of(
            Map.entry("v-model", this::model),
            Map.entry("v-if", this::vif),
            Map.entry("v-else", this::velse),
            // Modes
            Map.entry(":range", this::range),
            Map.entry(":multi-calendars", this::multiCalendars),
            Map.entry(":month-picker", this::monthPicker),
            Map.entry(":time-picker", this::timePicker),
            Map.entry(":year-picker", this::yearPicker),
            Map.entry(":week-picker", this::weekPicker),
            Map.entry(":text-input", this::textInput),
            Map.entry(":inline", this::inline),
            Map.entry(":multi-dates", this::multiDates),
            Map.entry(":flow", this::flow),
            Map.entry(":utc", this::utc),
            Map.entry(":vertical", this::vertical),
            // Modes configuration
            Map.entry(":partial-flow", this::partialFlow),
            // General configuration
            Map.entry(":month-change-on-scroll", this::monthChangeOnScroll),
            Map.entry(":clearable", this::clearable),
            Map.entry(":auto-apply", this::autoApply),
            Map.entry(":placeholder", this::placeholder),
            Map.entry(":no-today", this::noToday),
            Map.entry(":markers", this::markers),
            Map.entry(":disabled", this::disabled),
            Map.entry(":readonly", this::readonly),
            Map.entry(":name", this::name),
            Map.entry(":disable-year-select", this::disableYearSelect),
            // Calendar configuration
            Map.entry(":min-date", this::minDate),
            Map.entry(":max-date", this::maxDate),
            Map.entry(":disabled-dates", this::disabledDates),
            Map.entry(":disabled-week-days", this::disableWeekDays),
            // Time picker configuration
            Map.entry(":enable-time-picker", this::enableTimePicker),
            // Formatting
            Map.entry(":format", this::format),
            // Localization
            Map.entry(":locale", this::locale),
            Map.entry(":select-text", this::selectText),
            Map.entry(":cancel-text", this::cancelText),
            Map.entry(":aria-labels", this::ariaLabels),
            // Positioning
            Map.entry(":teleport", this::teleport),
            // Look and feel
            Map.entry(":six-weeks", this::sixWeeks),
            Map.entry(":ui", this::ui),
            // Events
            Map.entry("@text-submit", this::onTextSubmit),
            Map.entry("@update:model-value", this::onUpdateModel))
        .map(e -> Pair.of(e.getKey(), e.getValue().get()))
        .filter(e -> e.getRight() != null)
        .forEachOrdered(e -> result.put(e.getLeft(), e.getRight()));
    return result;
  }
}
