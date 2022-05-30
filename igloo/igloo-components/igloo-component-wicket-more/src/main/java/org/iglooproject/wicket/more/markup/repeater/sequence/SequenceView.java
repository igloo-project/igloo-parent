package org.iglooproject.wicket.more.markup.repeater.sequence;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.repeater.ISequenceProvider;
import org.iglooproject.wicket.api.util.SequenceProviders;
import org.iglooproject.wicket.more.markup.repeater.IRefreshableOnDemandRepeater;

public abstract class SequenceView<T> extends AbstractPageableView<T> implements IRefreshableOnDemandRepeater {

	private static final long serialVersionUID = 1L;
	
	private final ISequenceProvider<T> sequenceProvider;
	
	public SequenceView(String id, IDataProvider<T> dataProvider) {
		this(id, SequenceProviders.forDataProvider(dataProvider));
	}
	
	public SequenceView(String id, ISequenceProvider<T> sequenceProvider) {
		super(id);
		this.sequenceProvider = sequenceProvider;
	}
	
	@Override
	public void refreshItems() {
		onPopulate();
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		sequenceProvider.detach();
	}
	
	@Override
	protected Iterator<IModel<T>> getItemModels(long offset, long size) {
		return new ExactTypeIterator<>(sequenceProvider.iterator(offset, size));
	}
	
	private static class ExactTypeIterator<T> implements Iterator<T>  {
		private final Iterator<? extends T> delegate;
		
		public ExactTypeIterator(Iterator<? extends T> delegate) {
			super();
			this.delegate = delegate;
		}

		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		@Override
		public T next() {
			return delegate.next();
		}

		@Override
		public void remove() {
			delegate.remove();
		}

	}
	
	@Override
	protected long internalGetItemCount() {
		return sequenceProvider.size();
	}
	
	public ISequenceProvider<T> getSequenceProvider() {
		return sequenceProvider;
	}

}
