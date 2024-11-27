package igloo.vuedatepicker;

import igloo.wicket.model.Detachables;
import java.time.Instant;
import java.util.Date;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;

public class VueBehaviorAjax extends AbstractDefaultAjaxBehavior {

  private static final long serialVersionUID = 1L;

  private final IJsDatePicker jsDatePicker;
  private final IModel<Date> dateModel;

  public VueBehaviorAjax(IJsDatePicker jsDatePicker, IModel<Date> dateModel) {
    this.jsDatePicker = jsDatePicker;
    this.dateModel = dateModel;
  }

  @Override
  public void detach(Component component) {
    super.detach(component);
    Detachables.detach(dateModel);
  }

  @Override
  public void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);
    tag.put("v-model", "dateModel");
    tag.put("@update:model-value", "onchange");

    jsDatePicker.values().entrySet().stream()
        .filter(e -> !e.getKey().equals("v-model"))
        .forEach(e -> tag.put(e.getKey(), e.getValue().render()));
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);

    response.render(JavaScriptHeaderItem.forUrl("https://unpkg.com/vue@latest"));
    response.render(JavaScriptHeaderItem.forUrl("https://unpkg.com/@vuepic/vue-datepicker@latest"));
    response.render(
        CssHeaderItem.forUrl("https://unpkg.com/@vuepic/vue-datepicker@latest/dist/main.css"));
    /*    response.render(JavaScriptHeaderItem.forReference(VueJSJavaScriptResourceReference.get()));
    response.render(
        OnDomReadyHeaderItem.forScript(
            String.format(
                "vusScript.init(%s, %s)", jsDatePicker.dateModel().render(), getCallbackUrl())));*/

    response.render(
        JavaScriptHeaderItem.forScript(
            """
            window.addEventListener('load', function() {
              const { createApp, ref } = Vue
              createApp({
                  components: { VueDatePicker },
                  data() {
                    const dateModel = ref(%s)
                    const onchange = date => {
                      console.log(date);
                      Wicket.Ajax.ajax({
                        'u':'%s',
                        'async': true,
                        'wr': false,
                        'm':'POST',
                        'ep': {'dateValue': date.valueOf()}
                      })
                    }

                    return {
                      dateModel, onchange
                    }
                  }
              }).mount("body");
            });
            """
                .formatted(jsDatePicker.dateModel().render(), getCallbackUrl()),
            "vue-datepicker-init"));
  }

  @Override
  protected void respond(AjaxRequestTarget target) {
    Long time =
        RequestCycle.get()
            .getRequest()
            .getRequestParameters()
            .getParameterValue("dateValue")
            .toOptionalLong();
    dateModel.setObject(Date.from(Instant.ofEpochMilli(time)));
  }
}
