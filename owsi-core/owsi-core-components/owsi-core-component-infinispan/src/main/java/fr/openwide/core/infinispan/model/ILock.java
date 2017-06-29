package fr.openwide.core.infinispan.model;

import org.bindgen.Bindable;

@Bindable
public interface ILock extends ICacheKey {

	String getType();

}
