package fr.openwide.core.wicket.more.util.functional;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.commons.util.functional.SerializablePredicate;
import fr.openwide.core.wicket.more.condition.Condition;

/**
 * A simple interface enabling declaration of anonymous detachable predicates.
 * 
 * @deprecated Use {@link Condition}s instead: most APIs in OWSI-Core related
 * to conditional behavior now accept {@link Condition}s.
 */
@Deprecated
public interface DetachablePredicate<T> extends SerializablePredicate<T>, IDetachable {

}
