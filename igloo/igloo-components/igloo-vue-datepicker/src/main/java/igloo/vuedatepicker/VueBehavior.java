package igloo.vuedatepicker;

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
    tag.put("v-model", component.getMarkupId());
    tag.put("@update:model-value", component.getMarkupId() + "_onChange");

    jsDatePicker.values().entrySet().stream()
        .filter(e -> !(e.getKey().equals("v-model") || e.getKey().equals("@update:model-value")))
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
                "addVueModel('%s', %s)"
                    .formatted(component.getMarkupId(), jsDatePicker.dateModel().render()))));

    response.render(
        new PriorityHeaderItem(
            OnDomReadyHeaderItem.forScript(
                "addVueMethode('%s', '%s', %s)"
                    .formatted(
                        component.getMarkupId() + "_onChange",
                        component.getMarkupId(),
                        jsDatePicker.onUpdateModel() != null
                            ? jsDatePicker.onUpdateModel().render()
                            : null))));

    response.render(OnDomReadyHeaderItem.forScript("mountVueApp()"));
  }

  public IJsDatePicker getJsDatePicker() {
    return jsDatePicker;
  }

  public void setJsDatePicker(IJsDatePicker jsDatePicker) {
    this.jsDatePicker = jsDatePicker;
  }
}
