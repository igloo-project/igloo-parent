package igloo.vuedatepicker.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.test.AbstractVueDatePickerTestCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

/** The value typed in the datepicker input is converted with the format of the field. */
class DatePickerConversionTest extends AbstractVueDatePickerTestCase {

  @Test
  void localDate() {
    FormComponent<?> field = submit(id -> new LocalDatePickerVueField(id, Model.of()), "15/01/2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 15));
  }

  @Test
  void localDate_invalidInput() {
    IModel<LocalDate> model = Model.of(LocalDate.of(2026, 1, 1));

    FormComponent<?> field = submit(id -> new LocalDatePickerVueField(id, model), "not a date");

    tester.assertErrorMessages("invalid localDate");
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 1));
  }

  @Test
  void localDate_invalidInputException() {
    LocalDatePickerVueField field =
        (LocalDatePickerVueField) render(id -> new LocalDatePickerVueField(id, Model.of()));

    ConversionException exception =
        catchThrowableOfType(
            ConversionException.class, () -> field.convertValue(new String[] {"not a date"}));

    assertThat(exception.getMessage()).isEqualTo("Cannot parse 'not a date'");
    assertThat(exception.getSourceValue()).isEqualTo("not a date");
    assertThat(exception.getResourceKey()).isEqualTo("conversion.error.localDate");
  }

  @Test
  void localDate_emptyInputOnOptionalField() {
    FormComponent<?> field =
        submit(id -> new LocalDatePickerVueField(id, Model.of(LocalDate.of(2026, 1, 1))), "");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isNull();
  }

  @Test
  void localDate_emptyInputOnOptionalFieldWithoutValue() {
    FormComponent<?> field = submit(id -> new LocalDatePickerVueField(id, Model.of()), "");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isNull();
  }

  /**
   * A model holding a value gives its type to Wicket: the field format must still be used, not the
   * Wicket converter of the session locale.
   */
  @Test
  void localDate_modelWithValue_englishSession() {
    tester.getSession().setLocale(Locale.US);

    FormComponent<?> field =
        submit(id -> new LocalDatePickerVueField(id, Model.of(LocalDate.of(2026, 1, 1))), "15/01/2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 15));
  }

  @Test
  void localDateRange() {
    IModel<LocalDate> start = Model.of();
    IModel<LocalDate> end = Model.of();

    FormComponent<?> field =
        submit(
            id -> new LocalDateRangePickerVueField(id, start, end), "15/01/2026 - 20/01/2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject())
        .isEqualTo(Pair.with(LocalDate.of(2026, 1, 15), LocalDate.of(2026, 1, 20)));
  }

  @Test
  void localDateRange_singleDate() {
    FormComponent<?> field =
        submit(
            id -> new LocalDateRangePickerVueField(id, Model.of(), Model.of()), "15/01/2026");

    tester.assertErrorMessages("invalid localDate range");
    assertThat(field.getModelObject()).isNull();
  }

  @Test
  void localDateRange_modelsWithValues() {
    IModel<LocalDate> start = Model.of(LocalDate.of(2026, 1, 1));
    IModel<LocalDate> end = Model.of(LocalDate.of(2026, 1, 2));

    FormComponent<?> field =
        submit(
            id -> new LocalDateRangePickerVueField(id, start, end), "15/01/2026 - 20/01/2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject())
        .isEqualTo(Pair.with(LocalDate.of(2026, 1, 15), LocalDate.of(2026, 1, 20)));
  }

  @Test
  void localDateTime() {
    FormComponent<?> field =
        submit(id -> new LocalDateTimePickerVueField(id, Model.of()), "15/01/2026 10:30");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 30));
  }

  @Test
  void localDateTimeRange() {
    FormComponent<?> field =
        submit(
            id -> new LocalDateTimeRangePickerVueField(id, Model.of(), Model.of()),
            "15/01/2026 10:30 - 20/01/2026 18:00");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject())
        .isEqualTo(
            Pair.with(LocalDateTime.of(2026, 1, 15, 10, 30), LocalDateTime.of(2026, 1, 20, 18, 0)));
  }

  @Test
  void formatChangedWithTheBuilder() {
    FormComponent<?> field =
        submit(
            id ->
                new LocalDatePickerVueField(
                    id, Model.of(), builder -> builder.format(JsHelpers.of("yyyy-MM-dd"))),
            "2026-01-15");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 15));
  }

  @Test
  void formatChangedWithAModel() {
    FormComponent<?> field =
        submit(
            id ->
                new LocalDatePickerVueField(
                    id,
                    Model.of(),
                    builder -> builder.format(JsHelpers.ofString(Model.of("dd.MM.yyyy")))),
            "15.01.2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 15));
  }

  /** A pattern with characters escaped in JS: the field must parse with the pattern, not the JS. */
  @Test
  void formatWithEscapedCharacters() {
    FormComponent<?> field =
        submit(
            id ->
                new LocalDatePickerVueField(
                    id, Model.of(), builder -> builder.format(JsHelpers.of("dd>MM>yyyy"))),
            "15>01>2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 15));
  }

  /** A format given as a JS function cannot parse the input: getFormat() must be overridden. */
  @Test
  void formatFunction_getFormatOverridden() {
    FormComponent<?> field =
        submit(
            id ->
                new LocalDatePickerVueField(
                    id,
                    Model.of(),
                    builder ->
                        builder.format(JsHelpers.ofLiteral("date => date.toISOString().slice(0, 10)"))) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected DateTimeFormatter getFormat() {
                    return DateTimeFormatter.ISO_LOCAL_DATE;
                  }
                },
            "2026-01-15");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalDate.of(2026, 1, 15));
  }

  @Test
  void formatFunction_getFormatNotOverridden() {
    LocalDatePickerVueField field =
        (LocalDatePickerVueField)
            render(
                id ->
                    new LocalDatePickerVueField(
                        id,
                        Model.of(),
                        builder -> builder.format(JsHelpers.ofLiteral("date => String(date)"))));

    IllegalStateException exception =
        catchThrowableOfType(
            IllegalStateException.class, () -> field.convertValue(new String[] {"2026-01-15"}));

    assertThat(exception).hasMessageContaining("Option format").hasMessageContaining("string");
  }

  @Test
  void localTime() {
    FormComponent<?> field = submit(id -> new LocalTimePickerVueField(id, Model.of()), "10:30");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(LocalTime.of(10, 30));
  }

  @Test
  void yearMonth() {
    FormComponent<?> field = submit(id -> new YearMonthPickerVueField(id, Model.of()), "01/2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(YearMonth.of(2026, 1));
  }

  @Test
  void yearMonth_modelWithValue() {
    FormComponent<?> field =
        submit(id -> new YearMonthPickerVueField(id, Model.of(YearMonth.of(2025, 6))), "01/2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(YearMonth.of(2026, 1));
  }

  @Test
  void year() {
    FormComponent<?> field = submit(id -> new YearPickerVueField(id, Model.of()), "2026");

    tester.assertNoErrorMessage();
    assertThat(field.getModelObject()).isEqualTo(Year.of(2026));
  }
}
