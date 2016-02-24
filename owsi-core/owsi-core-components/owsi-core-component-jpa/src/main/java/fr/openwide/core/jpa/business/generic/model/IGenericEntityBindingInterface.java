package fr.openwide.core.jpa.business.generic.model;

import org.bindgen.Bindable;

@Bindable
public interface IGenericEntityBindingInterface {

	Object getId();

	boolean isNew();

}
