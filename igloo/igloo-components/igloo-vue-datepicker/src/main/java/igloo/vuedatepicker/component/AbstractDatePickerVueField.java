package igloo.vuedatepicker.component;

import com.google.common.collect.Iterables;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsVueDatePicker;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import igloo.vuedatepicker.behavior.VueDatePickerBehavior;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.wicket.more.markup.html.form.IVueComponent;

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

    // Default handling for component tag
    super.onComponentTag(tag);
  }

  protected DateTimeFormatter getFormat() {
    return DateTimeFormatter.ofPattern(
        Optional.ofNullable(getVueDatePickerBehavior().getJsVueDatePicker())
            .map(IJsVueDatePicker::format)
            .map(IJsStatement::render)
            .map(s -> StringUtils.unwrap(s, "\""))
            .orElseThrow());
  }

  protected IModel<String> getFormatPlaceholderModel() {
    return () ->
        Optional.ofNullable(getVueDatePickerBehavior().getJsVueDatePicker())
            .map(IJsVueDatePicker::formatPlaceholder)
            .map(IJsStatement::render)
            .map(s -> StringUtils.unwrap(s, "\""))
            .orElseThrow();
  }

  protected SerializableConsumer<Builder> initDefaultJsVueDatePickerBuilder() {
    return builder ->
        builder
            // Modes
            .textInput(JsHelpers.of(true))
            // Localization
            .locale(JsHelpers.of(getLocale().getLanguage()))
            .selectText(JsHelpers.of(getString(("datepicker.wording.select"))))
            .cancelText(JsHelpers.of(getString(("datepicker.wording.cancel"))))
            .ariaLabels(getAriaLabels())
            // Positioning
            .teleport(JsHelpers.of(true))
            // Look and feel
            .sixWeeks(JsHelpers.of("fair"))
            .ui(JsHelpers.ofLiteral("{ input: 'form-control', menu: 'dp__menu_bs' }"));
  }

  public VueDatePickerBehavior getVueDatePickerBehavior() {
    return Iterables.getOnlyElement(getBehaviors(VueDatePickerBehavior.class));
  }

  public String getVModelVarName() {
    return getVueDatePickerBehavior().getVmodelVarName(this);
  }

  public String getVModel() {
    return "vueInit.vModels.get('%s')".formatted(getVueDatePickerBehavior().getVmodelVarName(this));
  }

  public String getVueOnChangeVarName() {
    return getVueDatePickerBehavior().getVueOnChangeVarName(this);
  }

  public AbstractDatePickerVueField<T> addToBuilder(SerializableConsumer<Builder> consumer) {
    getVueDatePickerBehavior().addToBuilder(consumer);
    return this;
  }

  public AbstractDatePickerVueField<T> labelPlaceholder() {
    addToBuilder(builder -> builder.placeholder(JsHelpers.ofString(getLabel())));
    return this;
  }

  public AbstractDatePickerVueField<T> minDate(AbstractDatePickerVueField<T> datePicker) {
    addToBuilder(builder -> builder.minDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName())));
    return this;
  }

  public AbstractDatePickerVueField<T> minDate(IModel<LocalDate> dateMin) {
    addToBuilder(builder -> builder.minDate(JsHelpers.ofLocalDateModel(dateMin)));
    return this;
  }

  public AbstractDatePickerVueField<T> maxDate(AbstractDatePickerVueField<T> datePicker) {
    addToBuilder(builder -> builder.maxDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName())));
    return this;
  }

  public AbstractDatePickerVueField<T> maxDate(IModel<LocalDate> dateMax) {
    addToBuilder(builder -> builder.maxDate(JsHelpers.ofLocalDateModel(dateMax)));
    return this;
  }

  /**
   * append a css class to the datepicker input component (not available in html file, generate via
   * vueJs)
   *
   * <p>Check if vueInit JS variable is declared in header and exists in window scope
   *
   * <p>Wicket form validator reloads all forms on the page even if the form isn't displayed.
   */
  @Override
  public void appendCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "typeof vueInit !==  'undefined' ? vueInit.appendInputCssClass('%s', '%s') : undefined"
            .formatted(getMarkupId(), cssClass));
  }

  /**
   * remove a css class to the datepicker input component (not available in html file, generate via
   * vueJs)
   *
   * <p>Check if vueInit JS variable is declared in header and exists in window scope
   *
   * <p>Wicket form validator reloads all forms on the page even if the form isn't displayed.
   */
  @Override
  public void removeCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "typeof vueInit !==  'undefined' ? vueInit.removeInputCssClass('%s', '%s') : undefined"
            .formatted(getMarkupId(), cssClass));
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
        .putValues(
            "incrementValue",
            JsHelpers.ofLiteral(
                "type => typeof vueInit !==  'undefined' ? vueInit.getAriaLabelType(type, '%s', '%s', '%s'): undefined"
                    .formatted(
                        getString("datepicker.aria.label.incrementValue.hours"),
                        getString("datepicker.aria.label.incrementValue.minutes"),
                        getString("datepicker.aria.label.incrementValue.seconds"))))
        .putValues(
            "decrementValue",
            JsHelpers.ofLiteral(
                "type => typeof vueInit !==  'undefined' ? vueInit.getAriaLabelType(type, '%s', '%s', '%s') : undefined"
                    .formatted(
                        getString("datepicker.aria.label.decrementValue.hours"),
                        getString("datepicker.aria.label.decrementValue.minutes"),
                        getString("datepicker.aria.label.decrementValue.seconds"))))
        .putValues(
            "openTpOverlay",
            JsHelpers.ofLiteral(
                "type => typeof vueInit !==  'undefined' ? vueInit.getAriaLabelType(type, '%s', '%s', '%s') : undefined"
                    .formatted(
                        getString("datepicker.aria.label.openTpOverlay.hours"),
                        getString("datepicker.aria.label.openTpOverlay.minutes"),
                        getString("datepicker.aria.label.openTpOverlay.seconds"))))
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
        .putValues(
            "monthPicker",
            JsHelpers.ofLiteral(
                "overlay => typeof vueInit !==  'undefined' ? vueInit.getAriaLabelOverlay(overlay, '%s', '%s') : undefined"
                    .formatted(
                        getString("datepicker.aria.label.monthPicker.overlay"),
                        getString("datepicker.aria.label.monthPicker"))))
        .putValues(
            "yearPicker",
            JsHelpers.ofLiteral(
                "overlay => typeof vueInit !==  'undefined' ? vueInit.getAriaLabelOverlay(overlay, '%s', '%s') : undefined"
                    .formatted(
                        getString("datepicker.aria.label.yearPicker.overlay"),
                        getString("datepicker.aria.label.yearPicker"))))
        .putValues(
            "timeOverlay",
            JsHelpers.ofLiteral(
                "type => typeof vueInit !==  'undefined' ? vueInit.getAriaLabelType(type, '%s', '%s', '%s') : undefined"
                    .formatted(
                        getString("datepicker.aria.label.timeOverlay.hours"),
                        getString("datepicker.aria.label.timeOverlay.minutes"),
                        getString("datepicker.aria.label.timeOverlay.seconds"))))
        .build();
  }
}
