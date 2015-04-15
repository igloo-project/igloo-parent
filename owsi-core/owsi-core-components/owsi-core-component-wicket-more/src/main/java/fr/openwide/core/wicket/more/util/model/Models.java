package fr.openwide.core.wicket.more.util.model;

import org.apache.wicket.model.IModel;

public final class Models {
	
	private Models() {
	}

	/**
	 * A "placeholder" model, for use when the actual behavior of getObject() and setObject() do not matter.
	 * 
	 * <p>{@link #getObject()} always returns null and {@link #setObject(Object)} and {@link #detach()} do nothing.
	 */
	@SuppressWarnings("unchecked") // Works for any T
	public static <T> IModel<T> placeholder() {
		return (IModel<T>) PlaceholderModel.INSTANCE;
	}
		
	private enum PlaceholderModel implements IModel<Object> {
		INSTANCE;
		private Object readResolve() {
			return INSTANCE;
		}
		@Override
		public Object getObject() {
			return null;
		}
		
		@Override
		public void setObject(Object object) {
			// Does nothing
		}
		
		@Override
		public void detach() {
			// Does nothing
		}
	};
}
