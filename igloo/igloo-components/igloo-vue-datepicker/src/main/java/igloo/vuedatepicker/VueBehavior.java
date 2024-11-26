package igloo.vuedatepicker;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;

// TODO ajouter le behaviour dans un TextField pour tester ou dans un formCOmponent
// Comment avoir une seule application vue
// Wicket à un ressouce settings avec un comparator d'headerItem -> PriorityFirstComparator ->
// PriorityHeaderItem
// Le nom du model doit être unique, donc utilser la valeur du markup ID ou le name du formComponent
// pour le nom de la variable
// Faire 3 JavascriptRessourceReference -> 1 style (WebjarsCssResourceReference), 1 init App Vue, 1
// datePicker
// importer le webjar de vue et de vue-datepicker et ajouter les lien vers le js
// Ajouter un RessourceREfernce pour l'init de l'app
// VOir photo tableau pour les RR

public class VueBehavior extends Behavior {

  private static final long serialVersionUID = 1L;

  private final IJsDatePicker jsDatePicker;

  public VueBehavior(IJsDatePicker jsDatePicker) {
    this.jsDatePicker = jsDatePicker;
  }

  @Override
  public void onComponentTag(Component component, ComponentTag tag) {
    super.onComponentTag(component, tag);
    tag.put("v-model", component.getMarkupId());

    jsDatePicker.values().entrySet().stream()
        .filter(e -> !e.getKey().equals("v-model"))
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
                String.format(
                    "addVueModel('%s', %s)",
                    component.getMarkupId(), jsDatePicker.dateModel().render()))));
    response.render(OnDomReadyHeaderItem.forScript("mountVueApp()"));
    /*    response.render(JavaScriptHeaderItem.forReference(VueJSJavaScriptResourceReference.get()));
    response.render(
        OnDomReadyHeaderItem.forScript(
            String.format(
                "vusScript.init(%s, %s)", jsDatePicker.dateModel().render(), getCallbackUrl())));*/

    /*    response.render(
    JavaScriptHeaderItem.forScript(
        """
    window.addEventListener('load', function() {
      const { createApp, ref } = Vue
      createApp({
          components: { VueDatePicker },
          data() {
            const dateModel = ref(%s)
            return {
              dateModel
            }
          }
      }).mount("body");
    });
    """
            .formatted(jsDatePicker.dateModel().render()),
        "vue-datepicker-init"));*/
  }
}
