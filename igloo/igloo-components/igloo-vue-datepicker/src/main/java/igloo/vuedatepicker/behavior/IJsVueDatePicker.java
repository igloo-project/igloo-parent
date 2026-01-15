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
  IJsStatement timezone();

  @Nullable
  IJsStatement vertical();

  //
  // General configuration
  //

  @Nullable
  IJsObject config();

  @Nullable
  IJsStatement monthChangeOnScroll();

  @Nullable
  IJsObject inputAttrs();

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
  IJsStatement disableYearSelect();

  @Nullable
  IJsObject actionRow();

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
  IJsStatement filters();

  //
  // Time picker configuration
  //

  @Nullable
  IJsObject timeConfig();

  @Nullable
  IJsStatement enableTimePicker();

  //
  // Formatting
  //

  @Nullable
  IJsObject formats();

  @Nullable
  IJsStatement format();

  @Nullable
  IJsStatement formatPlaceholder();

  //
  // Localization
  //

  @Nullable
  IJsStatement selectBtnLabel();

  @Nullable
  IJsStatement cancelBtnLabel();

  @Nullable
  IJsStatement nowBtnLabel();

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
            Map.entry(":timezone", this::timezone),
            Map.entry(":vertical", this::vertical),
            // General configuration
            Map.entry(":action-row", this::getActionRowObject),
            Map.entry( // valeur obligatoire pour l'id
                ":input-attrs", this::getInputAttrObject),
            Map.entry(":config", this::getConfigObject),
            Map.entry(":clearable", this::clearable),
            Map.entry(":auto-apply", this::autoApply),
            Map.entry(":placeholder", this::placeholder),
            Map.entry(":no-today", this::noToday),
            Map.entry(":markers", this::markers),
            Map.entry(":disabled", this::disabled),
            Map.entry(":readonly", this::readonly),
            Map.entry(":disable-year-select", this::disableYearSelect),
            // Calendar configuration
            Map.entry(":min-date", this::minDate),
            Map.entry(":max-date", this::maxDate),
            Map.entry(":disabled-dates", this::disabledDates),
            Map.entry(":filters", this::filters),
            // Time picker configuration
            Map.entry(":time-config", this::getTimeConfigObject),
            // Formatting
            Map.entry(":formats", this::getFormatsObject),
            // Localization
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

  private IJsObject getActionRowObject() {
    IJsObject actionRow = actionRow();
    Map<String, IJsStatement> values =
        (actionRow != null && actionRow.values() != null)
            ? actionRow.values()
            : new LinkedHashMap<>();
    if (selectBtnLabel() != null) {
      values.put("selectBtnLabel", selectBtnLabel());
    }
    if (cancelBtnLabel() != null) {
      values.put("cancelBtnLabel", cancelBtnLabel());
    }
    if (nowBtnLabel() != null) {
      values.put("nowBtnLabel", nowBtnLabel());
    }
    return values.isEmpty() ? null : () -> values;
  }

  /**
   * create objects <a
   * href="https://vue3datepicker.com/props/general-configuration/#input-attrs">input-attrs</a>
   *
   * <p>must be present to add wicket id to input
   *
   * @return non null value
   */
  private IJsObject getInputAttrObject() {
    IJsObject inputAttrs = inputAttrs();
    Map<String, IJsStatement> values =
        (inputAttrs != null && inputAttrs.values() != null)
            ? inputAttrs.values()
            : new LinkedHashMap<>();
    return () -> values;
  }

  private IJsObject getFormatsObject() {
    IJsObject formats = formats();
    Map<String, IJsStatement> values =
        (formats != null && formats.values() != null) ? formats.values() : new LinkedHashMap<>();
    if (format() != null) {
      values.put("input", format());
    }
    return values.isEmpty() ? null : () -> values;
  }

  private IJsObject getTimeConfigObject() {
    IJsObject timeConfig = timeConfig();
    Map<String, IJsStatement> values =
        (timeConfig != null && timeConfig.values() != null)
            ? timeConfig.values()
            : new LinkedHashMap<>();
    if (enableTimePicker() != null) {
      values.put("enableTimePicker", enableTimePicker());
    }
    return values.isEmpty() ? null : () -> values;
  }

  private IJsObject getConfigObject() {
    IJsObject config = config();
    Map<String, IJsStatement> values =
        (config != null && config.values() != null) ? config.values() : new LinkedHashMap<>();
    if (monthChangeOnScroll() != null) {
      values.put("monthChangeOnScroll", monthChangeOnScroll());
    }
    return values.isEmpty() ? null : () -> values;
  }
}
