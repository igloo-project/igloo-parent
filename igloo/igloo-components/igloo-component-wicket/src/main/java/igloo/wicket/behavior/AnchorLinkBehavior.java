package igloo.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AnchorLinkBehavior extends AttributeAppender {

	private static final long serialVersionUID = -1051427671590955890L;
	
	private static final String HREF_ATTRIBUTE = "href";
	private static final String SEPARATOR = "#";

	public AnchorLinkBehavior(IModel<?> appendModel) {
		super(HREF_ATTRIBUTE, appendModel, SEPARATOR);
	}
	
	public AnchorLinkBehavior(String appendString) {
		super(HREF_ATTRIBUTE, Model.of(appendString), SEPARATOR);
	}
	
	@Override
	public boolean isEnabled(Component component) {
		return component.isEnabledInHierarchy();
	}

}
