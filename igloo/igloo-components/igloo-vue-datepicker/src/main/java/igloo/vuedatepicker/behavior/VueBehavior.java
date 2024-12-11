package igloo.vuedatepicker.behavior;

import com.google.common.base.Joiner;
import igloo.bootstrap.js.statement.IJsVariable;
import igloo.vuedatepicker.reference.VueDatePickerCssResourceReference;
import igloo.vuedatepicker.reference.VueDatePickerJavaScriptResourceReference;
import igloo.vuedatepicker.reference.VueInitAppResourceReference;
import igloo.vuedatepicker.reference.VueJavaScriptResourceReference;
import java.util.function.Predicate;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;

public class VueBehavior extends Behavior {

  private static final long serialVersionUID = 1L;

  private IJsDatePicker jsDatePicker;

  public VueBehavior(IJsDatePicker jsDatePicker) {
    this.jsDatePicker = jsDatePicker;
  }

  @Override
  public void onComponentTag(Component component, ComponentTag tag) {
    super.onComponentTag(component, tag);
    tag.put("v-model", getVmodelVarName(component));
    tag.put("@update:model-value", getVueOnChangeVarName(component));

    jsDatePicker.values().entrySet().stream()
        .filter(e -> e.getKey().startsWith(":"))
        .filter(Predicate.not(e -> e.getValue() instanceof IJsVariable))
        .forEach(e -> tag.put(e.getKey(), getVueOptionVarName(component, e.getKey())));

    // Add references to others variables or not ref
    jsDatePicker.values().entrySet().stream()
        .filter(e -> !(e.getKey().equals("v-model") || e.getKey().equals("@update:model-value")))
        .filter(e -> e.getValue() instanceof IJsVariable || !e.getKey().startsWith(":"))
        .forEach(e -> tag.put(e.getKey(), e.getValue().render()));
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);

    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(VueJavaScriptResourceReference.get())));
    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(VueDatePickerJavaScriptResourceReference.get())));

    response.render(
        new PriorityHeaderItem(
            CssHeaderItem.forReference(VueDatePickerCssResourceReference.get())));

    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(VueInitAppResourceReference.get())));

    response.render(
        new PriorityHeaderItem(
            OnDomReadyHeaderItem.forScript(
                "vueInit.addVueModel('%s', %s)"
                    .formatted(getVmodelVarName(component), jsDatePicker.dateModel().render()))));

    response.render(
        new PriorityHeaderItem(
            OnDomReadyHeaderItem.forScript(
                "vueInit.addVueOnChangeMethode('%s', '%s', %s)"
                    .formatted(
                        getVueOnChangeVarName(component),
                        component.getMarkupId(),
                        jsDatePicker.onUpdateModel() != null
                            ? jsDatePicker.onUpdateModel().render()
                            : null))));

    // add optionals props that should be updated with vue component
    jsDatePicker.values().entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(":"))
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
  }

  public IJsDatePicker getJsDatePicker() {
    return jsDatePicker;
  }

  public void setJsDatePicker(IJsDatePicker jsDatePicker) {
    this.jsDatePicker = jsDatePicker;
  }

  public String getVmodelVarName(Component component) {
    return component.getMarkupId();
  }

  public String getVueOnChangeVarName(Component component) {
    return component.getMarkupId() + "_onChange";
  }

  public String getVueOptionVarName(Component component, String option) {
    return Joiner.on("_").join(component.getMarkupId(), option.replace(":", "").replace("-", "_"));
  }
}
