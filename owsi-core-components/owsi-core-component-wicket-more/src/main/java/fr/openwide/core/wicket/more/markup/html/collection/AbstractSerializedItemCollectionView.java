package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * An {@link AbstractGenericCollectionView} that uses item models which serialize and deserialize the item object when the page is serialized/deserialized.
 * <p>This view and its subclasses are especially useful when dealing with lists of non-database objects, such as {@link Date Dates}, {@link Enum Enums}, and
 * when item models must refer to the same value even when the list changed between a server response and the next client request (for example, when the user
 * clicks a button in the list).
 * <p>While this view supports lists, you may want to use {@link SerializedItemListView} when possible, since it provides more functionalities.
 * @see GenericEntityCollectionView
 * @see GenericEntitySetView
 * @see GenericEntitySortedSetView
 * @see GenericEntityListView
 */
public abstract class AbstractSerializedItemCollectionView<T extends Serializable, C extends Collection<? extends T>> extends AbstractGenericCollectionView<T, C> {

	private static final long serialVersionUID = -6717993103476751931L;
	
	public AbstractSerializedItemCollectionView(String id, IModel<? extends C> model) {
		super(id, model);
	}

	/**
	 * Note: if you wish to override this, and overriding {@link #getModel(T)} is not enough, you're better off extending {@link AbstractGenericCollectionView} directly.
	 */
	@Override
	protected final Iterator<IModel<T>> getItemModels() {
		Collection<T> collectionWithoutTypeWildcard = Collections.unmodifiableCollection(getModelObject());
		return new ModelIteratorAdapter<T>(collectionWithoutTypeWildcard.iterator()) {
			@Override
			protected IModel<T> model(T object) {
				return AbstractSerializedItemCollectionView.this.getModel(object);
			}
		};
	}
	
	public IModel<T> getModel(T object) {
		return new Model<T>(object); // Model object is serialized along with the item 
	}

}
