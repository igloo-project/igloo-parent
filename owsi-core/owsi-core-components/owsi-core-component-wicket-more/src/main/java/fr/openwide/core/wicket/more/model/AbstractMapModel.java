package fr.openwide.core.wicket.more.model;

import java.util.Map;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.map.IMapModel;

public abstract class AbstractMapModel<K, V, M extends Map<K, V>>
		implements IMapModel<K, V, M> {

	private static final long serialVersionUID = 8313241207877097043L;
	
	public AbstractMapModel() {
		super();
	}

	@Override
	public IModel<V> getValueModel(final IModel<? extends K> keyModel) {
		return new IModel<V>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void detach() {
				AbstractMapModel.this.detach();
				keyModel.detach();
			}
			@Override
			public V getObject() {
				Map<K, V> map = AbstractMapModel.this.getObject();
				return map == null ? null : map.get(keyModel.getObject());
			}
			@Override
			public void setObject(V object) {
				AbstractMapModel.this.put(keyModel.getObject(), object);
			}
		};
	}

}
