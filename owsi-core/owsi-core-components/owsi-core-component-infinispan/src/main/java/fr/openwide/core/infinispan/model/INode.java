package fr.openwide.core.infinispan.model;

import org.infinispan.remoting.transport.Address;

public interface INode {

	Address getAddress();

	String getName();

	boolean isAnonymous();

}
