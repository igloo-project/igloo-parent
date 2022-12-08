package igloo.bootstrap.woption;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class WOptionHelpers {

	private WOptionHelpers() {}

	public static <T> IWOption<T> of(IModel<T> model) {
		return new ModelOption<>(model);
	}

	public static <T extends Serializable> IWOption<T> of(T option) {
		return new Wrapper<>(option);
	}

	@SuppressWarnings("unchecked")
	public static <T> IModel<T> asModel(IWOption<T> option) {
		if (option instanceof IModel) {
			return (IModel<T>) option;
		} else {
			return new OptionModel<>(option);
		}
	}

	private static class Wrapper<T extends Serializable> implements IWOption<T> {
		private static final long serialVersionUID = 5529415952219867345L;
		private final T option;
		public Wrapper(T option) {
			this.option = option;
		}
		@Override
		public T option() {
			return option;
		}
	}

	private static class ModelOption<T> implements IWOption<T>, IWOptionDetachable, IWOptionModel, IModel<T> {
		private static final long serialVersionUID = 1L;

		private IModel<T> model;
		public ModelOption(IModel<T> modelOption) {
			this.model = modelOption;
		}
		@Override
		public T option() {
			return model.getObject();
		}
		@Override
		public Collection<IDetachable> getDetachables() {
			return List.of(model);
		}
		@Override
		public void wrapModels(Component component) {
			if (model instanceof IComponentAssignedModel) {
				this.model = ((IComponentAssignedModel<T>) model).wrapOnAssignment(component);
			}
		}
		@Override
		public T getObject() {
			return model.getObject();
		}
		@Override
		public void detach() {
			model.detach();
			new WDetachablesVisitor().visitAndDetach(this);
		}
	}

	private static class OptionModel<T> implements IModel<T> {
		private static final long serialVersionUID = 1L;
		private IWOption<T> option;

		public OptionModel(IWOption<T> option) {
			this.option = option;
		}

		@Override
		public T getObject() {
			if (option == null) {
				return null;
			}
			return option.option();
		}

		@Override
		public void detach() {
			new WDetachablesVisitor().visitAndDetach(option);
		}
	}
}
