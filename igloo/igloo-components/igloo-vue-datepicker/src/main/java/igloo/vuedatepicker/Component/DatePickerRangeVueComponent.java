package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsDatePicker;
import igloo.vuedatepicker.behavior.JsDatePicker;
import igloo.vuedatepicker.behavior.VueBehavior;
import igloo.wicket.model.Detachables;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.Failable;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.javatuples.Pair;

public class DatePickerRangeVueComponent extends AbstractTextComponent<Pair<LocalDate, LocalDate>> {

  private static final long serialVersionUID = 1L;

  private final VueBehavior vueBehavior;

  /*  private final IModel<LocalDate> dateMinModel;
  private final IModel<LocalDate> dateMaxModel;*/

  public DatePickerRangeVueComponent(
      String id, IModel<LocalDate> dateMinModel, IModel<LocalDate> dateMaxModel) {
    this(id, dateMinModel, dateMaxModel, builder -> {});
  }

  public DatePickerRangeVueComponent(
      String id,
      IModel<LocalDate> dateMinModel,
      IModel<LocalDate> dateMaxModel,
      Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(
        id,
        new Model<>() {
          private static final long serialVersionUID = 1L;

          @Override
          public Pair<LocalDate, LocalDate> getObject() {
            LocalDate dateMin = dateMinModel.getObject();
            LocalDate dateMax = dateMaxModel.getObject();
            return (dateMin != null && dateMax != null) ? Pair.with(dateMin, dateMax) : null;
          }

          @Override
          public void setObject(Pair<LocalDate, LocalDate> object) {
            if (object == null) {
              return;
            }
            dateMinModel.setObject(object.getValue0());
            dateMaxModel.setObject(object.getValue1());
          }

          @Override
          public void detach() {
            super.detach();
            Detachables.detach(dateMinModel, dateMaxModel);
          }
        });
    setType(Pair.class);
    /*    this.dateMinModel = dateMinModel;
    this.dateMaxModel = dateMaxModel;*/
    JsDatePicker.Builder builder =
        JsDatePicker.builder()
            .dateModel(JsHelpers.of(getModel()))
            .enableTimePicker(JsHelpers.of(false))
            .range(JsHelpers.of(true))
            .format(JsHelpers.of("dd/MM/yyyy"))
            .multiCalendars(JsHelpers.of(true))
            .textInput(JsHelpers.of(true))
            .selectText(JsHelpers.of("Sélectionner"))
            .cancelText(JsHelpers.of("Annuler"));
    jsDatePickerConsumer.accept(builder);
    vueBehavior = new VueBehavior(builder.build());
    add(vueBehavior);
  }

  @Override
  public void convertInput() {
    IJsDatePicker jsDatePicker = vueBehavior.getJsDatePicker();
    String[] value = getInputAsArray();

    if (ArrayUtils.isNotEmpty(value)) {
      try {
        DateTimeFormatter format =
            DateTimeFormatter.ofPattern(
                (jsDatePicker == null || jsDatePicker.format() == null)
                    ? "MM/dd/yyyy, HH:mm"
                    : jsDatePicker.format().render().replace("\"", ""));

        List<LocalDate> list =
            Stream.of(value[0].replaceAll("[\\[\\]]", "").split(" - ", -1))
                .map(Failable.asFunction(dateInput -> LocalDate.parse(dateInput, format)))
                .toList();

        setConvertedInput(Pair.fromCollection(list));

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

  //  @Override
  //  public void updateModel() {
  //    FormComponent.updateCollectionModel(this);
  //    dateMinModel.setObject(getConvertedInput().stream().findFirst().orElse(null));
  //    dateMaxModel.setObject(
  //        getConvertedInput().stream().reduce((first, second) -> second).orElse(null));
  //  }
}
