package org.iglooproject.infinispan.model;

import java.io.IOException;
import java.io.Serializable;

import org.jgroups.Address;
import org.jgroups.util.ByteArrayDataInputStream;
import org.jgroups.util.ByteArrayDataOutputStream;
import org.jgroups.util.Util;

/**
 * This object is added so that we have an {@link Address}-implementation that is {@link Serializable}
 */
public class AddressWrapper implements Comparable<AddressWrapper>, Serializable {

	private static final long serialVersionUID = 2847205360502551822L;

	private transient Address address;

	private byte[] serialized;

	private AddressWrapper(Address address) {
		try {
			this.address = address;
			ByteArrayDataOutputStream dataOut = new ByteArrayDataOutputStream();
			Util.writeAddress(this.address, dataOut);
			this.serialized = dataOut.getByteBuffer().array();
		} catch (Exception e) {
			throw new RuntimeException("unreachable", e);
		}
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return address.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AddressWrapper) {
			return getAddress().equals(((AddressWrapper) obj).getAddress());
		}
		return false;
	}

	@Override
	public int compareTo(AddressWrapper o) {
		return getAddress().compareTo(((AddressWrapper) o).getAddress());
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		try {
			this.address = Util.readAddress(new ByteArrayDataInputStream(this.serialized));
		} catch (Exception e) {
			throw new IOException("unreachable", e);
		}
	}

	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	}

	public final static AddressWrapper from(Address address) {
		return new AddressWrapper(address);
	}

}
