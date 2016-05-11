package fr.openwide.core.wicket.more.bindable.model;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.markup.repeater.map.IItemModelAwareMapModel;
import fr.openwide.core.wicket.more.model.MapCopyModel;
import fr.openwide.core.wicket.more.model.WorkingCopyMapModel;

public class BindableMapModel<K, V, M extends Map<K, V>> extends BindableModel<M>
		implements IBindableMapModel<K, V, M> {
	private static final long serialVersionUID = 507773585338822489L;
	
	private final IItemModelAwareMapModel<K, V, M, IBindableModel<K>, IBindableModel<V>> mainModel;

	public BindableMapModel(IModel<M> referenceModel, Supplier<? extends M> newMapSupplier,
			Function<? super K, ? extends IModel<K>> keyModelFunction,
			Function<? super V, ? extends IModel<V>> valueModelFunction) {
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
			mainModel.getValueModelForProvidedKeyModel(itemModel).writeAll();
		}
	}
	
	@Override
	protected void onReadAll() {
		super.onReadAll();
		for (IBindableModel<K> itemModel : mainModel) {
			itemModel.readAll();
			mainModel.getValueModelForProvidedKeyModel(itemModel).readAll();
		}
	}

	@Override
	public Iterator<IBindableModel<K>> iterator(long offset, long limit) {
		return mainModel.iterator(offset, limit);
	}
	
	@Override
	public Iterator<IBindableModel<K>> iterator() {
		return mainModel.iterator();
	}
	
	@Override
	public long size() {
		return mainModel.size();
	}
	
	@Override
	public IModel<V> getValueModel(IModel<? extends K> keyModel) {
		return mainModel.getValueModel(keyModel);
	}

	@Override
	public IBindableModel<V> getValueModelForProvidedKeyModel(IModel<K> keyModel) {
		return mainModel.getValueModelForProvidedKeyModel(keyModel);
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