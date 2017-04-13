package fr.openwide.core.infinispan.model.impl;

import java.io.Serializable;

import org.infinispan.remoting.transport.Address;

import fr.openwide.core.infinispan.model.INode;

public class Node implements INode, Serializable {

	private static final long serialVersionUID = 5121676759539404734L;

	private final Address address;

	private final String name;

	private final boolean anonymous;

	private Node(Address address, String name, boolean anonymous) {
		super();
		this.name = name;
		this.address = address;
		this.anonymous = anonymous;
	}

	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("%s<%s-%s>", getClass().getSimpleName(), address, name);
	}

	@Override
	public boolean isAnonymous() {
		return anonymous;
	}

	public static final Node from(Address address, String name) {
		return new Node(address, name, false);
	}

	public static final Node from(Address address) {
		return new Node(address, "anonymous", true);
	}

}
