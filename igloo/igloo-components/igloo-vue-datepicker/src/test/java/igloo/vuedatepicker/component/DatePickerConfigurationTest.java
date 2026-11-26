package igloo.vuedatepicker.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsVueDatePicker;
import igloo.vuedatepicker.behavior.JsVueDatePicker;
import igloo.vuedatepicker.test.AbstractVueDatePickerTestCase;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Function;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;

/** Datepicker options set by each field type, and how projects override them. */
class DatePickerConfigurationTest extends AbstractVueDatePickerTestCase {

  @Test
  void commonDefaults() {
    IJsVueDatePicker options = options(render(id -> new LocalDatePickerVueField(id, Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::textInput)).isEqualTo("true");
    assertThat(rendered(options, IJsVueDatePicker::teleport)).isEqualTo("true");
    assertThat(rendered(options, IJsVueDatePicker::sixWeeks)).isEqualTo("\"fair\"");
    assertThat(rendered(options, IJsVueDatePicker::ui))
        .isEqualTo("{ input: 'form-control', menu: 'dp--menu-bs' }");
    assertThat(rendered(options, IJsVueDatePicker::selectBtnLabel)).isEqualTo("\"Select\"");
    assertThat(rendered(options, IJsVueDatePicker::cancelBtnLabel)).isEqualTo("\"Cancel\"");
    assertThat(options.ariaLabels()).isNotNull();
  }

  @Test
  void localDate() {
    IJsVueDatePicker options = options(render(id -> new LocalDatePickerVueField(id, Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"dd/MM/yyyy\"");
    assertThat(rendered(options, IJsVueDatePicker::placeholder)).isEqualTo("\"jj/mm/aaaa\"");
    assertThat(rendered(options, IJsVueDatePicker::autoApply)).isEqualTo("true");
    assertThat(rendered(options, IJsVueDatePicker::enableTimePicker)).isEqualTo("false");
  }

  @Test
  void localDateRange() {
    IJsVueDatePicker options =
        options(render(id -> new LocalDateRangePickerVueField(id, Model.of(), Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::range)).isEqualTo("{ partialRange: false }");
    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"dd/MM/yyyy\"");
    assertThat(rendered(options, IJsVueDatePicker::enableTimePicker)).isEqualTo("false");
  }

  @Test
  void localDateTime() {
    IJsVueDatePicker options =
        options(render(id -> new LocalDateTimePickerVueField(id, Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"dd/MM/yyyy HH:mm\"");
    assertThat(options.enableTimePicker()).isNull();
  }

  @Test
  void localDateTimeRange() {
    IJsVueDatePicker options =
        options(render(id -> new LocalDateTimeRangePickerVueField(id, Model.of(), Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::range)).isEqualTo("{ partialRange: false }");
    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"dd/MM/yyyy HH:mm\"");
  }

  @Test
  void localTime() {
    IJsVueDatePicker options = options(render(id -> new LocalTimePickerVueField(id, Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::timePicker)).isEqualTo("true");
    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"HH:mm\"");
  }

  @Test
  void yearMonth() {
    IJsVueDatePicker options = options(render(id -> new YearMonthPickerVueField(id, Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::monthPicker)).isEqualTo("true");
    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"MM/yyyy\"");
  }

  @Test
  void year() {
    IJsVueDatePicker options = options(render(id -> new YearPickerVueField(id, Model.of())));

    assertThat(rendered(options, IJsVueDatePicker::yearPicker)).isEqualTo("true");
    assertThat(rendered(options, IJsVueDatePicker::format)).isEqualTo("\"yyyy\"");
  }

  @Test
  void constructorBuilderOverridesDefaults() {
    IJsVueDatePicker options =
        options(
            render(
                id ->
                    new LocalDatePickerVueField(
                        id,
                        Model.of(),
                        builder -> builder.autoApply(JsHelpers.of(false)).textInput(null))));

    assertThat(rendered(options, IJsVueDatePicker::autoApply)).isEqualTo("false");
    assertThat(options.textInput()).isNull();
  }

  @Test
  void addToBuilderOverridesDefaults() {
    IJsVueDatePicker options =
        options(
            render(
                id ->
                    new LocalDatePickerVueField(id, Model.of())
                        .addToBuilder(
                            builder ->
                                builder.filters(JsHelpers.ofLiteral("{ weekDays: [6,0] }")))));

    assertThat(rendered(options, IJsVueDatePicker::filters)).isEqualTo("{ weekDays: [6,0] }");
  }

  @Test
  void labelPlaceholder() {
    IJsVueDatePicker options =
        options(
            render(
                id ->
                    new LocalDatePickerVueField(id, Model.of())
                        .labelPlaceholder()
                        .setLabel(Model.of("Birth date"))));

    assertThat(rendered(options, IJsVueDatePicker::placeholder)).isEqualTo("\"Birth date\"");
  }

  @Test
  void minAndMaxDateFromModels() {
    LocalDate min = LocalDate.of(2026, 1, 1);
    LocalDate max = LocalDate.of(2026, 12, 31);

    IJsVueDatePicker options =
        options(
            render(
                id ->
                    new LocalDatePickerVueField(id, Model.of())
                        .minDate(Model.of(min))
                        .maxDate(Model.of(max))));

    assertThat(rendered(options, IJsVueDatePicker::minDate)).isEqualTo(epochMillis(min));
    assertThat(rendered(options, IJsVueDatePicker::maxDate)).isEqualTo(epochMillis(max));
  }

  @Test
  void minAndMaxDateFromOtherDatepickers() {
    IJsVueDatePicker options =
        options(
            render(
                id -> {
                  LocalDatePickerVueField start = new LocalDatePickerVueField("start", Model.of());
                  LocalDatePickerVueField end = new LocalDatePickerVueField("end", Model.of());
                  start.setMarkupId("start");
                  end.setMarkupId("end");
                  return new LocalDatePickerVueField(id, Model.of()).minDate(start).maxDate(end);
                }));

    assertThat(rendered(options, IJsVueDatePicker::minDate))
        .isEqualTo("vueInit.getModelValue(\"start\")");
    assertThat(rendered(options, IJsVueDatePicker::maxDate))
        .isEqualTo("vueInit.getModelValue(\"end\")");
  }

  @Test
  void jsExpressions() {
    FormComponent<?> field =
        render(
            id -> {
              LocalDatePickerVueField birthDate = new LocalDatePickerVueField(id, Model.of());
              birthDate.setMarkupId("birthDate");
              return birthDate;
            });

    assertThat(((AbstractDatePickerVueField<?>) field).jsGetModelValue())
        .isEqualTo("vueInit.getModelValue(\"birthDate\")");
    assertThat(((AbstractDatePickerVueField<?>) field).jsSetModelValue("value"))
        .isEqualTo("vueInit.setModelValue(\"birthDate\", value)");
  }

  @Test
  void propsAreNamedAfterVueDatePickerProps() {
    IJsVueDatePicker options = options(render(id -> new LocalDatePickerVueField(id, Model.of())));

    assertThat(options.values())
        .containsKeys("textInput", "autoApply", "formats", "teleport", "sixWeeks", "ui")
        .doesNotContainKeys("model", "onUpdateModel");
  }

  @Test
  void unsupportedPropsAreRenderedWithTheProps() {
    IJsVueDatePicker options =
        options(
            render(
                id ->
                    new LocalDatePickerVueField(
                        id,
                        Model.of(),
                        builder ->
                            builder.putUnsupportedProps(
                                "weekNumbers", JsHelpers.ofLiteral("{ type: 'iso' }")))));

    assertThat(options.values()).containsKey("textInput");
    assertThat(options.values().get("weekNumbers").render()).isEqualTo("{ type: 'iso' }");
  }

  /** Two ways to set a prop would be hard to maintain: the option must be used. */
  @Test
  void unsupportedPropsRefuseAPropWithAnOption() {
    JsVueDatePicker.Builder builder =
        JsVueDatePicker.builder()
            .model(JsHelpers.of("value"))
            .putUnsupportedProps("minDate", JsHelpers.ofLiteral("new Date()"));

    assertThatIllegalStateException()
        .isThrownBy(builder::build)
        .withMessageContaining("use builder.minDate(...)");
  }

  @Test
  void unsupportedPropsRefuseThePropsSetByTheDatepicker() {
    JsVueDatePicker.Builder builder =
        JsVueDatePicker.builder()
            .model(JsHelpers.of("value"))
            .putUnsupportedProps("modelValue", JsHelpers.ofLiteral("new Date()"));

    assertThatIllegalStateException()
        .isThrownBy(builder::build)
        .withMessageContaining("set by the datepicker");
  }

  private IJsVueDatePicker options(FormComponent<?> field) {
    return ((AbstractDatePickerVueField<?>) field).getVueDatePickerBehavior().getJsVueDatePicker();
  }

  private String rendered(
      IJsVueDatePicker options, Function<IJsVueDatePicker, IJsStatement> option) {
    IJsStatement statement = option.apply(options);
    return statement != null ? statement.render() : null;
  }

  private String epochMillis(LocalDate date) {
    return String.valueOf(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
  }
}
