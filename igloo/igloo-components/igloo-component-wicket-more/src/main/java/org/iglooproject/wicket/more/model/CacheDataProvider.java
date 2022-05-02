package org.iglooproject.wicket.more.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Predicates2;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class CacheDataProvider<T> implements IDataProvider<T>, IBindableDataProvider {

	private static final long serialVersionUID = 6701302381473177093L;

	private boolean cached = false;
	private boolean cachedSize = false;

	private final IDataProvider<T> reference;
	private final List<IModel<T>> cache = Lists.newArrayList();
	private long cacheSize = 0;

	private Long lastFirst;
	private Long lastCount;
	
	public static <T> CacheDataProvider<T> of(IDataProvider<T> reference) {
		return new CacheDataProvider<>(reference);
	}

	public CacheDataProvider(IDataProvider<T> reference) {
		super();
		this.reference = checkNotNull(reference);
	}

	@Override
	public IModel<T> model(T object) {
		IModel<T> model = reference.model(object);
		return cache.stream().filter(Predicates2.equalTo(model)).findFirst().orElse(model);
	}

	@Override
	public void detach() {
		reference.detach();
		for (IModel<T> model : cache) {
			model.detach();
		}
	}

	@Override
	public Iterator<? extends T> iterator(long first, long count) {
		long realCount = Math.min(count, size() - first);
		if (!cached || !Long.valueOf(first).equals(lastFirst) || !Long.valueOf(realCount).equals(lastCount)) {
			read(first, count);
		}
		lastFirst = first;
		lastCount = realCount;
		return Iterables.transform(cache, new ModelToObjectFunction()).iterator();
	}

	@Override
	public long size() {
		if (!cachedSize) {
			cacheSize = reference.size();
			cachedSize = true;
		}
		return cacheSize;
	}

	public void read(long first, long count) {
		cacheSize = size();
		
		cache.clear();
		cache.addAll(Lists.newArrayList(Iterables.transform(Lists.newArrayList(reference.iterator(first, count)), new ObjectToModelFunction())));
		
		cached = true;
		cachedSize = true;
	}
	
	public void reset() {
		cached = false;
		cachedSize = false;
		cache.clear();
		cacheSize = 0;
	}

	public IDataProvider<T> getReference() {
		return reference;
	}

	private class ObjectToModelFunction implements Function2<T, IModel<T>> {
		@Override
		public IModel<T> apply(T input) {
			return input != null ? model(input) : null;
		}
	}

	private class ModelToObjectFunction implements Function2<IModel<T>, T> {
		@Override
		public T apply(IModel<T> input) {
			return input != null ? input.getObject() : null;
		}
	}

}
