package igloo.vuedatepicker;

import igloo.bootstrap.jsmodel.JsHelpers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;

public class DatePickerVueComponent extends AbstractTextComponent<Date> {

  private static final long serialVersionUID = 1L;
  private final JsDatePicker jsDatePicker;
  private final VueBehavior vueBehavior;

  public DatePickerVueComponent(String id, IModel<Date> model) {
    super(id, model);
    setOutputMarkupPlaceholderTag(true);
    setType(Date.class);
    jsDatePicker =
        JsDatePicker.builder()
            .dateModel(JsHelpers.of(model.getObject().getTime()))
            .autoApply(JsHelpers.of(true))
            .enableTimePicker(JsHelpers.of(false))
            .format(JsHelpers.of("dd/MM/yyyy"))
            .textInput(JsHelpers.of(true))
            .build();
    vueBehavior = new VueBehavior(jsDatePicker);
    add(vueBehavior);
  }

  @Override
  public void convertInput() {
    String format =
        jsDatePicker.format() == null ? "yyyy-MM-dd HH:mm:ss" : jsDatePicker.format().toString();
    String[] value = getInputAsArray();
    String tmp = value != null && value.length > 0 ? value[0] : null;
    try {
      new SimpleDateFormat(format, getLocale()).parse(tmp);
    } catch (ParseException e) {
      throw new ConversionException(e.getMessage());
    } catch (ConversionException e) {
      error(newValidationError(e));
    }
  }

  @Override
  protected void onBeforeRender() {
    super.onBeforeRender();
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    // Must be attached to an vue-date-picker tag
    checkComponentTag(tag, "vue-date-picker");

    // Default handling for component tag
    super.onComponentTag(tag);
  }
}
