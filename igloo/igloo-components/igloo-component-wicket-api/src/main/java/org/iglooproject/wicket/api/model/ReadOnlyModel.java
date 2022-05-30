package org.iglooproject.wicket.api.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.api.Models;
import org.iglooproject.wicket.api.factory.IDetachableFactory;
import org.iglooproject.wicket.api.util.Detachables;

public class ReadOnlyModel<F, T> implements IComponentAssignedModel<T> {

	private static final long serialVersionUID = -6272545665317639093L;

	private final IModel<? extends F> readModel;

	private final SerializableFunction2<? super F, ? extends T> function;

	public static final <F, T> IDetachableFactory<IModel<F>, IModel<T>> factory(final SerializableFunction2<? super F, T> function) {
		return new IDetachableFactory<IModel<F>, IModel<T>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public IModel<T> create(IModel<F> parameter) {
				return new ReadOnlyModel<>(parameter, function);
			}
			@Override
			public String toString() {
				return ReadOnlyModel.class.getSimpleName() + ".factory(" + function + ")";
			}
		};
	}

	public static <T> ReadOnlyModel<T, T> of(IModel<? extends T> model) {
		return new ReadOnlyModel<>(model, Functions2.<T>identity());
	}

	public static <T extends Serializable> ReadOnlyModel<T, T> of(T object) {
		return new ReadOnlyModel<>(Model.of(object), Functions2.<T>identity());
	}

	public static <F, T> ReadOnlyModel<F, T> of(IModel<F> model, SerializableFunction2<? super F, ? extends T> function) {
		return new ReadOnlyModel<>(model, function);
	}

	public static <F extends Serializable, T> ReadOnlyModel<F, T> of(F object, SerializableFunction2<? super F, ? extends T> function) {
		return new ReadOnlyModel<>(Model.of(object), function);
	}

	protected ReadOnlyModel(IModel<? extends F> readModel, SerializableFunction2<? super F, ? extends T> function) {
		super();
		checkNotNull(readModel);
		checkNotNull(function);
		this.readModel = readModel;
		this.function = function;
	}

	@Override
	public T getObject() {
		return function.apply(readModel.getObject());
	}

	@Override
	public void detach() {
		IComponentAssignedModel.super.detach();
		Detachables.detach(readModel);
	}

	@Override
	public IWrapModel<T> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}

	private class WrapModel extends ReadOnlyModel<F, T> implements IWrapModel<T> {
		private static final long serialVersionUID = 7996314523359141428L;
		
		protected WrapModel(Component component) {
			super(Models.wrap(readModel, component), function);
		}
		
		@Override
		public IModel<?> getWrappedModel() {
			return ReadOnlyModel.this;
		}
	}

}
