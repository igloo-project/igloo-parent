package fr.openwide.core.wicket.more.util.functional;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.commons.util.functional.AbstractSerializablePredicate;

/**
 * A simple class enabling declaration of anonymous detachable predicates.
 * 
 * @see AbstractSerializablePredicate
 */
public abstract class AbstractDetachablePredicate<T> extends AbstractSerializablePredicate<T> implements IDetachable {
	
	private static final long serialVersionUID = -8808637680118889256L;

}
