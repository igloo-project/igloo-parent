package fr.openwide.core.wicket.more.bindable.model;

import org.apache.wicket.model.IModel;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class BindableModel<T> extends ModelHolder<T> implements IBindableModel<T> {
	private static final long serialVersionUID = 3408103944536489900L;
	
	private transient boolean detaching = false;

	@SuppressWarnings("rawtypes") // Works for any T
	private static enum BindableModelWrappingFunction implements Function<IModel, IBindableModel> {
		INSTANCE;
		@Override
		@SuppressWarnings("unchecked")
		public IBindableModel apply(IModel input) {
			return input instanceof IBindableModel ? (IBindableModel) input : new BindableModel(input);
		}
		@SuppressWarnings("unchecked")
		private static <T> Function<? super IModel<T>, ? extends IBindableModel<T>> get() {
			// We are absolutely sure that the returned IBindableModel will be a IBindableModel<T>
			// We do two casts here to prevent the javac compiler to raise an error (it's a hack)
			return (Function<? super IModel<T>, ? extends IBindableModel<T>>)
					(Object) BindableModelWrappingFunction.INSTANCE;
		}
	}
	
	/*package*/ static <T> Function<? super T, ? extends IBindableModel<T>> wrap(Function<? super T, ? extends IModel<T>> function) {
		return Functions.compose(BindableModelWrappingFunction.<T>get(), function);
	}
	
	public BindableModel(IModel<T> delegate) {
		super(delegate);
	}
	
	@Override
	public IModel<T> getMainModel() {
		return this;
	}

	@Override
	public T getObject() {
		return getMainObject();
	}

	@Override
	public void setObject(T object) {
		super.getMainModel().setObject(object);
		readAllExceptMainModel();
	}

	@Override
	public void detach() {
		if (!detaching) {
			detaching = true;
			try {
				super.detach();
			} finally {
				detaching = false;
			}
		}
	}

}
