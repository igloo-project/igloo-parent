package fr.openwide.core.commons.util.functional;

import java.io.Serializable;

import com.google.common.base.Function;

/**
 * A simple interface enabling declaration of anonymous serializable functions.
 */
public interface SerializableFunction<F, T> extends Function<F, T>, Serializable {

}
