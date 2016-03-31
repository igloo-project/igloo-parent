package fr.openwide.core.wicket.more.util.functional;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.commons.util.functional.AbstractSerializablePredicate;
import fr.openwide.core.wicket.more.condition.Condition;

/**
 * A simple class enabling declaration of anonymous detachable predicates.
 * 
 * @deprecated Use {@link Condition}s instead: most APIs in OWSI-Core related
 * to conditional behavior now accept {@link Condition}s.
 */
@Deprecated
public abstract class AbstractDetachablePredicate<T> extends AbstractSerializablePredicate<T> implements IDetachable {
	
	private static final long serialVersionUID = -8808637680118889256L;

}
