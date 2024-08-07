package org.iglooproject.wicket.more.markup.html.form;

import igloo.wicket.condition.ComponentBooleanProperty;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Makes sure that the attached component is hidden and that its model is null so long that a given
 * prerequisite component's input is null or invalid.<br>
 * This behavior effectively enables sequenced input (first the user must input <code>
 * prerequisiteField</code>'s content, then the attached component's content).
 *
 * @see AjaxInputPrerequisiteBehavior
 */
public class AjaxInputPrerequisiteVisibilityAllowedBehavior<T>
    extends AjaxInputPrerequisiteBehavior<T> {

  private static final long serialVersionUID = -745132571908314845L;

  public AjaxInputPrerequisiteVisibilityAllowedBehavior(FormComponent<T> prerequisiteField) {
    super(prerequisiteField, ComponentBooleanProperty.VISIBILITY_ALLOWED);
  }
}
