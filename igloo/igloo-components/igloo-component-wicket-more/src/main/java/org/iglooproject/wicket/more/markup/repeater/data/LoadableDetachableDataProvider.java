package org.iglooproject.wicket.more.markup.repeater.data;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.iglooproject.wicket.model.IBindableDataProvider;

public abstract class LoadableDetachableDataProvider<T> implements IDataProvider<T>, IBindableDataProvider {

	private static final long serialVersionUID = -1126491236540289799L;

	private Long lastFirst;

	private Long lastCount;

	private Long lastSize;

	private List<T> lastList;

	protected abstract List<T> loadList(long first, long count);

	protected abstract long loadSize();

	@Override
	public void detach() {
		lastFirst = null;
		lastCount = null;
		lastSize = null;
		lastList = null;
	}

	@Override
	public final long size() {
		if (lastSize == null) {
			lastSize = loadSize();
		}
		return lastSize;
	}

	@Override
	public final Iterator<? extends T> iterator(long first, long count) {
		long realCount = Math.min(count, size() - first);
		if (lastList != null && Long.valueOf(first).equals(lastFirst) && Long.valueOf(realCount).equals(lastCount)) {
			return lastList.iterator();
		} else {
			lastFirst = first;
			lastCount = realCount;
			lastList = loadList(first, count);
			return lastList.iterator();
		}
	}
}