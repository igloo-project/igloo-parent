package org.iglooproject.infinispan.model;

import java.io.Serializable;
import java.util.Date;

import org.bindgen.Bindable;

@Bindable
public interface INode extends Serializable {

	AddressWrapper getAddress();

	String getName();

	Date getCreationDate();

	Date getLeaveDate();

	boolean isAnonymous();

}
