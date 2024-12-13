package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.YearMonth;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;

public class MonthYearPickerVueField extends AbstractDatePickerVueField<YearMonth> {

  private static final long serialVersionUID = 1L;

  public MonthYearPickerVueField(String id, IModel<YearMonth> model) {
    this(id, model, builder -> {});
  }

  public MonthYearPickerVueField(
      String id, IModel<YearMonth> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] values = getInputAsArray();
    if (ArrayUtils.isNotEmpty(values)) {
      String value = values[0];
      setConvertedInput(StringUtils.isBlank(value) ? null : YearMonth.parse(value, getFormat()));
    }
  }

  @Override
  protected JsDatePicker.Builder initDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.ofYearMonthModel(getModel()))
        .autoApply(JsHelpers.of(true))
        .format(JsHelpers.of("MM/yyyy"))
        // TODO RFO faire une clef de conf ? Override dans projet pour recuperer clef de trad ?
        .placeholder(JsHelpers.of("mm/aaaa"))
        .textInput(JsHelpers.of(true))
        .required(JsHelpers.of(this.isRequired()))
        .ui(JsHelpers.ofLiteral("{ input: 'form-control' }"));
  }
}
