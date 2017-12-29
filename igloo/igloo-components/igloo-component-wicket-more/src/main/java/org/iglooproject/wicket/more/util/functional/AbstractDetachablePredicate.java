package org.iglooproject.wicket.more.util.functional;

import org.apache.wicket.model.IDetachable;

import org.iglooproject.commons.util.functional.AbstractSerializablePredicate;
import org.iglooproject.wicket.more.condition.Condition;

/**
 * A simple class enabling declaration of anonymous detachable predicates.
 * 
 * @deprecated Use {@link Condition}s instead: most APIs in Igloo related
 * to conditional behavior now accept {@link Condition}s.
 */
@Deprecated
public abstract class AbstractDetachablePredicate<T> extends AbstractSerializablePredicate<T> implements IDetachable {
	
	private static final long serialVersionUID = -8808637680118889256L;

}
