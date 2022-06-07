package org.iglooproject.wicket.more.bindable.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.model.IItemModelAwareCollectionModel;
import org.iglooproject.wicket.model.IItemModelAwareMapModel;
import org.iglooproject.wicket.model.MapCopyModel;
import org.iglooproject.wicket.more.model.WorkingCopyMapModel;

public class BindableMapModel<K, V, M extends Map<K, V>> extends BindableModel<M>
		implements IBindableMapModel<K, V, M> {
	private static final long serialVersionUID = 507773585338822489L;
	
	private final IItemModelAwareMapModel<K, V, M, IBindableModel<K>, IBindableModel<V>> mainModel;

	public BindableMapModel(IModel<M> referenceModel, SerializableSupplier2<? extends M> newMapSupplier,
			SerializableFunction2<? super K, ? extends IModel<K>> keyModelFunction,
			SerializableFunction2<? super V, ? extends IModel<V>> valueModelFunction) {
		this(
				new WorkingCopyMapModel<>(
						referenceModel,
						MapCopyModel.custom(
								newMapSupplier,
								BindableModel.factory(keyModelFunction), BindableModel.factory(valueModelFunction)
						)
				)
		);
	}

	private BindableMapModel(IItemModelAwareMapModel<K, V, M, IBindableModel<K>, IBindableModel<V>> delegate) {
		super(delegate);
		this.mainModel = delegate;
	}

	@Override
	public boolean equals(Object obj) {
		return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).hashCode();
	}
	
	@Override
	protected void onWriteAll() {
		super.onWriteAll();
		for (IBindableModel<K> itemModel : mainModel) {
			itemModel.writeAll();
			mainModel.valueModelForProvidedKeyModel(itemModel).writeAll();
		}
	}
	
	@Override
	protected void onReadAll() {
		super.onReadAll();
		for (IBindableModel<K> itemModel : mainModel) {
			itemModel.readAll();
			mainModel.valueModelForProvidedKeyModel(itemModel).readAll();
		}
	}
	
	@Override
	public Iterator<IBindableModel<K>> iterator() {
		return mainModel.iterator();
	}

	@Override
	public Iterator<IBindableModel<K>> iterator(long offset, long limit) {
		return mainModel.iterator(offset, limit);
	}
	
	@Override
	public long size() {
		return mainModel.size();
	}
	
	@Override
	public IItemModelAwareCollectionModel<K, Set<K>, IBindableModel<K>> keysModel() {
		return mainModel.keysModel();
	}
	
	@Override
	public IItemModelAwareCollectionModel<V, Collection<V>, IBindableModel<V>> valuesModel() {
		return mainModel.valuesModel();
	}
	
	@Override
	public IModel<V> valueModel(IModel<? extends K> keyModel) {
		return mainModel.valueModel(keyModel);
	}

	@Override
	public IBindableModel<V> valueModelForProvidedKeyModel(IModel<K> keyModel) {
		return mainModel.valueModelForProvidedKeyModel(keyModel);
	}
	
	@Override
	public void put(K key, V value) {
		mainModel.put(key, value);
	}
	
	@Override
	public void remove(K key) {
		mainModel.remove(key);
	}

	@Override
	public void clear() {
		mainModel.clear();
	}

}