package org.iglooproject.wicket.more.markup.html.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.ClassVisitFilter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitFilter;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.util.visit.Visits;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.more.markup.html.form.observer.IFormComponentChangeObserver;
import org.iglooproject.wicket.more.markup.html.form.observer.impl.FormComponentChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.util.visit.VisitFilters;
import org.wicketstuff.wiquery.core.events.StateEvent;

import com.google.common.collect.Lists;

/**
 * Performs abstract actions on the attached component according to the actual, client-side content of a given {@link FormComponent}.<br>
 * This Behavior takes into account the fact that, in some cases, the model of a FormComponent may not reflect
 * the actual value entered by the user on the client side (especially when form validation fails).<br>
 * Upon the attached component's {@link #onConfigure(Component) configuration}, the Behavior perform one of two set of (abstract) actions
 * based on the current value for the <code>prerequisiteField</code>, which is the {@link {@link FormComponent#getConvertedInput() converted input}
 * if there was a user input, or the {@link FormComponent#getModelObject() model object} otherwise.<br>
 * <br>
 * This Behavior will automatically add an {@link AjaxEventBehavior} to the <code>prerequisiteField</code> to enable Ajax
 * updates on the attached component when the <code>prerequisiteField</code> {@link StateEvent#CHANGE changes}. You can override {@link #getAjaxTarget(Component)}
 * in order to specify which component must be added to the {@link AjaxRequestTarget target}
 * (this can be useful for example when the attached component is a Select2 drop-down choice).<br>
 * <br>
 * The <code>prerequisiteField</code> model object is guaranteed NOT to be updated upon client-side ajax calls.
 * If this is needed in your case, this Behavior is clearly overkill for you, since you do not need to inspect the <code>prerequisiteField</code>'s input.<br>
 * <br>
 * The sets of actions that will be performed upon the attached component's configuration are the following (each one can be overriden) :
 * <ul>
 * <li>{@link #setUpAttachedComponent(Component)} when {@link #shouldSetUpAttachedComponent()} is true and
 * {@link #isConvertedInputSatisfyingRequirements(FormComponent, Object)} or {@link #isCurrentModelSatisfyingRequirements(FormComponent, IModel)}
 * is true (which one is called depends on whether there was a user input or not).
 * <li>{@link #cleanDefaultModelObject(Component)} followed by {@link #takeDownAttachedComponent(Component)} otherwise
 * </ul>
 * 
 *
 * @param <T> The model object type of the <code>prerequisiteField</code>.
 */
public abstract class AbstractAjaxInputPrerequisiteBehavior<T> extends Behavior {

	private static final long serialVersionUID = 4689707482303046984L;

	private final FormComponent<T> prerequisiteField;
	
	private final IFormComponentChangeObserver observer = new IFormComponentChangeObserver() {
		private static final long serialVersionUID = 1L;
		@Override
		public void onChange(AjaxRequestTarget target) {
			AbstractAjaxInputPrerequisiteBehavior.this.onPrerequisiteFieldChange(target);
		}
	};
	
	private final Collection<Component> attachedComponents = Lists.newArrayList();
	
	private Condition defaultWhenPrerequisiteInvisibleCondition = Condition.alwaysTrue();
	
	private boolean useWicketValidation = false;
	
	private boolean updatePrerequisiteModel = false;
	
	private SerializablePredicate2<? super T> resetAttachedModelPredicate = Predicates2.alwaysFalse();
	
	private SerializablePredicate2<? super T> resetAttachedFormComponentsPredicate = Predicates2.alwaysFalse();
	
	/**
	 * @deprecated This should disappear soon, along with the {@link #cleanDefaultModelObject(Component)} method.
	 */
	@Deprecated
	private boolean resetAttachedModelOnConfigure = true;
	
	private boolean refreshParent = false;
	
	private final Collection<IListener> onChangeListeners = Lists.newArrayList();
	
	private SerializablePredicate2<? super T> objectValidPredicate = Predicates2.notNull();
	
	private Condition forceSetUpConditon = null;
	private Condition forceTakeDownConditon = null;

	private Resetter resetter = new DefaultResetter();

	public AbstractAjaxInputPrerequisiteBehavior(FormComponent<T> prerequisiteField) {
		super();
		Args.notNull(prerequisiteField, "prerequisiteField");
		this.prerequisiteField = prerequisiteField;
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(
				defaultWhenPrerequisiteInvisibleCondition,
				forceSetUpConditon,
				forceTakeDownConditon
		);
	}
	
	/**
	 * Sets whether the attached component should be set up (when condition is <code>true</code>) or taken down (when condition
	 * is <code>false</code>) when the prerequisiteField is invisible.
	 * <p>Default is <code>Condition.alwaysTrue()</code>.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setDefaultWhenPrerequisiteInvisible(Condition setUpCondition) {
		this.defaultWhenPrerequisiteInvisibleCondition = setUpCondition;
		return this;
	}
	
	/**
	 * Sets whether the attached component should be set up (<code>true</code>) or taken down (<code>false</code>)
	 * when the prerequisiteField is invisible.
	 * <p>Default is <code>true</code>.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setDefaultWhenPrerequisiteInvisible(boolean defaultToSetUp) {
		this.defaultWhenPrerequisiteInvisibleCondition = Condition.constant(defaultToSetUp);
		return this;
	}

	/**
	 * Sets whether wicket validation ({@link FormComponent#validate()} method) is to be taken into account before
	 * trying to apply the {@code #setObjectValidPredicate(Predicate) objectValidPredicate}.
	 * <p><strong>WARNING:</strong> The wicket validation relies solely on input, and not on the model object. Thus any wicket validation
	 * will systematically deem a field invalid when there is no input. In particular, this means that the prerequisite field will systematically be deemed invalid on
	 * the first page rendering, which means this feature is not suitable for "editing" forms, where the form is filled before the first rendering.
	 * @deprecated Avoid using this, for the reasons mentioned above.
	 */
	@Deprecated
	public AbstractAjaxInputPrerequisiteBehavior<T> setUseWicketValidation(boolean validatePrerequisiteInput) {
		this.useWicketValidation = validatePrerequisiteInput;
		return this;
	}
	
	/**
	 * Sets whether the prerequisite field model is to be updated when the prerequisite field input changes.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setUpdatePrerequisiteModel(boolean updatePrerequisiteModel) {
		this.updatePrerequisiteModel = updatePrerequisiteModel;
		return this;
	}
	
	/**
	 * Sets whether the attached component's models are to be set to null when the prerequisite model changes.
	 * @deprecated Use {@link #setResetAttachedFormComponentsIfInvalid(boolean) instead. Be aware that, on contrary to
	 * this method, the other also applies to descendants of the attached component and clears FormComponent inputs.
	 */
	@Deprecated
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedModel(boolean resetAttachedModel) {
		this.resetAttachedModelPredicate = resetAttachedModel ? Predicates2.alwaysTrue() : Predicates2.alwaysFalse();
		return this;
	}
	
	/**
	 * Sets whether the attached component's models are to be set to null when the prerequisite model is updated <em>and its new object is invalid</em>.
	 * @deprecated Use {@link #setResetAttachedFormComponentsIfInvalid(boolean) instead. Be aware that, on contrary to
	 * this method, the other also applies to descendants of the attached component and clears FormComponent inputs.
	 */
	@Deprecated
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedModelIfInvalid(boolean resetAttachedModel) {
		this.resetAttachedModelPredicate = input -> !objectValidPredicate.test(input);
		return this;
	}

	/**
	 * Sets a predicate to determine, based on the prerequisite model value, whether the attached component's
	 * models are to be set to null when the prerequisite model changes.
	 * <p>Note that FormComponents children will not be reset (allowing the use of FormComponentPanels, which should
	 * handle input clearing of their children themselves).
	 * @deprecated Use {@link #setResetAttachedFormComponentsIfInvalid(boolean) instead. Be aware that, on contrary to
	 * this method, the other also applies to descendants of the attached component and clears FormComponent inputs.
	 */
	@Deprecated
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedModelPredicate(SerializablePredicate2<? super T> resetAttachedModelPredicate) {
		this.resetAttachedModelPredicate = resetAttachedModelPredicate;
		return this;
	}
	
	/**
	 * Sets whether the attached component and all its children are to be reset if they are FormComponents, i.e. their
	 * model are to be set to null and their convertedInput are to be cleared, when the prerequisite model changes.
	 * <p>Note that FormComponents children will not be reset (allowing the use of FormComponentPanels, which should
	 * handle input clearing of their children themselves).
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedFormComponents() {
		this.resetAttachedFormComponentsPredicate = Predicates2.alwaysTrue();
		return this;
	}
	
	/**
	 * Sets whether the attached component and all its children are to be reset if they are FormComponents, i.e. their
	 * model are to be set to null and their convertedInput are to be cleared, when the prerequisite model changes
	 * <em>and its new object is invalid</em>.
	 * <p>Note that FormComponents children will not be reset (allowing the use of FormComponentPanels, which should
	 * handle input clearing of their children themselves).
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedFormComponentsIfInvalid() {
		this.resetAttachedFormComponentsPredicate = input -> !objectValidPredicate.test(input);
		return this;
	}

	/**
	 * Sets whether the attached component and all its children are to be reset if they are FormComponents, i.e. their
	 * model are to be set to null and their convertedInput are to be cleared, when the prerequisite model changes and
	 * the given predicate does not apply to the prerequisite model value anymore.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedFormComponentsPredicate(SerializablePredicate2<? super T> resetAttachedFormComponentsPredicate) {
		this.resetAttachedFormComponentsPredicate = resetAttachedModelPredicate;
		return this;
	}
	
	/**
	 * Sets whether the attached component's model are to be set to null when calling onConfigure if the prerequisite model is invalid.
	 * <p>This should be used only to prevent a legacy behavior.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setNoResetAttachedModelOnConfigure() {
		this.resetAttachedModelOnConfigure = false;
		return this;
	}
	
	/**
	 * Sets the predicate used to determine whether the prerequisite field value is valid.
	 * <strong>Note:</strong> the predicate must be {@link Serializable}.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setObjectValidPredicate(SerializablePredicate2<? super T> objectValidPredicate) {
		this.objectValidPredicate = objectValidPredicate;
		return this;
	}
	
	/**
	 * Sets the condition under which the attached component will be "set up" regardless of the value of the prerequisite field.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setForceSetUpCondition(Condition condition) {
		this.forceSetUpConditon = condition;
		return this;
	}

	/**
	 * Sets the condition under which the attached component will be "taken down" regardless of the value of the prerequisite field.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setForceTakeDownCondition(Condition condition) {
		this.forceTakeDownConditon = condition;
		return this;
	}
	
	/**
	 * Sets whether the ajax refresh should target the attached component's parent instead of the component itself.
	 * <p>This is useful for components that use javascript to generate siblings in the DOM tree, such as Select2.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setRefreshParent(boolean refreshParent) {
		this.refreshParent = refreshParent;
		return this;
	}
	
	/**
	 * Sets attached components resetter.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetter(Resetter resetter) {
		if (resetter != null) {
			this.resetter = resetter;
		}
		return this;
	}
	
	public AbstractAjaxInputPrerequisiteBehavior<T> onChange(IListener listener) {
		this.onChangeListeners.add(listener);
		return this;
	}
	
	
	public AbstractAjaxInputPrerequisiteBehavior<T> onChange(Collection<IListener> listeners) {
		this.onChangeListeners.addAll(listeners);
		return this;
	}
	
	@Override
	public final void bind(Component component) {
		if (attachedComponents.isEmpty()) {
			// Make sure that the attached component will be updated when the content of the prerequisiteField changes.
			FormComponentChangeAjaxEventBehavior.get(prerequisiteField).subscribe(observer);
		}
		
		attachedComponents.add(component);
	}
	
	@Override
	public final void unbind(Component component) {
		attachedComponents.remove(component);
		
		if (attachedComponents.isEmpty()) {
			FormComponentChangeAjaxEventBehavior.get(prerequisiteField).unsubscribe(observer);
		}
	}
	
	private void onPrerequisiteFieldChange(AjaxRequestTarget target) {
		T convertedInput = getPrerequisiteFieldConvertedInput();
		
		for (Component attachedComponent : attachedComponents) {
			Component reloadedComponent = getAjaxTarget(attachedComponent);
			if (VisitFilters.renderedComponents().visitObject(reloadedComponent)) {
				target.add(reloadedComponent);
			}
			
			boolean hasReset = false;
			if (resetAttachedModelPredicate.test(convertedInput)) {
				attachedComponent.setDefaultModelObject(null);
				hasReset = true;
			}
			if (resetAttachedFormComponentsPredicate.test(convertedInput)) {
				resetFormComponents(attachedComponent);
				hasReset = true;
			}
			if (hasReset) {
				// Handle chained prerequisites
				FormComponentChangeAjaxEventBehavior behavior = FormComponentChangeAjaxEventBehavior.getExisting(attachedComponent);
				if (behavior != null) {
					behavior.notify(target);
				}
			}
		}
		
		for (IListener onChangeListener : onChangeListeners) {
			target.addListener(onChangeListener);
		}
		
		onPrerequisiteFieldChange(target, prerequisiteField, Collections.unmodifiableCollection(attachedComponents));
	}
	
	private void resetFormComponents(Component attachedComponent) {
		visit(attachedComponent, resetFormComponentsVisitor, new ClassVisitFilter(FormComponent.class));
	}
	
	// Visits.visit is screwed: it does not accept Components, but only Iterable<Component>, on contrary to Visits.visitPostOrder
	private static final <T extends Component, R> R visit(Component component, IVisitor<T, R> visitor, IVisitFilter filter) {
		return Visits.visitChildren(Collections.<Component>singleton(component), visitor, filter);
	}
	
	private IVisitor<FormComponent<?>, Void> resetFormComponentsVisitor = new ResetFormComponentsVisitor();
	
	private class ResetFormComponentsVisitor implements IVisitor<FormComponent<?>, Void>, Serializable {
		private static final long serialVersionUID = 2038057558891912818L;
		
		@Override
		public void component(FormComponent<?> object, IVisit<Void> visit) {
			if (attachedComponents.contains(object)) {
				resetter.reset(object);
			} else {
				IModel<?> model = object.getDefaultModel();
				if (model != null) {
					model.setObject(null);
				}
			}
			object.clearInput();
			visit.dontGoDeeper();
		}
	}
	
	private Component getAjaxTarget(Component componentToRender) {
		if (refreshParent) {
			return componentToRender.getParent();
		} else {
			return componentToRender;
		}
	}
	
	/**
	 * Ajax call triggered by a change on the prerequisite field.
	 * Called after adding the attached components to the target, and before generating the response.
	 * @deprecated Use {@link #onChange(IListener)} instead.
	 */
	@Deprecated
	protected void onPrerequisiteFieldChange(AjaxRequestTarget target, FormComponent<T> prerequisiteField, Collection<Component> attachedComponents) {
		
	}
	
	private boolean hasPrerequisiteFieldInputChanged() {
		return prerequisiteField.isEnabledInHierarchy() && prerequisiteField.isVisibleInHierarchy()
				&& 
				(
						prerequisiteField.getForm().isSubmitted()
						|| FormComponentChangeAjaxEventBehavior.getExisting(prerequisiteField).isBeingSubmitted()
				);
	}
	
	private T getPrerequisiteFieldConvertedInput() {
		prerequisiteField.inputChanged();
		prerequisiteField.validate(); // Performs input conversion
		T converted = prerequisiteField.getConvertedInput();
		prerequisiteField.getFeedbackMessages().clear();
		return converted;
	}

	@Override
	public final void onConfigure(Component component) {
		super.onConfigure(component);
		
		if (prerequisiteField.isVisibleInHierarchy()) {
			if (forceTakeDownConditon != null && forceTakeDownConditon.applies()) {
				cleanDefaultModelObject(component);
				takeDownAttachedComponent(component);
			} else if (forceSetUpConditon != null && forceSetUpConditon.applies()) {
				setUpAttachedComponent(component);
			} else {
				if (hasPrerequisiteFieldInputChanged()) {
					// The prerequisiteField input has changed : the rendering of the attached component was triggered either by our
					// InputPrerequisiteAjaxEventBehavior or by a form submit.
					// We will decide whether the attached component should be set up or taken down based on the prerequisiteField's input.
					prerequisiteField.validate(); // Performs input conversion
					updateModelIfNecessary(prerequisiteField);
					
					if (isConvertedInputSatisfyingRequirements(prerequisiteField, prerequisiteField.getConvertedInput())) {
						setUpAttachedComponent(component);
					} else {
						if (resetAttachedModelOnConfigure) {
							cleanDefaultModelObject(component);
						}
						takeDownAttachedComponent(component);
					}
					
					// Clearing the input seems useless here, and may harm if the input is used when rendering the form component.
	//				prerequisiteField.clearInput();
				} else {
					if (useWicketValidation) {
						// TODO YRO : see if anyone is still using this, and drop it if not. This is dangerous since [...]
						// it may result in using wicket to validate input even though the form was not submitted.  
						prerequisiteField.validate(); // Performs input conversion
					}
					
					if (isCurrentModelSatisfyingRequirements(prerequisiteField, prerequisiteField.getModel())) {
						setUpAttachedComponent(component);
					} else {
						if (resetAttachedModelOnConfigure) {
							cleanDefaultModelObject(component);
						}
						takeDownAttachedComponent(component);
					}
				}

				if (useWicketValidation) {
					// We need to clear the message that may have been added during the validation, since they are not relevant to the user (no form was submitted)
					prerequisiteField.getFeedbackMessages().clear();
				}
			}
		} else {
			if (defaultWhenPrerequisiteInvisibleCondition.applies()) {
				setUpAttachedComponent(component);
			} else {
				takeDownAttachedComponent(component);
			}
		}
	}

	private void updateModelIfNecessary(FormComponent<T> prerequisiteField) {
		if (updatePrerequisiteModel) {
			prerequisiteField.updateModel();
		}
	}
	
	protected final boolean isConvertedInputSatisfyingRequirements(FormComponent<T> prerequisiteField, T convertedInput) {
		return (!useWicketValidation || prerequisiteField.isValid()) && isObjectValid(convertedInput);
	}
	
	protected final boolean isCurrentModelSatisfyingRequirements(FormComponent<T> prerequisiteField, IModel<T> currentModel) {
		return (!useWicketValidation || prerequisiteField.isValid()) && isObjectValid(currentModel.getObject());
	}
	
	protected final boolean isObjectValid(T object) {
		return objectValidPredicate.test(object);
	}

	/**
	 * @deprecated Use the various setReset*() methods instead.
	 */
	@Deprecated
	protected void cleanDefaultModelObject(Component attachedComponent) {
		resetter.reset(attachedComponent);
	}

	protected abstract void setUpAttachedComponent(Component attachedComponent);

	protected abstract void takeDownAttachedComponent(Component attachedComponent);
}
