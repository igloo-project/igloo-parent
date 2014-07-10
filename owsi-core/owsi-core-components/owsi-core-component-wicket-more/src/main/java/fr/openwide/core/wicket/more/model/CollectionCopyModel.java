package fr.openwide.core.wicket.more.model;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import com.google.common.base.Supplier;

@SuppressWarnings("rawtypes")
public final class CollectionCopyModel<C extends Collection>
		implements IModel<C> {

	private static final long serialVersionUID = 6790239207288306463L;

	private final Supplier<? extends C> newCollectionSupplier;
	
	private C collection = null;
	
	public CollectionCopyModel(Supplier<? extends C> newCollectionSupplier) {
		super();
		this.newCollectionSupplier = newCollectionSupplier;
		setObject(null); // Sets to an empty collection
	}

	protected C createCollection() {
		return newCollectionSupplier.get();
	}
	
	@Override
	public C getObject() {
		return collection;
	}
	
	/**
	 * WARNING: if the client calls <code>setObject(null)</code>, a subsequent call to <code>getObject()</code>
	 * will not return <code>null</code>, but <em>an empty collection</em>.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setObject(C object) {
		collection = createCollection();
		if (object != null) {
			collection.addAll(object);
		}
	}

	@Override
	public void detach() {
		// Rien à faire
	};

}
