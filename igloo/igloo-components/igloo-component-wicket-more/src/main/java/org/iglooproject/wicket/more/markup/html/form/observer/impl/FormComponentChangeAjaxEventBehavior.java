package org.iglooproject.wicket.more.markup.html.form.observer.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import com.google.common.collect.Sets;
import java.util.Collection;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxAttributeName;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes.Method;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.iglooproject.wicket.more.markup.html.form.IVueComponent;
import org.iglooproject.wicket.more.markup.html.form.observer.IFormComponentChangeObservable;
import org.iglooproject.wicket.more.markup.html.form.observer.IFormComponentChangeObserver;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import org.wicketstuff.wiquery.core.events.StateEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

/**
 * A behavior for notifying observers when changes occur on a given {@link FormComponent}.
 *
 * <p>This behavior differs from {@link AjaxFormComponentUpdatingBehavior} and {@link
 * AjaxFormChoiceComponentUpdatingBehavior} in that:
 *
 * <ul>
 *   <li>It's more low-level: it does not presume of the actions to be executed on change. Only
 *       {@link FormComponent#inputChanged()} is called on change, the calls to {@link
 *       FormComponent#validate()}, {@link FormComponent#valid()}, {@link
 *       FormComponent#updateModel()}, and so on being the responsibility of the observers (if they
 *       want to).
 *   <li>It supports both choice and non-choice components
 *   <li>It supports choice components whose markup ID was not rendered (it relies on the form's
 *       markup ID). This allows using <code>radioGroup.setRenderBodyOnly(true)</code>, in
 *       particular.
 *   <li>It supports binding multiple, independent observers to the same {@link FormComponent}, in
 *       which case only one Ajax call will be made for all the observers.
 * </ul>
 */
public class FormComponentChangeAjaxEventBehavior extends AjaxEventBehavior
    implements IFormComponentChangeObservable {
  private static final long serialVersionUID = -2099510409333557398L;

  public static IFormComponentChangeObservable get(FormComponent<?> component) {
    FormComponentChangeAjaxEventBehavior ajaxEventBehavior = getExisting(component);
    if (ajaxEventBehavior == null) {
      ajaxEventBehavior = new FormComponentChangeAjaxEventBehavior((FormComponent<?>) component);
      component.add(ajaxEventBehavior);
    }
    return ajaxEventBehavior;
  }

  public static FormComponentChangeAjaxEventBehavior getExisting(Component component) {
    Collection<FormComponentChangeAjaxEventBehavior> ajaxEventBehaviors =
        component.getBehaviors(FormComponentChangeAjaxEventBehavior.class);
    if (ajaxEventBehaviors.isEmpty()) {
      return null;
    } else if (ajaxEventBehaviors.size() > 1) {
      throw new IllegalStateException(
          "There should not be more than ONE FormComponentChangeAjaxEventBehavior attached to "
              + component);
    } else {
      return ajaxEventBehaviors.iterator().next();
    }
  }

  private static final MetaDataKey<Boolean> IS_SUBMITTED_USING_THIS_BEHAVIOR =
      new MetaDataKey<Boolean>() {
        private static final long serialVersionUID = 1L;
      };

  private final Collection<IFormComponentChangeObserver> observers = Sets.newHashSet();

  private final FormComponent<?> prerequisiteField;

  private final boolean choice;
  private final boolean vueComponent;

  private FormComponentChangeAjaxEventBehavior(FormComponent<?> prerequisiteField) {
    this(
        prerequisiteField, isChoice(prerequisiteField), prerequisiteField instanceof IVueComponent);
  }

  private FormComponentChangeAjaxEventBehavior(
      FormComponent<?> prerequisiteField, boolean choice, boolean vueComponent) {
    super(
        choice
            ? MouseEvent.CLICK.getEventLabel() /* Internet Explorer... */
            : StateEvent.CHANGE.getEventLabel());
    this.prerequisiteField = checkNotNull(prerequisiteField);
    this.choice = choice;
    this.vueComponent = vueComponent;
  }

  private static boolean isChoice(Component component) {
    return (component instanceof RadioChoice)
        || (component instanceof CheckBoxMultipleChoice)
        || (component instanceof RadioGroup)
        || (component instanceof CheckGroup);
  }

  @Override
  protected void onBind() {
    super.onBind();
    Component component = getComponent();
    checkState(
        prerequisiteField.equals(component),
        "This behavior can only be attached to the component passed to its constructor (%s)",
        prerequisiteField);
    if (choice) {
      component.setRenderBodyOnly(false);
    }
  }

  protected FormComponent<?> getFormComponent() {
    return (FormComponent<?>) getComponent();
  }

  /* Due to the fact that, for choice components, events are attached to the form and not to the component itself,
   * we must remove the handlers on ajax refreshes.
   * Thus we need a unique event name, so that we can call $('#formId').off('click.my.unique.namespace')
   */
  protected String getUniqueEventName() {
    return getEvent() + ".formComponentChange." + getComponent().getMarkupId();
  }

  @Override
  protected CharSequence getCallbackScript(Component component) {
    if (choice) {
      /* Due to the fact that, for choice components, events are attached to the form and not to the component itself,
       * we must remove the handlers on ajax refreshes.
       * See also: getUniqueEventName(), updateAjaxAttributes(), postprocessConfiguration()
       */
      return new StringBuilder()
          .append(
              new JsStatement()
                  .$(component.findParent(Form.class))
                  .chain("off", JsUtils.quotes(getUniqueEventName(), true))
                  .render(true))
          .append(super.getCallbackScript(component));
    }
    return super.getCallbackScript(component);
  }

  @Override
  protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
    super.updateAjaxAttributes(attributes);

    attributes.setMethod(Method.POST);

    /* Allows all sort of things to work properly:
     *  * allows clicks on labels to work properly
     *  * makes the radio/check buttons properly change their visual aspect on IE.
     */
    attributes.setPreventDefault(false);

    // Vue component can have multiple element before input tag
    if (vueComponent) {
      // Copied from AjaxFormChoiceComponentUpdatingBehavior
      attributes.setSerializeRecursively(true);
      attributes
          .getAjaxCallListeners()
          .add(
              new AjaxCallListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public CharSequence getPrecondition(Component component) {
                  return String.format(
                      "return attrs.event.target.name === '%s'", getFormComponent().getInputName());
                }
              });
    }
    if (choice) {
      // For explanations, see: getUniqueEventName(), getCallbackScript(),
      // postprocessConfiguration()
      attributes.setEventNames(getUniqueEventName());

      // Copied from AjaxFormChoiceComponentUpdatingBehavior
      attributes.setSerializeRecursively(true);
      attributes
          .getAjaxCallListeners()
          .add(
              new AjaxCallListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public CharSequence getPrecondition(Component component) {
                  return String.format(
                      "return attrs.event.target.name === '%s'", getFormComponent().getInputName());
                }
              });
    }
  }

  @Override
  protected void postprocessConfiguration(JSONObject attributesJson, Component component)
      throws JSONException {
    super.postprocessConfiguration(attributesJson, component);
    if (choice) {
      /* RadioGroups *may* not have an ID in the resulting HTML, so we must attach the handler to each
       * input with the correct name in the same form.
       * See also: getUniqueEventName(), getCallbackScript(), updateAjaxAttributes()
       */
      attributesJson.put(
          AjaxAttributeName.MARKUP_ID.jsonName(), component.findParent(Form.class).getMarkupId());
      attributesJson.put(
          AjaxAttributeName.CHILD_SELECTOR.jsonName(),
          "input[name=\"" + ((FormComponent<?>) component).getInputName() + "\"]");
    }

    // Vue component can have multiple element before input tag
    // add child selector 'sel' with input in ajax attributes
    if (vueComponent) {
      attributesJson.put(
          AjaxAttributeName.CHILD_SELECTOR.jsonName(),
          "input[name=\"" + ((FormComponent<?>) component).getInputName() + "\"]");
    }
  }

  @Override
  public boolean isEnabled(Component component) {
    return super.isEnabled(component) && !observers.isEmpty();
  }

  @Override
  protected void onEvent(AjaxRequestTarget target) {
    getComponent().setMetaData(IS_SUBMITTED_USING_THIS_BEHAVIOR, true);
    getFormComponent().inputChanged();
    notify(target);
  }

  @Override
  public void detach(Component component) {
    super.detach(component);
    component.setMetaData(IS_SUBMITTED_USING_THIS_BEHAVIOR, null);
  }

  @Override
  public void subscribe(IFormComponentChangeObserver observer) {
    observers.add(observer);
  }

  @Override
  public void unsubscribe(IFormComponentChangeObserver observer) {
    observers.remove(observer);
  }

  @Override
  public boolean isBeingSubmitted() {
    Boolean submitted = getComponent().getMetaData(IS_SUBMITTED_USING_THIS_BEHAVIOR);
    return submitted != null && submitted;
  }

  @Override
  public void notify(AjaxRequestTarget target) {
    for (IFormComponentChangeObserver observer : observers) {
      observer.onChange(target);
    }
  }
}
