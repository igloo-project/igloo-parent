package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IDetachable;

/**
 * TODO YRO make this extend Java's Function and Guava's Function with default implementations
 * in Java 8. Also, deprecate these overrides so that no one tries to use them explicitly.
 */
public interface IDetachableFactory<T, R> extends IDetachable {

	R create(T parameter);

}
