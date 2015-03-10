package fr.openwide.core.wicket.more.markup.html.form;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.AbstractListener;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.ClassVisitFilter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitFilter;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.util.visit.Visits;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.StateEvent;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.functional.SerializablePredicate;
import fr.openwide.core.wicket.more.condition.Condition;

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
	
	private static final ResourceReference CHOICE_JS = new JQueryPluginResourceReference(
			AbstractAjaxInputPrerequisiteBehavior.class,
			"jquery.AbstractAjaxInputPrerequisiteBehavior.Choice.js"
	);

	private final FormComponent<T> prerequisiteField;
	
	private final Collection<Component> attachedComponents = Lists.newArrayList();
	
	private boolean defaultWhenPrerequisiteInvisible = true;
	
	private boolean useWicketValidation = false;
	
	private boolean updatePrerequisiteModel = false;
	
	private Predicate<? super T> resetAttachedModelPredicate = Predicates.alwaysFalse();
	
	private Predicate<? super T> resetAttachedFormComponentsPredicate = Predicates.alwaysFalse();
	
	/**
	 * @deprecated This should disappear soon, along with the {@link #cleanDefaultModelObject(Component)} method.
	 */
	@Deprecated
	private boolean resetAttachedModelOnConfigure = true;
	
	private boolean refreshParent = false;
	
	private final Collection<AbstractListener> onChangeListeners = Lists.newArrayList();
	
	private Predicate<? super T> objectValidPredicate = Predicates.notNull();
	
	private Condition forceSetUpConditon = null;
	private Condition forceTakeDownConditon = null;

	private Resetter resetter = new DefaultResetter();

	public AbstractAjaxInputPrerequisiteBehavior(FormComponent<T> prerequisiteField) {
		super();
		Args.notNull(prerequisiteField, "prerequisiteField");
		this.prerequisiteField = prerequisiteField;
	}
	
	/**
	 * Sets whether the attached component should be set up (<code>true</code>) or taken down (<code>false</code>)
	 * when the prerequisiteField is invisible.
	 * <p>Default is <code>true</code>.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setDefaultWhenPrerequisiteInvisible(boolean defaultToSetUp) {
		this.defaultWhenPrerequisiteInvisible = defaultToSetUp;
		return this;
	}

	/**
	 * Sets whether wicket validation ({@link FormComponent#validate()} method) is to be taken into account before
	 * trying to apply the {@code #setObjectValidPredicate(Predicate) objectValidPredicate}.
	 * <p><strong>WARNING:</strong> The wicket validation relies solely on input, and not on the model object. Thus any wicket validation
	 * will systematically deem a field invalid when there is no input. In particular, this means that the prerequisite field will systematically be deemed invalid on
	 * the first page rendering, which means this feature is not suitable for "editing" forms, where the form is filled before the first rendering.
	 */
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
		this.resetAttachedModelPredicate = resetAttachedModel ? Predicates.alwaysTrue() : Predicates.alwaysFalse();
		return this;
	}
	
	/**
	 * Sets whether the attached component's models are to be set to null when the prerequisite model is updated <em>and its new object is invalid</em>.
	 * @deprecated Use {@link #setResetAttachedFormComponentsIfInvalid(boolean) instead. Be aware that, on contrary to
	 * this method, the other also applies to descendants of the attached component and clears FormComponent inputs.
	 */
	@Deprecated
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedModelIfInvalid(boolean resetAttachedModel) {
		this.resetAttachedModelPredicate = new SerializablePredicate<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean apply(T input) {
				return !objectValidPredicate.apply(input);
			}
		};
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
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedModelPredicate(Predicate<? super T> resetAttachedModelPredicate) {
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
		this.resetAttachedFormComponentsPredicate = Predicates.alwaysTrue();
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
		this.resetAttachedFormComponentsPredicate = new SerializablePredicate<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean apply(T input) {
				return !objectValidPredicate.apply(input);
			}
		};
		return this;
	}

	/**
	 * Sets whether the attached component and all its children are to be reset if they are FormComponents, i.e. their
	 * model are to be set to null and their convertedInput are to be cleared, when the prerequisite model changes and
	 * the given predicate does not apply to the prerequisite model value anymore.
	 */
	public AbstractAjaxInputPrerequisiteBehavior<T> setResetAttachedFormComponentsPredicate(Predicate<? super T> resetAttachedFormComponentsPredicate) {
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
	public AbstractAjaxInputPrerequisiteBehavior<T> setObjectValidPredicate(Predicate<? super T> objectValidPredicate) {
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
	
	public AbstractAjaxInputPrerequisiteBehavior<T> onChange(AbstractListener listener) {
		this.onChangeListeners.add(listener);
		return this;
	}
	
	private static class InputPrerequisiteAjaxEventBehavior extends AjaxEventBehavior {
		private static final long serialVersionUID = -2099510409333557398L;
		
		private static final MetaDataKey<Boolean> IS_SUBMITTED_USING_THIS_BEHAVIOR = new MetaDataKey<Boolean>() {
			private static final long serialVersionUID = 1L;
		};
		
		private final Collection<AbstractAjaxInputPrerequisiteBehavior<?>> listeners = Sets.newHashSet();
		
		private final FormComponent<?> prerequisiteField;
		
		private final boolean choice;
		
		public InputPrerequisiteAjaxEventBehavior(FormComponent<?> prerequisiteField) {
			this(prerequisiteField, isChoice(prerequisiteField));
		}
		
		private InputPrerequisiteAjaxEventBehavior(FormComponent<?> prerequisiteField, boolean choice) {
			super(choice ? MouseEvent.CLICK.getEventLabel() /* Internet Explorer... */ : StateEvent.CHANGE.getEventLabel());
			this.prerequisiteField = checkNotNull(prerequisiteField);
			this.choice = choice;
		}
		
		private static boolean isChoice(Component component) {
			return (component instanceof RadioChoice) ||
					(component instanceof CheckBoxMultipleChoice) || (component instanceof RadioGroup) ||
					(component instanceof CheckGroup);
		}
		
		public boolean isInputSubmitted() {
			Boolean submitted = getComponent().getMetaData(IS_SUBMITTED_USING_THIS_BEHAVIOR);
			return submitted != null && submitted;
		}
		
		@Override
		protected void onBind() {
			super.onBind();
			Component component = getComponent();
			checkState(prerequisiteField.equals(component),
					"This behavior can only be attached to the component passed to its constructor (%s)", prerequisiteField);
			if (choice) {
				component.setRenderBodyOnly(false);
			}
		}
		
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			super.renderHead(component, response);
			if (choice) {
				response.render(JavaScriptHeaderItem.forReference(CHOICE_JS));
			}
		}

		@Override
		protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
			super.updateAjaxAttributes(attributes);
			
			FormComponent<?> component = (FormComponent<?>)getComponent();
			if (choice) {
				// Copied from AjaxFormChoiceComponentUpdatingBehavior
				attributes.getAjaxCallListeners().add(new AjaxCallListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public CharSequence getPrecondition(Component component) {
						return String.format("return OWSI.AbtractAjaxInputPrerequisiteBehavior.Choice.acceptInput('%s', attrs)", ((FormComponent<?>)component).getInputName());
					}
				});
				
				// Allow clicks on labels to work properly
				// Also makes the radio/check buttons properly change their visual aspect on IE.
				attributes.setAllowDefault(true);
				
				attributes.getDynamicExtraParameters().add(
						String.format("return OWSI.AbtractAjaxInputPrerequisiteBehavior.Choice.getInputValues('%s', attrs)", component.getInputName())
				);
			}
		}
		
		public void register(AbstractAjaxInputPrerequisiteBehavior<?> inputPrerequisiteBehavior) {
			listeners.add(inputPrerequisiteBehavior);
		}
		
		public void unregister(AbstractAjaxInputPrerequisiteBehavior<?> inputPrerequisiteBehavior) {
			listeners.remove(inputPrerequisiteBehavior);
		}
		
		@Override
		public boolean isEnabled(Component component) {
			return super.isEnabled(component) && !listeners.isEmpty();
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			getComponent().setMetaData(IS_SUBMITTED_USING_THIS_BEHAVIOR, true);
			notify(target);
		}
		
		public void notify(AjaxRequestTarget target) {
			for (AbstractAjaxInputPrerequisiteBehavior<?> listener : listeners) {
				listener.prerequisiteFieldChange(target);
			}
		}
		
		@Override
		public void detach(Component component) {
			super.detach(component);
			component.setMetaData(IS_SUBMITTED_USING_THIS_BEHAVIOR, null);
		}
	}
	
	private InputPrerequisiteAjaxEventBehavior getExistingAjaxEventBehavior() {
		return getExistingAjaxEventBehavior(prerequisiteField);
	}
	
	private InputPrerequisiteAjaxEventBehavior getExistingAjaxEventBehavior(Component component) {
		Collection<InputPrerequisiteAjaxEventBehavior> ajaxEventBehaviors = component.getBehaviors(InputPrerequisiteAjaxEventBehavior.class);
		if (ajaxEventBehaviors.isEmpty()) {
			return null;
		} else if (ajaxEventBehaviors.size() > 1) {
			throw new IllegalStateException("There should not be more than ONE InputPrerequisiteAjaxEventBehavior attached to " + component);
		} else {
			return ajaxEventBehaviors.iterator().next();
		}
	}
	
	@Override
	public final void bind(Component component) {
		if (attachedComponents.isEmpty()) {
			// Make sure that the attached component will be updated when the content of the prerequisiteField changes.
			// The following makes sure that only one ajaxEventBehavior will be attached to the prerequisiteField
			// even if multiple components are attached to different InputPrerequisiteBehavior pointing to the same prerequisiteField.
			InputPrerequisiteAjaxEventBehavior ajaxEventBehavior = getExistingAjaxEventBehavior();
			if (ajaxEventBehavior == null) {
				ajaxEventBehavior = new InputPrerequisiteAjaxEventBehavior(prerequisiteField);
				prerequisiteField.add(ajaxEventBehavior);
			}
			ajaxEventBehavior.register(this);
		}
		
		attachedComponents.add(component);
	}
	
	@Override
	public final void unbind(Component component) {
		attachedComponents.remove(component);
		
		if (attachedComponents.isEmpty()) {
			InputPrerequisiteAjaxEventBehavior ajaxEventBehavior = getExistingAjaxEventBehavior();
			
			// We no longer need ajax events anymore
			if (ajaxEventBehavior != null) {
				ajaxEventBehavior.unregister(this);
			}
		}
	}
	
	private void prerequisiteFieldChange(AjaxRequestTarget target) {
		T convertedInput = getPrerequisiteFieldConvertedInput();
		
		for (Component attachedComponent : attachedComponents) {
			target.add(getAjaxTarget(attachedComponent));
			boolean hasReset = false;
			if (resetAttachedModelPredicate.apply(convertedInput)) {
				attachedComponent.setDefaultModelObject(null);
				hasReset = true;
			}
			if (resetAttachedFormComponentsPredicate.apply(convertedInput)) {
				resetFormComponents(attachedComponent);
				hasReset = true;
			}
			if (hasReset) {
				// Handle chained prerequisites
				InputPrerequisiteAjaxEventBehavior behavior = getExistingAjaxEventBehavior(attachedComponent);
				if (behavior != null) {
					behavior.notify(target);
				}
			}
		}
		
		for (AbstractListener onChangeListener : onChangeListeners) {
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

	@Override
	public void detach(Component component) {
		super.detach(component);
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
	 * @deprecated Use {@link #onChange(AbstractListener)} instead.
	 */
	@Deprecated
	protected void onPrerequisiteFieldChange(AjaxRequestTarget target, FormComponent<T> prerequisiteField, Collection<Component> attachedComponents) {
		
	}
	
	private boolean hasPrerequisiteFieldInputChanged() {
		return prerequisiteField.isEnabledInHierarchy() && prerequisiteField.isVisibleInHierarchy()
				&& 
				(
						prerequisiteField.getForm().isSubmitted()
						|| getExistingAjaxEventBehavior().isInputSubmitted()
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
		
		if (prerequisiteField.determineVisibility()) {
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
					prerequisiteField.inputChanged();
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
					prerequisiteField.validate(); // Performs input conversion
					
					if (isCurrentModelSatisfyingRequirements(prerequisiteField, prerequisiteField.getModel())) {
						setUpAttachedComponent(component);
					} else {
						if (resetAttachedModelOnConfigure) {
							cleanDefaultModelObject(component);
						}
						takeDownAttachedComponent(component);
					}
				}
				
				// We need to clear the message that may have been added during the validation, since they are not relevant to the user (no form was submitted)
				prerequisiteField.getFeedbackMessages().clear();
			}
		} else {
			if (defaultWhenPrerequisiteInvisible) {
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
		return objectValidPredicate.apply(object);
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
