package fr.openwide.core.wicket.more.util.functional;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.commons.util.functional.SerializablePredicate;

/**
 * A simple interface enabling declaration of anonymous detachable predicates.
 * 
 * @see SerializablePredicate
 */
public interface DetachablePredicate<T> extends SerializablePredicate<T>, IDetachable {

}
