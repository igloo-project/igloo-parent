package org.iglooproject.wicket.more.util.functional;

import org.apache.wicket.model.IDetachable;

import org.iglooproject.commons.util.functional.SerializablePredicate;
import org.iglooproject.wicket.more.condition.Condition;

/**
 * A simple interface enabling declaration of anonymous detachable predicates.
 * 
 * @deprecated Use {@link Condition}s instead: most APIs in Igloo related
 * to conditional behavior now accept {@link Condition}s.
 */
@Deprecated
public interface DetachablePredicate<T> extends SerializablePredicate<T>, IDetachable {

}
