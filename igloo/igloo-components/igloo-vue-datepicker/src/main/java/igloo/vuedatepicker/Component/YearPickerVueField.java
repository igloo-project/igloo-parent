package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import java.time.Year;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;

public class YearPickerVueField extends AbstractDatePickerVueField<Year> {

  private static final long serialVersionUID = 1L;

  public YearPickerVueField(String id, IModel<Year> model) {
    this(id, model, builder -> {});
  }

  public YearPickerVueField(
      String id, IModel<Year> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model, jsDatePickerConsumer);
  }

  @Override
  public void convertInput() {
    String[] values = getInputAsArray();
    if (ArrayUtils.isNotEmpty(values)) {
      String value = values[0];
      setConvertedInput(StringUtils.isBlank(value) ? null : Year.parse(value, getFormat()));
    }
  }

  @Override
  protected JsDatePicker.Builder initDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.ofYearModel(getModel()))
        .autoApply(JsHelpers.of(true))
        .format(JsHelpers.of("yyyy"))
        // TODO RFO faire une clef de conf ? Override dans projet pour recuperer clef de trad ?
        .placeholder(JsHelpers.of("aaaa"))
        .textInput(JsHelpers.of(true))
        .required(JsHelpers.of(this.isRequired()))
        .ui(JsHelpers.ofLiteral("{ input: 'form-control' }"));
  }
}
