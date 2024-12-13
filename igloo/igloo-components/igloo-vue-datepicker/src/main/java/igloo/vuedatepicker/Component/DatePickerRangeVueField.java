package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsDatePicker;
import igloo.wicket.model.Detachables;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.Failable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.javatuples.Pair;

public class DatePickerRangeVueField
    extends AbstractDatePickerVueField<Pair<LocalDate, LocalDate>> {

  private static final long serialVersionUID = 1L;

  public DatePickerRangeVueField(
      String id, IModel<LocalDate> dateMinModel, IModel<LocalDate> dateMaxModel) {
    this(id, dateMinModel, dateMaxModel, builder -> {});
  }

  public DatePickerRangeVueField(
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
        },
        jsDatePickerConsumer);
  }

  @Override
  public String getInput() {
    return super.getInput();
  }

  @Override
  public void convertInput() {
    String[] value = getInputAsArray();

    if (ArrayUtils.isNotEmpty(value)) {
      try {
        List<LocalDate> list =
            Stream.of(value[0].replaceAll("[\\[\\]]", "").split(" - ", -1))
                .filter(StringUtils::isNotBlank)
                .map(Failable.asFunction(dateInput -> LocalDate.parse(dateInput, getFormat())))
                .toList();

        setConvertedInput(list.size() == 2 ? Pair.fromCollection(list) : Pair.with(null, null));

      } catch (ConversionException e) {
        error(newValidationError(e));
      }
    }
  }

  @Override
  protected JsDatePicker.Builder initDefaultJsDatePickerBuilder() {
    return JsDatePicker.builder()
        .dateModel(JsHelpers.of(getModel()))
        .enableTimePicker(JsHelpers.of(false))
        .range(JsHelpers.ofLiteral("{ partialRange: false }"))
        .format(JsHelpers.of("dd/MM/yyyy"))
        // TODO RFO faire une clef de conf ? Override dans projet pour recuperer clef de trad ?
        .placeholder(JsHelpers.of("jj/mm/aaaa - jj/mm/aaaa"))
        .multiCalendars(JsHelpers.of(true))
        .textInput(JsHelpers.of(true))
        .selectText(JsHelpers.of("Sélectionner"))
        .cancelText(JsHelpers.of("Annuler"))
        .ui(JsHelpers.ofLiteral("{ input: 'form-control' }"));
  }
}
