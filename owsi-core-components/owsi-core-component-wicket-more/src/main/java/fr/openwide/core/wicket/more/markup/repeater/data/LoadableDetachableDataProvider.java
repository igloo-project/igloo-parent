package fr.openwide.core.wicket.more.markup.repeater.data;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public abstract class LoadableDetachableDataProvider<T> implements IDataProvider<T> {

	private static final long serialVersionUID = -1126491236540289799L;

	private Integer lastFirst;

	private Integer lastCount;

	private Integer lastSize;

	private List<T> lastList;

	protected abstract List<T> loadList(int first, int count);

	protected abstract int loadSize();

	@Override
	public void detach() {
		lastFirst = null;
		lastCount = null;
		lastSize = null;
		lastList = null;
	}

	@Override
	public final int size() {
		if (lastSize == null) {
			lastSize = loadSize();
		}
		return lastSize;
	}

	@Override
	public final Iterator<? extends T> iterator(int first, int count) {
		int realCount = Math.min(count, size() - first);
		if (lastList != null && Integer.valueOf(first).equals(lastFirst) && Integer.valueOf(realCount).equals(lastCount)) {
			return lastList.iterator();
		} else {
			lastFirst = first;
			lastCount = realCount;
			lastList = loadList(first, count);
			return lastList.iterator();
		}
	}
}