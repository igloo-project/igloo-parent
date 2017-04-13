package fr.openwide.core.infinispan.model;

import java.io.Serializable;

import org.infinispan.remoting.transport.Address;

public class Message<M extends Serializable> implements Serializable {

	private static final long serialVersionUID = 8414986045738423530L;

	private Address address;

	private M message;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public M getMessage() {
		return message;
	}

	public void setMessage(M message) {
		this.message = message;
	}

	public static <M extends Serializable> Message<M> from(Address address, M message) {
		Message<M> m = new Message<M>();
		m.address = address;
		m.message = message;
		return m;
	}

}
