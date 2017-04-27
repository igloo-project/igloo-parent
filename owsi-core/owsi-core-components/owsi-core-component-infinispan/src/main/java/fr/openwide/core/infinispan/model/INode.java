package fr.openwide.core.infinispan.model;

import java.io.Serializable;
import java.util.Date;

import org.bindgen.Bindable;
import org.jgroups.Address;

@Bindable
public interface INode extends Serializable {

	Address getAddress();

	String getName();

	Date getCreationDate();

	Date getLeaveDate();

	boolean isAnonymous();

}
