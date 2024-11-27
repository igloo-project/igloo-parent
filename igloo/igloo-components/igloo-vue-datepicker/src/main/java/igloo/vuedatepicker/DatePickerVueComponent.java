package igloo.vuedatepicker;

import igloo.bootstrap.jsmodel.JsHelpers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;

public class DatePickerVueComponent extends AbstractTextComponent<Date> {

  private static final long serialVersionUID = 1L;

  private final VueBehavior vueBehavior;

  // TODO RFO :
  //  - DatePicker en erreur disparait
  //  - Recharger un composant en ajax... disparait
  //  - DatePicker range : comment ajouter plusieurs models dans un FormComponant ?
  //  - verifier les imports webjars dans les logs
  // - passer en localDate !!!!!!!!

  public DatePickerVueComponent(String id, IModel<Date> model) {
    super(id, model);
    // setOutputMarkupPlaceholderTag(true);
    setType(Date.class);

    vueBehavior =
        new VueBehavior(
            JsDatePicker.builder()
                .dateModel(JsHelpers.of(model.getObject()))
                .autoApply(JsHelpers.of(true))
                .enableTimePicker(JsHelpers.of(false))
                .format(JsHelpers.ofLiteral("dd/MM/yyyy"))
                .textInput(JsHelpers.of(true))
                .onUpdateModel(JsHelpers.ofLiteral("value => console.log(value)"))
                .build());
    add(vueBehavior);
  }

  @Override
  public void convertInput() {
    IJsDatePicker jsDatePicker = vueBehavior.getJsDatePicker();
    String[] value = getInputAsArray();
    if (ArrayUtils.isNotEmpty(value)) {
      String format =
          (jsDatePicker == null || jsDatePicker.format() == null)
              ? "MM/dd/yyyy, HH:mm"
              : jsDatePicker.format().render();
      String tmp = value.length > 0 ? value[0] : null;
      try {
        setConvertedInput(new SimpleDateFormat(format, getLocale()).parse(tmp));
      } catch (ParseException e) {
        throw new ConversionException(e.getMessage());
      } catch (ConversionException e) {
        error(newValidationError(e));
      }
    }
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    // Must be attached to an vue-date-picker tag
    checkComponentTag(tag, "vue-date-picker");

    // Default handling for component tag
    super.onComponentTag(tag);
  }

  public DatePickerVueComponent setJsDatePicker(IJsDatePicker jsDatePicker) {
    this.vueBehavior.setJsDatePicker(jsDatePicker);
    return this;
  }
}
