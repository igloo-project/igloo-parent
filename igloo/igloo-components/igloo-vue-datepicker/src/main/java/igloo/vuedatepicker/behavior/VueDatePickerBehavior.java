package igloo.vuedatepicker.behavior;

import com.google.common.base.Joiner;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.statement.IJsVariable;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import igloo.vuedatepicker.reference.VueDatePickerJavaScriptResourceReference;
import igloo.vuedatepicker.reference.VueInitAppResourceReference;
import igloo.vuedatepicker.reference.VueJavaScriptDevResourceReference;
import igloo.vuedatepicker.reference.VueJavaScriptResourceReference;
import java.util.function.Predicate;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.spring.property.service.IPropertyService;

public class VueDatePickerBehavior extends Behavior {

  private static final long serialVersionUID = 1L;

  private SerializableConsumer<Builder> jsVueDatePickerConsumer;
  private IJsVueDatePicker jsVueDatePicker;

  @SpringBean private IPropertyService propertyService;

  public VueDatePickerBehavior(SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    this.jsVueDatePickerConsumer = jsVueDatePickerConsumer;
  }

  /** build jsVueDatePicker configuration options */
  @Override
  public void onConfigure(Component component) {
    super.onConfigure(component);
    if (jsVueDatePicker == null) {
      Builder builder = JsVueDatePicker.builder();
      jsVueDatePickerConsumer.accept(builder);
      jsVueDatePicker = builder.build();
    }
  }

  /** put tags on vue datepicker input whith jsVueDatePicker configuration options */
  @Override
  public void onComponentTag(Component component, ComponentTag tag) {
    super.onComponentTag(component, tag);
    tag.put("v-model", getVmodelVarName(component));
    tag.put("@update:model-value", getVueOnChangeVarName(component));
    tag.put("uid", component.getMarkupId());

    // Add tags values with variables for dynamics options
    // (variable is made in JS to have async vue ref object)
    jsVueDatePicker.values().entrySet().stream()
        .filter(e -> e.getKey().startsWith(":") || e.getKey().startsWith("@"))
        .filter(Predicate.not(e -> e.getValue() instanceof IJsVariable))
        .forEach(e -> tag.put(e.getKey(), getVueOptionVarName(component, e.getKey())));

    // Add tags values with conf value directly for non dynamics options
    jsVueDatePicker.values().entrySet().stream()
        .filter(e -> !(e.getKey().equals("v-model") || e.getKey().equals("@update:model-value")))
        .filter(
            e ->
                e.getValue() instanceof IJsVariable
                    || !(e.getKey().startsWith(":") || e.getKey().startsWith("@")))
        .forEach(e -> tag.put(e.getKey(), e.getValue().render()));
  }

  /** Init scipts JS to build vue datePicker */
  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);

    // Vue framework
    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(
                propertyService.isConfigurationTypeDevelopment()
                    ? VueJavaScriptDevResourceReference.get()
                    : VueJavaScriptResourceReference.get())));

    // Vue datepicker
    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(VueDatePickerJavaScriptResourceReference.get())));

    // initApp with datePicker
    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(VueInitAppResourceReference.get())));

    // add Vue model async from IModel wicket
    response.render(
        new PriorityHeaderItem(
            OnDomReadyHeaderItem.forScript(
                "vueInit.addVueModel('%s', %s)"
                    .formatted(getVmodelVarName(component), jsVueDatePicker.model().render()))));

    // add onChange methods JS
    IJsStatement onUpdateModelStatement = jsVueDatePicker.onUpdateModel();
    response.render(
        new PriorityHeaderItem(
            OnDomReadyHeaderItem.forScript(
                "vueInit.addVueOnChangeMethode('%s', '%s', %s)"
                    .formatted(
                        getVueOnChangeVarName(component),
                        component.getMarkupId(),
                        onUpdateModelStatement != null ? onUpdateModelStatement.render() : null))));

    // add optionals props that should be updated with vue component
    jsVueDatePicker.values().entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(":") || entry.getKey().startsWith("@"))
        .filter(Predicate.not(e -> e.getValue() instanceof IJsVariable))
        .forEach(
            entry ->
                response.render(
                    new PriorityHeaderItem(
                        OnDomReadyHeaderItem.forScript(
                            "vueInit.addVueOptionModel('%s', %s)"
                                .formatted(
                                    getVueOptionVarName(component, entry.getKey()),
                                    entry.getValue().render())))));

    response.render(
        OnDomReadyHeaderItem.forScript(
            "vueInit.mountVueAppWithId('%s')".formatted(component.getMarkupId())));

    // Replace tag "for" value in linked datePicker label if exist
    // (If not, the "for" of "wicket:for" is apply to the parent div and not to the generated input)
    response.render(
        new PriorityHeaderItem(
            OnDomReadyHeaderItem.forScript(
                "vueInit.replaceForToLabel('%s')".formatted(component.getMarkupId()))));
  }

  public IJsVueDatePicker getJsVueDatePicker() {
    return jsVueDatePicker;
  }

  public VueDatePickerBehavior addToBuilder(SerializableConsumer<Builder> consumer) {
    jsVueDatePickerConsumer = jsVueDatePickerConsumer.andThen(consumer);
    return this;
  }

  public String getVmodelVarName(Component component) {
    return component.getMarkupId();
  }

  public String getVueOnChangeVarName(Component component) {
    return component.getMarkupId() + "_onChange";
  }

  public String getVueOptionVarName(Component component, String option) {
    return Joiner.on("_")
        .join(component.getMarkupId(), option.replace(":", "").replace("-", "_").replace("@", "_"));
  }
}
