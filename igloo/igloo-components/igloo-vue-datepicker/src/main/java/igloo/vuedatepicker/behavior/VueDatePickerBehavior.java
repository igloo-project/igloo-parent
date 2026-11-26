package igloo.vuedatepicker.behavior;

import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.statement.JsMapping;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.behavior.JsVueDatePicker.Builder;
import igloo.vuedatepicker.reference.VueInitAppResourceReference;
import igloo.vuedatepicker.reference.VueJavaScriptDevResourceReference;
import igloo.vuedatepicker.reference.VueJavaScriptResourceReference;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.spring.property.service.IPropertyService;

/**
 * Renders a VueDatePicker in the tag of a {@link FormComponent}.
 *
 * <p>The tag is the host of the datepicker: it is rendered empty, and the script {@code
 * vueInit.mount(...)} renders the datepicker inside it on DOM ready, on page load as on each Ajax
 * refresh. The datepicker input bears the input name of the component, so its value is submitted
 * and converted as for a text field.
 */
public class VueDatePickerBehavior extends Behavior {

  private static final long serialVersionUID = 1L;

  private SerializableConsumer<Builder> jsVueDatePickerConsumer;
  private IJsVueDatePicker jsVueDatePicker;

  @SpringBean private IPropertyService propertyService;

  public VueDatePickerBehavior(SerializableConsumer<Builder> jsVueDatePickerConsumer) {
    this.jsVueDatePickerConsumer = jsVueDatePickerConsumer;
  }

  @Override
  public void bind(Component component) {
    super.bind(component);
    // the script finds the host by its id
    component.setOutputMarkupId(true);
  }

  /** Builds the options once, when the component and its resources are available. */
  @Override
  public void onConfigure(Component component) {
    super.onConfigure(component);
    if (jsVueDatePicker == null) {
      Builder builder = JsVueDatePicker.builder();
      jsVueDatePickerConsumer.accept(builder);
      jsVueDatePicker = builder.build();
    }
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);

    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(
                propertyService.isConfigurationTypeDevelopment()
                    ? VueJavaScriptDevResourceReference.get()
                    : VueJavaScriptResourceReference.get())));
    response.render(
        new PriorityHeaderItem(
            JavaScriptHeaderItem.forReference(VueInitAppResourceReference.get())));

    response.render(
        OnDomReadyHeaderItem.forScript(
            "vueInit.mount(%s, %s);"
                .formatted(
                    JsHelpers.of(component.getMarkupId()).render(),
                    mountConfig(component).render())));
  }

  /** Configuration of the datepicker, see DatePickerConfig in vueInit.ts. */
  private IJsStatement mountConfig(Component component) {
    JsMapping.Builder config =
        JsHelpers.mapping()
            .putValues("inputName", JsHelpers.of(((FormComponent<?>) component).getInputName()))
            .putValues("language", JsHelpers.of(component.getLocale().getLanguage()))
            .putValues("initialValue", jsVueDatePicker.model())
            // a function, so that props reading other datepicker values follow them
            .putValues(
                "props", JsHelpers.ofLiteral("() => (%s)".formatted(jsVueDatePicker.render())));
    if (jsVueDatePicker.onUpdateModel() != null) {
      config.putValues("onUpdateModel", jsVueDatePicker.onUpdateModel());
    }
    if (jsVueDatePicker.slots() != null) {
      config.putValues("slots", jsVueDatePicker.slots());
    }
    // Wicket ignores the input of a disabled field: the datepicker must not be editable either
    if (!component.isEnabledInHierarchy()) {
      config.putValues("disabled", JsHelpers.of(true));
    }
    return config.build();
  }

  /** Options of the datepicker, available once the component is configured. */
  public IJsVueDatePicker getJsVueDatePicker() {
    return jsVueDatePicker;
  }

  /** Adds options, applied after the previous ones. Must be called before the first render. */
  public VueDatePickerBehavior addToBuilder(SerializableConsumer<Builder> consumer) {
    jsVueDatePickerConsumer = jsVueDatePickerConsumer.andThen(consumer);
    return this;
  }
}
