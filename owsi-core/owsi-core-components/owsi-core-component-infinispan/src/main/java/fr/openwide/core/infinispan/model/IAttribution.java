package fr.openwide.core.infinispan.model;

import java.util.Date;

import org.infinispan.remoting.transport.Address;

public interface IAttribution {

	Address getOwner();
	Date getAttributionDate();
	boolean match(Address address);
	boolean match(IAttribution attribution);

}
