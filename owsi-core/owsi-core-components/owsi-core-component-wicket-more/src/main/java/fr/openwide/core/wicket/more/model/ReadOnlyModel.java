package fr.openwide.core.wicket.more.model;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class ReadOnlyModel<F, T> extends AbstractReadOnlyModel<T> implements IComponentAssignedModel<T> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends F> readModel;
	
	private final Function<F, T> function;
	
	public static <T> ReadOnlyModel<T, T> of(IModel<? extends T> model) {
		return new ReadOnlyModel<T, T>(model, Functions.<T>identity());
	}

	public static <T extends Serializable> ReadOnlyModel<T, T> of(T object) {
		return new ReadOnlyModel<T, T>(Model.of(object), Functions.<T>identity());
	}
	
	public static <F, T> ReadOnlyModel<F, T> of(IModel<? extends F> model, Function<F, T> function) {
		return new ReadOnlyModel<F, T>(model, function);
	}

	public static <F extends Serializable, T> ReadOnlyModel<F, T> of(F object, Function<F, T> function) {
		return new ReadOnlyModel<F, T>(Model.of(object), function);
	}

	protected ReadOnlyModel(IModel<? extends F> readModel, Function<F, T> function) {
		super();
		this.readModel = readModel;
		this.function = function;
	}

	@Override
	public T getObject() {
		return function.apply(readModel.getObject());
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
	
	private class WrapModel extends ReadOnlyModel<F, T> implements IWrapModel<T> {
		private static final long serialVersionUID = 7996314523359141428L;
		
		protected WrapModel(Component component) {
			super(wrap(readModel, component), function);
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
