package org.iglooproject.infinispan.model;

import java.io.Serializable;
import java.util.Date;

import org.bindgen.Bindable;

@Bindable
public interface IAttribution extends Serializable {

	AddressWrapper getOwner();
	Date getAttributionDate();
	boolean match(AddressWrapper address);
	boolean match(IAttribution attribution);

}
