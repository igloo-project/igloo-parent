package igloo.vuedatepicker.behavior;

import igloo.bootstrap.js.statement.IJsObject;
import igloo.bootstrap.js.statement.IJsStatement;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;

/**
 * Options of a datepicker: its value, the VueDatePicker props and the change callback. Each option
 * is a JS statement, rendered as is in the page.
 *
 * @see <a href="https://vue3datepicker.com/props/modes/">VueDatePicker props</a>
 */
@Value.Immutable
@Value.Style(typeImmutable = "*", typeAbstract = "I*", jakarta = true)
public interface IJsVueDatePicker extends IJsObject, Serializable {

  /**
   * Initial value of the datepicker, rendered from the field model. Not rendered with the other
   * props (see {@link #values()}): the datepicker keeps the current value, which the user and the
   * linked datepickers change, and gives it to VueDatePicker as `modelValue`.
   */
  IJsStatement model();

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

  /**
   * JS function called with the new value after each change made by the user. Not rendered with the
   * other props (see {@link #values()}): the datepicker handles `onUpdate:modelValue` itself, to
   * update its value and notify Wicket first, then calls this function.
   */
  @Nullable
  IJsStatement onUpdateModel();

  @Nullable
  IJsStatement onTextSubmit();

  //
  // Slots
  //

  /**
   * VueDatePicker slots, to replace parts of its rendering: a JS object whose keys are slot names
   * and values functions rendering the slot with the global {@code Vue.h}, e.g. {@code { marker: ()
   * => Vue.h('span', { class: 'custom-marker' }) }}.
   *
   * @see <a href="https://vue3datepicker.com/slots/content/">VueDatePicker slots</a>
   */
  @Nullable
  IJsStatement slots();

  //
  // Other props
  //

  /**
   * VueDatePicker props the options above do not support yet, by prop name, e.g. {@code
   * putUnsupportedProps("weekNumbers", JsHelpers.ofLiteral("{ type: 'iso' }"))}. A way out, harder
   * to maintain: prefer asking for an option in igloo. A prop an option supports is refused, as
   * well as the props the datepicker sets itself.
   */
  Map<String, IJsStatement> unsupportedProps();

  @Value.Check
  default void checkUnsupportedProps() {
    Set<String> supported = Set.copyOf(supportedProps().stream().map(Map.Entry::getKey).toList());
    unsupportedProps()
        .keySet()
        .forEach(
            name -> {
              if (supported.contains(name)) {
                throw new IllegalStateException(
                    "Prop %s has an option: use builder.%s(...) instead of putUnsupportedProps"
                        .formatted(name, name));
              }
              if (Set.of("modelValue", "onUpdate:modelValue").contains(name)) {
                throw new IllegalStateException(
                    "Prop %s is set by the datepicker, from the field model: use model() and onUpdateModel()"
                        .formatted(name));
              }
            });
  }

  /**
   * Map of VueDatePicker props: contains all non-null options (excluding {@link #slots()}, {@link
   * #model()} and {@link #onUpdateModel()} which are managed internally), merged with {@link
   * #unsupportedProps()}.
   */
  @Override
  @Value.Derived
  default Map<String, IJsStatement> values() {
    Map<String, IJsStatement> result = new LinkedHashMap<>();
    supportedProps().stream()
        .map(e -> Pair.of(e.getKey(), e.getValue().get()))
        .filter(e -> e.getRight() != null)
        .forEachOrdered(e -> result.put(e.getLeft(), e.getRight()));
    result.putAll(unsupportedProps());
    return result;
  }

  /** The props the options support, by prop name. */
  private List<Map.Entry<String, Supplier<IJsStatement>>> supportedProps() {
    return Stream.<Map.Entry<String, Supplier<IJsStatement>>>of(
            // Modes
            Map.entry("range", this::range),
            Map.entry("multiCalendars", this::multiCalendars),
            Map.entry("monthPicker", this::monthPicker),
            Map.entry("timePicker", this::timePicker),
            Map.entry("yearPicker", this::yearPicker),
            Map.entry("weekPicker", this::weekPicker),
            Map.entry("textInput", this::textInput),
            Map.entry("inline", this::inline),
            Map.entry("multiDates", this::multiDates),
            Map.entry("flow", this::flow),
            Map.entry("timezone", this::timezone),
            Map.entry("vertical", this::vertical),
            // General configuration
            Map.entry("actionRow", this::getActionRowObject),
            Map.entry("inputAttrs", this::inputAttrs),
            Map.entry("config", this::getConfigObject),
            Map.entry("clearable", this::clearable),
            Map.entry("autoApply", this::autoApply),
            Map.entry("placeholder", this::placeholder),
            Map.entry("noToday", this::noToday),
            Map.entry("markers", this::markers),
            Map.entry("disabled", this::disabled),
            Map.entry("readonly", this::readonly),
            Map.entry("disableYearSelect", this::disableYearSelect),
            // Calendar configuration
            Map.entry("minDate", this::minDate),
            Map.entry("maxDate", this::maxDate),
            Map.entry("disabledDates", this::disabledDates),
            Map.entry("filters", this::filters),
            // Time picker configuration
            Map.entry("timeConfig", this::getTimeConfigObject),
            // Formatting
            Map.entry("formats", this::getFormatsObject),
            // Localization
            Map.entry("ariaLabels", this::ariaLabels),
            // Positioning
            Map.entry("teleport", this::teleport),
            // Look and feel
            Map.entry("sixWeeks", this::sixWeeks),
            Map.entry("ui", this::ui),
            // Events: Vue takes listeners as props named on + event name
            Map.entry("onTextSubmit", this::onTextSubmit))
        .toList();
  }

  /**
   * Values of an optional object option, copied: the options given by projects are not modified.
   */
  private Map<String, IJsStatement> copyValues(@Nullable IJsObject object) {
    return Optional.ofNullable(object)
        .map(IJsObject::values)
        .map(LinkedHashMap::new)
        .orElseGet(LinkedHashMap::new);
  }

  private IJsObject getActionRowObject() {
    Map<String, IJsStatement> values = copyValues(actionRow());
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

  private IJsObject getFormatsObject() {
    Map<String, IJsStatement> values = copyValues(formats());
    if (format() != null) {
      values.put("input", format());
    }
    return values.isEmpty() ? null : () -> values;
  }

  private IJsObject getTimeConfigObject() {
    Map<String, IJsStatement> values = copyValues(timeConfig());
    if (enableTimePicker() != null) {
      values.put("enableTimePicker", enableTimePicker());
    }
    return values.isEmpty() ? null : () -> values;
  }

  private IJsObject getConfigObject() {
    Map<String, IJsStatement> values = copyValues(config());
    if (monthChangeOnScroll() != null) {
      values.put("monthChangeOnScroll", monthChangeOnScroll());
    }
    return values.isEmpty() ? null : () -> values;
  }
}
