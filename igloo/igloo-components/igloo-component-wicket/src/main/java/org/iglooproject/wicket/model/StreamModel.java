package org.iglooproject.wicket.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;

public abstract class StreamModel<T> implements IModel<Iterable<T>> {

	private static final long serialVersionUID = -1713424773189633187L;

	public static <T> StreamModel<T> of(final IModel<? extends Iterable<T>> model) {
		Objects.requireNonNull(model);
		return new StreamModel<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterable<T> getObject() {
				return model.getObject();
			}
			@Override
			public void detach() {
				super.detach();
				Detachables.detach(model);
			}
		};
	}

	private StreamModel() {
	}

	public final StreamModel<T> concat(IModel<? extends Iterable<? extends T>> firstModel) {
		return concat(this, firstModel);
	}
	
	@SafeVarargs
	public final StreamModel<T> concat(IModel<? extends Iterable<? extends T>> firstModel,
			IModel<? extends Iterable<? extends T>>... otherModels) {
		return new ConcatStreamModel(firstModel, otherModels);
	}

	private class ConcatStreamModel extends StreamModel<T> {
		
		private static final long serialVersionUID = 1L;
		
		private final List<IModel<? extends Iterable<? extends T>>> models;
		
		@SafeVarargs
		public ConcatStreamModel(IModel<? extends Iterable<? extends T>> firstModel,
				IModel<? extends Iterable<? extends T>>... otherModels) {
			super();
			this.models = ImmutableList.<IModel<? extends Iterable<? extends T>>>builder()
					.add(StreamModel.this)
					.add(firstModel)
					.add(otherModels)
					.build();
		}
		
		@Override
		public Iterable<T> getObject() {
			return Iterables.concat(
					Iterables.transform(
							models,
							Models.<Iterable<? extends T>>getObject()
					)
			);
		}
		
		@Override
		public void detach() {
			super.detach();
			Detachables.detach(models);
		}
	}

	public <S> StreamModel<S> map(SerializableFunction2<? super T, S> function) {
		return new MapStreamModel<>(function);
	}

	private class MapStreamModel<S> extends StreamModel<S> {
		
		private static final long serialVersionUID = 1L;
		
		private final SerializableFunction2<? super T, S> function;
		
		public MapStreamModel(SerializableFunction2<? super T, S> function) {
			super();
			this.function = Objects.requireNonNull(function);
		}
		
		@Override
		public Iterable<S> getObject() {
			Iterable<T> iterable = StreamModel.this.getObject();
			Objects.requireNonNull(iterable);
			return Streams.stream(iterable).map(function)::iterator;
		}
		
		@Override
		public void detach() {
			super.detach();
			Detachables.detach(StreamModel.this);
		}
	}

	public <C extends Collection<T>> IModel<C> collect(SerializableSupplier2<? extends C> supplier) {
		return new CollectModel<>(supplier);
	}

	private class CollectModel<C extends Collection<T>> implements IModel<C> {
		
		private static final long serialVersionUID = 1L;
		
		private final SerializableSupplier2<? extends C> supplier;
		
		public CollectModel(SerializableSupplier2<? extends C> supplier) {
			super();
			this.supplier = Objects.requireNonNull(supplier);
		}
		
		@Override
		public C getObject() {
			C collection = supplier.get();
			Iterables.addAll(collection, StreamModel.this.getObject());
			return collection;
		}
		
		@Override
		public void detach() {
			IModel.super.detach();
			Detachables.detach(StreamModel.this);
		}
	}

	public IModel<T> first() {
		return new FirstModel();
	}

	private class FirstModel implements IModel<T> {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public T getObject() {
			return Iterables.getFirst(StreamModel.this.getObject(), null);
		}
		
		@Override
		public void detach() {
			IModel.super.detach();
			Detachables.detach(StreamModel.this);
		}
	}

}
