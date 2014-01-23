package fr.openwide.core.commons.util.functional;

import java.io.Serializable;

import com.google.common.base.Predicate;

/**
 * A simple interface enabling declaration of anonymous serializable predicates.
 */
public interface SerializablePredicate<T> extends Predicate<T>, Serializable {

}
