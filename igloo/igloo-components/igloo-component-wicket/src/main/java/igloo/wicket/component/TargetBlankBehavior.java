package igloo.wicket.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;

public class TargetBlankBehavior extends AttributeModifier {
	
	private static final long serialVersionUID = 9191014585354684960L;
	
	public static final String TARGET_ATTRIBUTE = "target";
	
	public static final String BLANK_VALUE = "_blank";
	
	public TargetBlankBehavior() {
		super(TARGET_ATTRIBUTE, BLANK_VALUE);
	}
	
	@Override
	public boolean isEnabled(Component component) {
		return component.isEnabled();
	}
}
