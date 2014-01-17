package fr.openwide.core.commons.util.functional;

import java.io.Serializable;

import com.google.common.base.Supplier;

/**
 * A simple interface enabling declaration of anonymous serializable suppliers.
 */
public interface SerializableSupplier<T> extends Supplier<T>, Serializable {

}
