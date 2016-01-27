package fr.openwide.core.wicket.more.model;

import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.map.IItemModelAwareMapModel;

public class WorkingCopyMapModel<K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
		extends WorkingCopyModel<M>
		implements IItemModelAwareMapModel<K, V, M, MK, MV> {
	
	private static final long serialVersionUID = -4049247716740595168L;

	private final IItemModelAwareMapModel<K, V, M, MK, MV> mapWorkingCopy;
	
	public WorkingCopyMapModel(IModel<M> reference, MapCopyModel<K, V, M, MK, MV> workingCopy) {
		super(reference, workingCopy);
		this.mapWorkingCopy = workingCopy;
	}

	@Override
	public M getObject() {
		return mapWorkingCopy.getObject();
	}

	@Override
	public void setObject(M object) {
		mapWorkingCopy.setObject(object);
	}
	
	@Override
	public Iterator<MK> iterator(long offset, long limit) {
		return mapWorkingCopy.iterator(offset, limit);
	}
	
	@Override
	public Iterator<MK> iterator() {
		return mapWorkingCopy.iterator();
	}
	
	@Override
	public long count() {
		return mapWorkingCopy.count();
	}
	
	@Override
	public IModel<V> getValueModel(IModel<? extends K> keyModel) {
		return mapWorkingCopy.getValueModel(keyModel);
	}

	@Override
	public MV getValueModelForProvidedKeyModel(IModel<K> keyModel) {
		return mapWorkingCopy.getValueModelForProvidedKeyModel(keyModel);
	}
	
	@Override
	public void put(K key, V value) {
		mapWorkingCopy.put(key, value);
	}
	
	@Override
	public void remove(K key) {
		mapWorkingCopy.remove(key);
	}
	
	@Override
	public void clear() {
		mapWorkingCopy.clear();
	}
}
