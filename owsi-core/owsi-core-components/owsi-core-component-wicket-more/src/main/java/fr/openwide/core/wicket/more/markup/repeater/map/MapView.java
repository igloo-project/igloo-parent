package fr.openwide.core.wicket.more.markup.repeater.map;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.sequence.SequenceView;

public abstract class MapView<K, V> extends SequenceView<K> {

	private static final long serialVersionUID = 1L;
	
	public MapView(String id, IMapModel<K, V, ?> mapModel) {
		super(id, mapModel);
		setDefaultModel(mapModel);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected final void populateItem(Item<K> item) {
		populateItem((MapItem<K, V>)item);
	}

	protected abstract void populateItem(MapItem<K, V> item);

	@SuppressWarnings("unchecked")  // The sequence provider is known to be a IMapModel, see constructor
	public IMapModel<K, V, ?> getModel() {
		return (IMapModel<K, V, ?>) getSequenceProvider();
	}

	@Override
	protected MapItem<K, V> newItem(String id, int index, IModel<K> model) {
		return new MapItem<K, V>(id, index, model, getModel().getValueModelForProvidedKeyModel(model));
	}

}
