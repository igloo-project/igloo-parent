package igloo.wicket.markup.html.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

public class JavascriptEventConfirmationBehavior extends AttributeModifier {
	
	private static final long serialVersionUID = 3850346788108610792L;

	public JavascriptEventConfirmationBehavior(ResourceModel detailedMessage) {
		super("onclick", new StringResourceModel("javascript.confirmation")
				.setParameters(detailedMessage));
	}

}
