package igloo.wicket.condition;

import org.apache.wicket.Component;

public enum ComponentBooleanProperty {
	VISIBLE {
		@Override
		public void set(Component component, boolean value) {
			component.setVisible(value);
		}
	},
	VISIBILITY_ALLOWED {
		@Override
		public void set(Component component, boolean value) {
			component.setVisibilityAllowed(value);
		}
	},
	ENABLE {
		@Override
		public void set(Component component, boolean value) {
			component.setEnabled(value);
		}
	};
	
	public abstract void set(Component component, boolean value);
}