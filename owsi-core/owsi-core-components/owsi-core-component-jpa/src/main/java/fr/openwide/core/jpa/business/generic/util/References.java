package fr.openwide.core.jpa.business.generic.util;

import fr.openwide.core.jpa.business.generic.model.IReference;
import fr.openwide.core.jpa.business.generic.model.IReferenceable;

public final class References {
	
	private References() {
	}
	
	@SuppressWarnings("unchecked") // IReference<T> is covariant in T, so this cast is harmless 
	public static <T> IReference<T> asReference(IReferenceable<? extends T> referenceable) {
		return referenceable == null ? null : (IReference<T>)referenceable.asReference();
	}
	
}
