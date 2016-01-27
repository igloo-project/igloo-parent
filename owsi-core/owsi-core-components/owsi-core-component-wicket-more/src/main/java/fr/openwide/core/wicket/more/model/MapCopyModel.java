package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.markup.repeater.map.IMapModel;
import fr.openwide.core.wicket.more.util.model.Models;

/**
 * A {@link IMapModel} whose content is to be "cloned" (i.e. copied to a new map) each time
 * {@link #setObject(Map)} is called.
 * 
 * <p>This is typically what you want when editing a map in a form.
 * 
 * <p>Instances of this class are guaranteed to always return the same model for a given element (key or value),
 * up to this element's removal from the map.
 * 
 * <p><strong>WARNING:</strong> this model is only intended to contain small maps. It is absolutely not optimized
 * for large maps (say, more than just one or two dozens of items). Performance issues may arise when dealing
 * with large maps.
 * 
 * @see AbstractMapCopyModel
 */
public final class MapCopyModel<K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
		extends AbstractMapCopyModel<K, V, M, MK, MV> {

	private static final long serialVersionUID = 5159538764516521470L;

	private final Supplier<? extends M> newMapSupplier;

	private final Function<? super K, ? extends MK> keyModelFunction;

	private final Function<? super V, ? extends MV> valueModelFunction;

	/**
	 * A map copy model suitable for keys and values that may be safely serialized as is, such as enums.
	 * <p>This <strong>should not</strong> be used when your elements are database entities.
	 */
	public static <K extends Serializable, V extends Serializable, M extends Map<K, V>>
			MapCopyModel<K, V, M, Model<K>, Model<V>>
			serializable(Supplier<? extends M> newMapSupplier) {
		return new MapCopyModel<>(newMapSupplier, Models.<K>serializableModelFactory(), Models.<V>serializableModelFactory());
	}

	/**
	 * A map copy model suitable for keys and values that must be serialized through a custom model, such as entities.
	 */
	public static <K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
			MapCopyModel<K, V, M, MK, MV>
			custom(Supplier<? extends M> newMapSupplier, Function<? super K, ? extends MK> keyModelFunction,
					Function<? super V, ? extends MV> valueModelFunction) {
		return new MapCopyModel<>(newMapSupplier, keyModelFunction, valueModelFunction);
	}
	
	private MapCopyModel(Supplier<? extends M> newMapSupplier, Function<? super K, ? extends MK> keyModelFunction,
			Function<? super V, ? extends MV> valueModelFunction) {
		super();
		this.newMapSupplier = newMapSupplier;
		this.keyModelFunction = keyModelFunction;
		this.valueModelFunction = valueModelFunction;
	}

	@Override
	protected M createMap() {
		return newMapSupplier.get();
	}

	@Override
	protected MK createKeyModel(K key) {
		return keyModelFunction.apply(key);
	}

	@Override
	protected MV createValueModel(V value) {
		return valueModelFunction.apply(value);
	}

}
