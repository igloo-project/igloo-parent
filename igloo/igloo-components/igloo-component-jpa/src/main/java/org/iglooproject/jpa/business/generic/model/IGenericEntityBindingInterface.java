package org.iglooproject.jpa.business.generic.model;

import org.bindgen.Bindable;

@Bindable
public interface IGenericEntityBindingInterface {

	Object getId();

	boolean isNew();

}
