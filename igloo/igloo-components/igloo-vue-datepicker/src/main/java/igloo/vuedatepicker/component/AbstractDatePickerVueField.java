package igloo.vuedatepicker.component;

import com.google.common.collect.Iterables;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.statement.IJsString;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsVueDatePicker;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import igloo.vuedatepicker.behavior.VueDatePickerBehavior;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.string.Strings;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.wicket.more.markup.html.form.IVueComponent;

/** Form field edited with a VueDatePicker, attached to a {@code <vue-date-picker>} tag. */
public abstract class AbstractDatePickerVueField<T> extends AbstractTextComponent<T>
    implements IVueComponent {

  private static final long serialVersionUID = -1;

  protected AbstractDatePickerVueField(
      String id, IModel<T> model, SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    super(id, model);
    add(
        new VueDatePickerBehavior(
            initDefaultJsVueDatePickerBuilder().andThen(jsVueDatePickerConsumer)));
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    // Must be attached to a vue-date-picker tag
    checkComponentTag(tag, "vue-date-picker");

    // Browsers do not close <vue-date-picker />: the following elements would end up inside
    if (tag.isOpenClose()) {
      tag.setType(TagType.OPEN);
    }

    // Default handling for component tag
    super.onComponentTag(tag);
  }

  /**
   * Converts the input with the datepicker format, see {@link #convertValue(String[])}, like {@link
   * AbstractTextComponent#convertInput()} does for an untyped field. Wicket would otherwise use the
   * converter of the session locale as soon as the model gives the value type.
   */
  @Override
  public void convertInput() {
    String[] value = getInputAsArray();
    if (getConvertEmptyInputStringToNull()
        && Strings.isEmpty(value != null && value.length > 0 ? value[0] : null)) {
      setConvertedInput(null);
      return;
    }
    try {
      setConvertedInput(convertValue(value));
    } catch (ConversionException e) {
      error(newValidationError(e));
    }
  }

  /**
   * Format the input is parsed with: the {@code format} option, the one the datepicker displays the
   * value with. Override it if this option is not a pattern, e.g. a JS function.
   */
  protected DateTimeFormatter getFormat() {
    return DateTimeFormatter.ofPattern(getStringOption(IJsVueDatePicker::format, "format"));
  }

  protected ConversionException newConversionException(String input, String resourceKey) {
    return new ConversionException("Cannot parse '%s'".formatted(input))
        .setResourceKey(resourceKey)
        .setSourceValue(input)
        .setLocale(getLocale());
  }

  protected IModel<String> getFormatPlaceholderModel() {
    return () -> getStringOption(IJsVueDatePicker::formatPlaceholder, "formatPlaceholder");
  }

  /**
   * Value of a string option, as given to the builder: read from the rendered JS, it would be
   * escaped.
   */
  private String getStringOption(Function<IJsVueDatePicker, IJsStatement> option, String name) {
    IJsVueDatePicker jsVueDatePicker = getVueDatePickerBehavior().getJsVueDatePicker();
    if (jsVueDatePicker == null) {
      throw new IllegalStateException(
          "Options of %s not built yet".formatted(getPageRelativePath()));
    }
    if (option.apply(jsVueDatePicker) instanceof IJsString string && string.value() != null) {
      return string.value();
    }
    throw new IllegalStateException(
        "Option %s of %s must be a string, set with JsHelpers.of(String) or JsHelpers.ofString(IModel)"
            .formatted(name, getPageRelativePath()));
  }

  protected SerializableConsumer<Builder> initDefaultJsVueDatePickerBuilder() {
    return builder ->
        builder
            // Modes
            .textInput(JsHelpers.of(true))
            // Localization
            .selectBtnLabel(JsHelpers.of(getString(("datepicker.wording.select"))))
            .cancelBtnLabel(JsHelpers.of(getString(("datepicker.wording.cancel"))))
            .ariaLabels(getAriaLabels())
            // Positioning
            .teleport(JsHelpers.of(true))
            // Look and feel
            .sixWeeks(JsHelpers.of("fair"))
            .ui(JsHelpers.ofLiteral("{ input: 'form-control', menu: 'dp--menu-bs' }"));
  }

  public VueDatePickerBehavior getVueDatePickerBehavior() {
    return Iterables.getOnlyElement(getBehaviors(VueDatePickerBehavior.class));
  }

  public String getJsModelValue() {
    return "vueInit.getModelValue(%s)".formatted(JsHelpers.of(getMarkupId()).render());
  }

  public String getJsSetModelValue(String jsValue) {
    return "vueInit.setModelValue(%s, %s)".formatted(JsHelpers.of(getMarkupId()).render(), jsValue);
  }

  public AbstractDatePickerVueField<T> addToBuilder(SerializableConsumer<Builder> consumer) {
    getVueDatePickerBehavior().addToBuilder(consumer);
    return this;
  }

  public AbstractDatePickerVueField<T> labelPlaceholder() {
    addToBuilder(builder -> builder.placeholder(JsHelpers.ofString(getLabel())));
    return this;
  }

  /** The min date follows the value of {@code datePicker}, client-side. */
  public AbstractDatePickerVueField<T> minDate(AbstractDatePickerVueField<T> datePicker) {
    addToBuilder(builder -> builder.minDate(JsHelpers.ofLiteral(datePicker.getJsModelValue())));
    return this;
  }

  public AbstractDatePickerVueField<T> minDate(IModel<LocalDate> dateMin) {
    addToBuilder(builder -> builder.minDate(JsHelpers.ofLocalDateModel(dateMin)));
    return this;
  }

  /** The max date follows the value of {@code datePicker}, client-side. */
  public AbstractDatePickerVueField<T> maxDate(AbstractDatePickerVueField<T> datePicker) {
    addToBuilder(builder -> builder.maxDate(JsHelpers.ofLiteral(datePicker.getJsModelValue())));
    return this;
  }

  public AbstractDatePickerVueField<T> maxDate(IModel<LocalDate> dateMax) {
    addToBuilder(builder -> builder.maxDate(JsHelpers.ofLocalDateModel(dateMax)));
    return this;
  }

  /**
   * Adds a CSS class to the datepicker input, rendered by Vue. {@code vueInit} may be absent:
   * Wicket calls this method for every Vue component of the page, even outside the refreshed form.
   */
  @Override
  public void appendCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "typeof vueInit !== 'undefined' && vueInit.appendInputCssClass(%s, %s);"
            .formatted(JsHelpers.of(getMarkupId()).render(), JsHelpers.of(cssClass).render()));
  }

  /** Removes a CSS class from the datepicker input, see {@link #appendCssClass}. */
  @Override
  public void removeCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "typeof vueInit !== 'undefined' && vueInit.removeInputCssClass(%s, %s);"
            .formatted(JsHelpers.of(getMarkupId()).render(), JsHelpers.of(cssClass).render()));
  }

  protected IJsStatement getAriaLabels() {
    return JsHelpers.mapping()
        .putValues("toggleOverlay", JsHelpers.of(getString("datepicker.aria.label.toggleOverlay")))
        .putValues("menu", JsHelpers.of(getString(("datepicker.aria.label.menu"))))
        .putValues(
            "input",
            JsHelpers.ofString(
                () -> getString("datepicker.aria.label.input", getFormatPlaceholderModel())))
        .putValues(
            "openTimePicker", JsHelpers.of(getString(("datepicker.aria.label.openTimePicker"))))
        .putValues(
            "closeTimePicker", JsHelpers.of(getString(("datepicker.aria.label.closeTimePicker"))))
        .putValues("incrementValue", byTimeUnit("datepicker.aria.label.incrementValue"))
        .putValues("decrementValue", byTimeUnit("datepicker.aria.label.decrementValue"))
        .putValues("openTpOverlay", byTimeUnit("datepicker.aria.label.openTpOverlay"))
        .putValues("amPmButton", JsHelpers.of(getString(("datepicker.aria.label.amPmButton"))))
        .putValues(
            "openYearsOverlay", JsHelpers.of(getString(("datepicker.aria.label.openYearsOverlay"))))
        .putValues(
            "openMonthsOverlay",
            JsHelpers.of(getString(("datepicker.aria.label.openMonthsOverlay"))))
        .putValues("nextMonth", JsHelpers.of(getString(("datepicker.aria.label.nextMonth"))))
        .putValues("prevMonth", JsHelpers.of(getString(("datepicker.aria.label.prevMonth"))))
        .putValues("nextYear", JsHelpers.of(getString(("datepicker.aria.label.nextYear"))))
        .putValues("prevYear", JsHelpers.of(getString(("datepicker.aria.label.prevYear"))))
        .putValues("clearInput", JsHelpers.of(getString(("datepicker.aria.label.clearInput"))))
        .putValues("calendarIcon", JsHelpers.of(getString(("datepicker.aria.label.calendarIcon"))))
        .putValues("timePicker", JsHelpers.of(getString(("datepicker.aria.label.timePicker"))))
        .putValues("monthPicker", byOverlay("datepicker.aria.label.monthPicker"))
        .putValues("yearPicker", byOverlay("datepicker.aria.label.yearPicker"))
        .putValues("timeOverlay", byTimeUnit("datepicker.aria.label.timeOverlay"))
        .build();
  }

  private IJsStatement byTimeUnit(String keyPrefix) {
    IJsStatement labels =
        JsHelpers.mapping()
            .putValues("hours", JsHelpers.of(getString(keyPrefix + ".hours")))
            .putValues("minutes", JsHelpers.of(getString(keyPrefix + ".minutes")))
            .putValues("seconds", JsHelpers.of(getString(keyPrefix + ".seconds")))
            .build();
    return JsHelpers.ofLiteral("type => (%s)[type]".formatted(labels.render()));
  }

  private IJsStatement byOverlay(String key) {
    return JsHelpers.ofLiteral(
        "overlay => overlay ? %s : %s"
            .formatted(
                JsHelpers.of(getString(key + ".overlay")).render(),
                JsHelpers.of(getString(key)).render()));
  }
}
