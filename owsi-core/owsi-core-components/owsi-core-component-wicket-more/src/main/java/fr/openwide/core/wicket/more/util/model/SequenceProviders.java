package fr.openwide.core.wicket.more.util.model;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.sequence.ISequenceProvider;

public final class SequenceProviders {

	private SequenceProviders() {
	}
	
	public static <T> ISequenceProvider<T> forDataProvider(IDataProvider<T> dataProvider) {
		return new DataProviderSequenceProviderAdapter<T>(dataProvider);
	}
	
	public static class DataProviderSequenceProviderAdapter<T> implements ISequenceProvider<T> {
		private static final long serialVersionUID = 1L;
		
		private final IDataProvider<T> dataProvider;
		
		public DataProviderSequenceProviderAdapter(IDataProvider<T> dataProvider) {
			this.dataProvider = dataProvider;
		}
		
		@Override
		public void detach() {
			dataProvider.detach();
		}

		@Override
		public Iterator<? extends IModel<T>> iterator(final long offset, final long limit) {
			return new Iterator<IModel<T>>() {
				private Iterator<? extends T> dataIterator = dataProvider.iterator(offset, limit);
				@Override
				public boolean hasNext() {
					return dataIterator.hasNext();
				}
				@Override
				public IModel<T> next() {
					return dataProvider.model(dataIterator.next());
				}
				@Override
				public void remove() {
					dataIterator.remove();
				}
			};
		}

		@Override
		public long size() {
			return dataProvider.size();
		}
	}

}
