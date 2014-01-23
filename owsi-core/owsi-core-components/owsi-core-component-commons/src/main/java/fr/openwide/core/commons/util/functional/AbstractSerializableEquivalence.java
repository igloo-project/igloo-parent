package fr.openwide.core.commons.util.functional;

import java.io.Serializable;

import com.google.common.base.Equivalence;

/**
 * A simple class enabling declaration of anonymous serializable equivalences.
 */
public abstract class AbstractSerializableEquivalence<T> extends Equivalence<T> implements Serializable {

	private static final long serialVersionUID = 221437391205244335L;

}
