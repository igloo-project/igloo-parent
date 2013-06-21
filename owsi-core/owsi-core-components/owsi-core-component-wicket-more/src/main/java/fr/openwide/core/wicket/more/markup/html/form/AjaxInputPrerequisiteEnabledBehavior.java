package fr.openwide.core.wicket.more.markup.html.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Makes sure that the attached component is disabled and that its model is null
 * so long that a given prerequisite component's input is null or invalid.<br>
 * This behavior effectively enables sequenced input (first the user must input
 * <code>prerequisiteField</code>'s content, then the attached component's
 * content).
 * 
 * @see AbstractAjaxInputPrerequisiteBehavior
 */
public class AjaxInputPrerequisiteEnabledBehavior<T> extends AbstractAjaxInputPrerequisiteBehavior<T> {

	private static final long serialVersionUID = 3907107358503401628L;

	public AjaxInputPrerequisiteEnabledBehavior(FormComponent<T> prerequisiteField) {
		super(prerequisiteField);
	}

	@Override
	protected void setUpAttachedComponent(Component attachedComponent) {
		attachedComponent.setEnabled(true);
	}

	@Override
	protected void takeDownAttachedComponent(Component attachedComponent) {
		attachedComponent.setEnabled(false);
	}
}
