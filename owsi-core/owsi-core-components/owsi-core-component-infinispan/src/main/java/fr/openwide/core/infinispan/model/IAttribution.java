package fr.openwide.core.infinispan.model;

import java.io.Serializable;
import java.util.Date;

import org.infinispan.remoting.transport.Address;

public interface IAttribution extends Serializable {

	Address getOwner();
	Date getAttributionDate();
	boolean match(Address address);
	boolean match(IAttribution attribution);

}
