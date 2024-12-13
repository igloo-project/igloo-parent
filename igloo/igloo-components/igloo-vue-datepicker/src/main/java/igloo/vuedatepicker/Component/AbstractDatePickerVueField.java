package igloo.vuedatepicker.Component;

import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.IJsDatePicker;
import igloo.vuedatepicker.behavior.JsDatePicker;
import igloo.vuedatepicker.behavior.VueBehavior;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.form.IVueComponent;

public abstract class AbstractDatePickerVueField<T> extends AbstractTextComponent<T>
    implements IVueComponent {

  private static final long serialVersionUID = -1;

  private final VueBehavior vueBehavior;

  public AbstractDatePickerVueField(
      String id, IModel<T> model, Consumer<JsDatePicker.Builder> jsDatePickerConsumer) {
    super(id, model);
    JsDatePicker.Builder builder = initDefaultJsDatePickerBuilder();
    jsDatePickerConsumer.accept(builder);
    vueBehavior = new VueBehavior(builder);
    add(vueBehavior);
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    // Must be attached to an vue-date-picker tag
    checkComponentTag(tag, "vue-date-picker");

    // Default handling for component tag
    super.onComponentTag(tag);
  }

  protected DateTimeFormatter getFormat() {
    IJsDatePicker jsDatePicker = vueBehavior.getJsDatePicker();
    return DateTimeFormatter.ofPattern(
        (jsDatePicker == null || jsDatePicker.format() == null)
            ? "MM/dd/yyyy, HH:mm"
            : jsDatePicker.format().render().replace("\"", ""));
  }

  protected abstract JsDatePicker.Builder initDefaultJsDatePickerBuilder();

  public VueBehavior getVueBehavior() {
    return vueBehavior;
  }

  public String getVModelVarName() {
    return vueBehavior.getVmodelVarName(this);
  }

  public String getVModel() {
    return "vueInit.vModels.get('%s')".formatted(vueBehavior.getVmodelVarName(this));
  }

  public String getVueOnChangeVarName() {
    return vueBehavior.getVueOnChangeVarName(this);
  }

  public static Consumer<JsDatePicker.Builder> dateMinConsumer(
      AbstractDatePickerVueField<?> datePicker) {
    return builder -> builder.minDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName()));
  }

  public static Consumer<JsDatePicker.Builder> dateMaxConsumer(
      AbstractDatePickerVueField<?> datePicker) {
    return builder -> builder.maxDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName()));
  }

  public AbstractDatePickerVueField<?> setDateMin(AbstractDatePickerVueField<?> datePicker) {
    getVueBehavior()
        .getJsDatePickerBuilder()
        .minDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName()));
    return this;
  }

  public AbstractDatePickerVueField<?> setDateMax(AbstractDatePickerVueField<?> datePicker) {
    getVueBehavior()
        .getJsDatePickerBuilder()
        .maxDate(JsHelpers.ofJsVariable(datePicker.getVModelVarName()));
    return this;
  }

  @Override
  public void appendCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "vueInit.appendInputCssClass('%s', '%s')".formatted(getMarkupId(), cssClass));
  }

  @Override
  public void removeCssClass(AjaxRequestTarget target, String cssClass) {
    target.appendJavaScript(
        "vueInit.removeInputCssClass('%s', '%s')".formatted(getMarkupId(), cssClass));
  }

  public AbstractDatePickerVueField<?> setSelectText(IModel<String> selectTextModel) {
    if (selectTextModel != null && StringUtils.isNotBlank(selectTextModel.getObject())) {
      getVueBehavior()
          .getJsDatePickerBuilder()
          .selectText(JsHelpers.of(selectTextModel.getObject()));
    }
    return this;
  }

  public AbstractDatePickerVueField<?> setCancelText(IModel<String> cancelTextModel) {
    if (cancelTextModel != null && StringUtils.isNotBlank(cancelTextModel.getObject())) {
      getVueBehavior()
          .getJsDatePickerBuilder()
          .cancelText(JsHelpers.of(cancelTextModel.getObject()));
    }
    return this;
  }

  public JsDatePicker.Builder getJsBuilder() {
    return getVueBehavior().getJsDatePickerBuilder();
  }
}
