package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.LocalDate;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;

public class DatePickerVueField extends AbstractDatePickerVueField<LocalDate> {

  private static final long serialVersionUID = 1L;

  public DatePickerVueField(String id, IModel<LocalDate> model) {
    this(id, model, builder -> {});
  }

  public DatePickerVueField(
      String id, IModel<LocalDate> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] values = getInputAsArray();
    if (ArrayUtils.isNotEmpty(values)) {
      String value = values[0];
      setConvertedInput(StringUtils.isBlank(value) ? null : LocalDate.parse(value, getFormat()));
    }
  }

  @Override
  protected JsDatePicker.Builder initDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.ofLocalDateModel(getModel()))
        .autoApply(JsHelpers.of(true))
        .enableTimePicker(JsHelpers.of(false))
        .format(JsHelpers.of("dd/MM/yyyy"))
        // TODO RFO faire une clef de conf ? Override dans projet pour recuperer clef de trad ?
        .placeholder(JsHelpers.of("jj/mm/aaaa"))
        .textInput(JsHelpers.of(true))
        .required(JsHelpers.of(this.isRequired()))
        .ui(JsHelpers.ofLiteral("{ input: 'form-control' }"));
  }
}
