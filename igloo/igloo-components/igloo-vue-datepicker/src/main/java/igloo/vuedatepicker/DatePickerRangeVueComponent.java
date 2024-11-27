package igloo.vuedatepicker;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.wicket.model.CollectionCopyModel;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.Failable;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;

public class DatePickerRangeVueComponent extends AbstractTextComponent<Collection<Date>> {

  private static final long serialVersionUID = 1L;

  private final VueBehavior vueBehavior;
  private final IModel<Date> dateMinModel;
  private final IModel<Date> dateMaxModel;

  public DatePickerRangeVueComponent(
      String id, IModel<Date> dateMinModel, IModel<Date> dateMaxModel) {
    super(
        id,
        CollectionCopyModel.of(() -> List.of(dateMinModel.getObject(), dateMaxModel.getObject())));
    setOutputMarkupPlaceholderTag(true);
    setType(Date.class);
    this.dateMinModel = dateMinModel;
    this.dateMaxModel = dateMaxModel;

    vueBehavior =
        new VueBehavior(
            JsDatePicker.builder()
                .dateModel(
                    JsHelpers.of(List.of(dateMinModel.getObject(), dateMaxModel.getObject())))
                .autoApply(JsHelpers.of(true))
                .enableTimePicker(JsHelpers.of(false))
                .format(JsHelpers.of("dd/MM/yyyy"))
                .multiCalendars(JsHelpers.of(true))
                .textInput(JsHelpers.of(true))
                .build());
    add(vueBehavior);
  }

  @Override
  public void convertInput() {
    IJsDatePicker jsDatePicker = vueBehavior.getJsDatePicker();
    String[] value = getInputAsArray();

    if (ArrayUtils.isNotEmpty(value)) {
      try {
        String format =
            (jsDatePicker == null || jsDatePicker.format() == null)
                ? "MM/dd/yyyy, HH:mm"
                : jsDatePicker.format().render().replace("\"", "");

        setConvertedInput(
            Stream.of(value[0].replaceAll("[\\[\\]]", "").split(" - ", -1))
                .map(
                    Failable.asFunction(
                        dateInput -> new SimpleDateFormat(format, getLocale()).parse(dateInput)))
                .toList());

      } catch (ConversionException e) {
        error(newValidationError(e));
      }
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

  public DatePickerRangeVueComponent setJsDatePicker(IJsDatePicker jsDatePicker) {
    this.vueBehavior.setJsDatePicker(jsDatePicker);
    return this;
  }

  @Override
  public void updateModel() {
    FormComponent.updateCollectionModel(this);
    dateMinModel.setObject(getConvertedInput().stream().findFirst().orElse(null));
    dateMaxModel.setObject(
        getConvertedInput().stream().reduce((first, second) -> second).orElse(null));
  }
}
