package fr.openwide.core.wicket.more.model;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.Model;

public class ReadOnlyModel<T> extends AbstractReadOnlyModel<T> implements IComponentAssignedModel<T> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends T> readModel;
	
	public static <T> ReadOnlyModel<T> of(IModel<? extends T> model) {
		return new ReadOnlyModel<T>(model);
	}

	public static <T extends Serializable> ReadOnlyModel<T> of(T object) {
		return new ReadOnlyModel<T>(Model.of(object));
	}

	protected ReadOnlyModel(IModel<? extends T> readModel) {
		super();
		this.readModel = readModel;
	}

	@Override
	public T getObject() {
		return readModel.getObject();
	}
	
	@Override
	public void detach() {
		super.detach();
		readModel.detach();
	}
	
	@Override
	public IWrapModel<T> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}
	
	private class WrapModel extends ReadOnlyModel<T> implements IWrapModel<T> {
		private static final long serialVersionUID = 7996314523359141428L;
		
		protected WrapModel(Component component) {
			super(wrap(readModel, component));
		}
		
		@Override
		public IModel<?> getWrappedModel() {
			return ReadOnlyModel.this;
		}
	}
	
	private static <T> IModel<? extends T> wrap(IModel<? extends T> model, Component component) {
		if (model instanceof IComponentAssignedModel) {
			return ((IComponentAssignedModel<? extends T>)model).wrapOnAssignment(component);
		} else {
			return model;
		}
	}

}
