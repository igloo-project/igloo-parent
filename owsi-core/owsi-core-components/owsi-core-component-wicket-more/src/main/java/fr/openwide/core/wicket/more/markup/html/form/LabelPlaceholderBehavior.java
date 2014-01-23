package fr.openwide.core.wicket.more.markup.html.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ComponentModel;
import org.apache.wicket.model.IModel;

public class LabelPlaceholderBehavior extends AttributeModifier {

	private static final long serialVersionUID = 7392345869664046823L;

	private static final String PLACEHOLDER_ATTRIBUTE = "placeholder";

	public LabelPlaceholderBehavior() {
		super(PLACEHOLDER_ATTRIBUTE, new LabelPlaceholderModel());
	}

	private static class LabelPlaceholderModel extends ComponentModel<String> {
		private static final long serialVersionUID = 8627941143273996086L;
		
		@Override
		protected String getObject(Component component) {
			if (!(component instanceof FormComponent)) {
				throw new IllegalStateException("Behavior " + getClass().getName()
						+ " can only be added to an instance of a FormComponent");
			}
			FormComponent<?> formComponent = (FormComponent<?>) component;
			IModel<String> labelModel = formComponent.getLabel();
			return labelModel == null ? null : labelModel.getObject();
		}
	}
}
