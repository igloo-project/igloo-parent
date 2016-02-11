package fr.openwide.core.commons.util.collections;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingListIterator;

public final class Iterators2 {
	
	private Iterators2() {
	}

	/**
	 * An iterator that will delegate all its calls to an iterator from <code>iterable</code>
	 * <p>Useful when you know that the user of this iterator will make structural changes to the iterable
	 * before using the iterator, but you must give an iterator right now. This gives you some kind of "joker".
	 */
	public static <T> Iterator<T> deferred(Iterable<T> iterable) {
		return iterable instanceof List
				? new DeferredListIterator<>((List<T>)iterable) : new DeferredIterator<>(iterable);
	}
	
	private static class DeferredIterator<T> extends ForwardingIterator<T> {
		private final Iterable<T> iterable;
		private Iterator<T> iterator;
		
		public DeferredIterator(Iterable<T> iterable) {
			super();
			this.iterable = iterable;
		}

		@Override
		protected Iterator<T> delegate() {
			if (iterator == null) {
				iterator = iterable.iterator();
			}
			return iterator;
		}
	}
	
	private static class DeferredListIterator<T> extends ForwardingListIterator<T> {
		private final List<T> list;
		private ListIterator<T> iterator;
		
		public DeferredListIterator(List<T> list) {
			super();
			this.list = list;
		}

		@Override
		protected ListIterator<T> delegate() {
			if (iterator == null) {
				iterator = list.listIterator();
			}
			return iterator;
		}
	}

}
