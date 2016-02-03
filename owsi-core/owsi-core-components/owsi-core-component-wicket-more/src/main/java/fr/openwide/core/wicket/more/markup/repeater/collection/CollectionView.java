package fr.openwide.core.wicket.more.markup.repeater.collection;

import org.apache.wicket.markup.repeater.Item;

import fr.openwide.core.wicket.more.markup.repeater.sequence.SequenceView;

public abstract class CollectionView<T> extends SequenceView<T> {

	private static final long serialVersionUID = 1L;
	
	public CollectionView(String id, ICollectionModel<T, ?> collectionModel) {
		super(id, collectionModel);
		setDefaultModel(collectionModel);
	}
	
	@Override
	protected abstract void populateItem(Item<T> item);

	@SuppressWarnings("unchecked") // The sequence provider is known to be a ICollectionModel, see constructor
	public ICollectionModel<T, ?> getModel() {
		return (ICollectionModel<T, ?>) getSequenceProvider();
	}

}
