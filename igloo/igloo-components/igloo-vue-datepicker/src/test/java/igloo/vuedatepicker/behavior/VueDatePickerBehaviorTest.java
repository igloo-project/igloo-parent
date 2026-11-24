package igloo.vuedatepicker.behavior;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.component.LocalDatePickerVueField;
import igloo.vuedatepicker.component.LocalDateRangePickerVueField;
import igloo.vuedatepicker.test.AbstractVueDatePickerTestCase;
import igloo.vuedatepicker.test.DatePickerTestPage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TagTester;
import org.junit.jupiter.api.Test;

/** What the behavior sends to the browser: scripts and component tag. */
class VueDatePickerBehaviorTest extends AbstractVueDatePickerTestCase {

  @Test
  void loadsProductionVueRuntimeBuild() {
    when(propertyService.isConfigurationTypeDevelopment()).thenReturn(false);

    render(id -> new LocalDatePickerVueField(id, Model.of()));

    assertThat(tester.getLastResponseAsString())
        .contains("dist/vue.runtime.global.prod.js")
        .doesNotContain("dist/vue.runtime.global.js")
        .contains("dist/vueInit.js");
  }

  @Test
  void loadsDevelopmentVueRuntimeBuildInDevelopment() {
    when(propertyService.isConfigurationTypeDevelopment()).thenReturn(true);

    render(id -> new LocalDatePickerVueField(id, Model.of()));

    assertThat(tester.getLastResponseAsString())
        .contains("dist/vue.runtime.global.js")
        .doesNotContain("dist/vue.runtime.global.prod.js");
  }

  @Test
  void rendersAnEmptyHostTagWithItsMarkupId() {
    FormComponent<?> field = render(id -> new LocalDatePickerVueField(id, Model.of()));

    TagTester tag = tester.getTagByWicketId(DatePickerTestPage.FIELD_ID);
    assertThat(tag.getName()).isEqualTo("vue-date-picker");
    assertThat(tag.getAttribute("id")).isEqualTo(field.getMarkupId());
    assertThat(tag.getValue()).isEmpty();
  }

  @Test
  void closesSelfClosingTag() {
    tester.startPage(
        new DatePickerTestPage(id -> new LocalDatePickerVueField(id, Model.of()), true));

    // otherwise the browser would put the following elements inside the datepicker tag
    assertThat(tester.getLastResponseAsString())
        .containsPattern("<vue-date-picker[^>]*></vue-date-picker>\\s*<span id=\"after-field\">");
  }

  @Test
  void mountsTheDatepickerWithTheFieldInputNameValueAndLanguage() {
    tester.getSession().setLocale(Locale.ENGLISH);
    LocalDate value = LocalDate.of(2026, 1, 15);

    FormComponent<?> field = render(id -> new LocalDatePickerVueField(id, Model.of(value)));

    assertThat(tester.getLastResponseAsString())
        .contains("vueInit.mount(\"%s\", {".formatted(field.getMarkupId()))
        .contains("inputName: \"%s\"".formatted(field.getInputName()))
        .contains("language: \"en\"")
        .contains(
            "initialValue: "
                + value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                + ",")
        .contains("props: () => ({")
        .contains("formats: {input: \"dd/MM/yyyy\"}")
        .doesNotContain("onUpdateModel");
  }

  @Test
  void passesTheOnUpdateModelCallback() {
    render(
        id ->
            new LocalDatePickerVueField(
                id,
                Model.of(),
                builder -> builder.onUpdateModel(JsHelpers.ofLiteral("value => alert(value)"))));

    assertThat(tester.getLastResponseAsString()).contains("onUpdateModel: value => alert(value)");
  }

  @Test
  void mountsACompleteRange() {
    LocalDate start = LocalDate.of(2026, 1, 15);
    LocalDate end = LocalDate.of(2026, 1, 20);

    render(id -> new LocalDateRangePickerVueField(id, Model.of(start), Model.of(end)));

    assertThat(tester.getLastResponseAsString())
        .contains(
            "initialValue: [%d,%d],"
                .formatted(
                    start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    end.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
  }

  /** VueDatePicker would display the end date alone as a start date: a partial range is empty. */
  @Test
  void mountsAPartialRangeEmpty() {
    render(
        id ->
            new LocalDateRangePickerVueField(id, Model.of(), Model.of(LocalDate.of(2026, 1, 20))));

    assertThat(tester.getLastResponseAsString()).contains("initialValue: [],");
  }

  @Test
  void enabledFieldIsNotDisabled() {
    render(id -> new LocalDatePickerVueField(id, Model.of()));

    assertThat(tester.getLastResponseAsString()).doesNotContain("disabled: true");
  }

  @Test
  void disablesTheDatepickerOfADisabledField() {
    render(
        id -> {
          LocalDatePickerVueField field = new LocalDatePickerVueField(id, Model.of());
          field.setEnabled(false);
          return field;
        });

    assertThat(tester.getLastResponseAsString()).contains("disabled: true");
  }

  @Test
  void disablesTheDatepickerOfAFieldInADisabledContainer() {
    DatePickerTestPage page =
        new DatePickerTestPage(id -> new LocalDatePickerVueField(id, Model.of()));
    page.get(DatePickerTestPage.FORM_ID).setEnabled(false);

    tester.startPage(page);

    assertThat(tester.getLastResponseAsString()).contains("disabled: true");
  }

  @Test
  void escapesTheLabels() {
    render(id -> new LocalDatePickerVueField(id, Model.of()));

    // the label contains an apostrophe
    assertThat(tester.getLastResponseAsString()).contains("hours: \"Increment l'heure\"");
  }

  /** A label may come from the user: it must not be able to close the script and inject another. */
  @Test
  void escapesUserDataInTheScript() {
    render(
        id ->
            new LocalDatePickerVueField(id, Model.of())
                .labelPlaceholder()
                .setLabel(Model.of("\"</script><script>alert(1)</script>\\")));

    assertThat(tester.getLastResponseAsString())
        .contains(
            "placeholder: \"\\\"\\u003C/script\\u003E\\u003Cscript\\u003Ealert(1)\\u003C/script\\u003E\\\\\"")
        .doesNotContain("<script>alert(1)");
  }
}
