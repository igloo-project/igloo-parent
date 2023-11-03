package org.iglooproject.wicket.more.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;

import igloo.wicket.model.Detachables;

public class DataModel<T> extends LoadableDetachableModel<T> implements IDataModel<T> {

	private static final long serialVersionUID = 1L;

	public static <T> SerializableFunction2<T, IDataModel<T>> factory(SerializableSupplier2<IDataModel<T>> dataModelSupplier) {
		return o -> {
			IDataModel<T> dataModel = dataModelSupplier.get();
			dataModel.setObject(o);
			return dataModel;
		};
	}

	private final IModel<T> providerModel;

	private final List<DataModelProperty<T, ?>> properties = new ArrayList<>();

	public DataModel(IModel<T> providerModel) {
		this.providerModel = Objects.requireNonNull(providerModel);
	}

	@Override
	protected T load() {
		T object = providerModel.getObject();
		write(object);
		return object;
	}

	@Override
	public void setObject(T object) {
		super.setObject(object);
		detach();
	}

	@Override
	public <V> DataModel<T> bind(BindingRoot<? super T, V> binding, IModel<V> model) {
		DataModelProperty<T, ?> property = new DataModelProperty<>(binding, model);
		if (properties.contains(property)) {
			properties.set(properties.indexOf(property), property);
		} else {
			properties.add(property);
		}
		return this;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		getObject();
		read(getObject());
		Detachables.detach(providerModel);
		Detachables.detach(properties);
	}

	private void write(T object) {
		properties.forEach(p -> p.write(object));
	}

	private void read(T object) {
		properties.forEach(p -> p.read(object));
	}

	private static class DataModelProperty<T, V> implements IDetachable {
		
		private static final long serialVersionUID = 1L;
		
		private final BindingRoot<? super T, V> binding;
		
		private final IModel<V> model;
		
		public DataModelProperty(BindingRoot<? super T, V> binding, IModel<V> model) {
			this.binding = Objects.requireNonNull(binding);
			this.model = Objects.requireNonNull(model);
		}
		
		public void write(T object) {
			binding.setWithRoot(object, model.getObject());
		}
		
		public void read(T object) {
			model.setObject(binding.getSafelyWithRoot(object));
		}
		
		@Override
		public void detach() {
			Detachables.detach(model);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof DataModelProperty)) {
				return false;
			}
			DataModelProperty<?, ?> other = (DataModelProperty<?, ?>) obj;
			return new EqualsBuilder()
				.append(binding.getPath(), other.binding.getPath())
				.isEquals();
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
				.append(binding.getPath())
				.toHashCode();
		}
	}

}
