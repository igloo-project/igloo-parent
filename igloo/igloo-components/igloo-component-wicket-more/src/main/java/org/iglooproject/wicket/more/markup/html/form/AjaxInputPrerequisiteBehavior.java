package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.condition.ComponentBooleanProperty;

/**
 * Makes sure that the attached component is disabled and that its model is null
 * so long that a given prerequisite component's input is null or invalid.<br>
 * This behavior effectively enables sequenced input (first the user must input
 * <code>prerequisiteField</code>'s content, then the attached component's
 * content).
 * 
 * @see AbstractAjaxInputPrerequisiteBehavior
 */
public class AjaxInputPrerequisiteBehavior<T> extends AbstractAjaxInputPrerequisiteBehavior<T> {

	private static final long serialVersionUID = 3907107358503401628L;

	private final ComponentBooleanProperty property;

	public AjaxInputPrerequisiteBehavior(FormComponent<T> prerequisiteField, ComponentBooleanProperty property) {
		super(prerequisiteField);
		this.property = Args.notNull(property, "property");
	}

	@Override
	protected void setUpAttachedComponent(Component attachedComponent) {
		property.set(attachedComponent, true);
	}

	@Override
	protected void takeDownAttachedComponent(Component attachedComponent) {
		property.set(attachedComponent, false);
	}
}
