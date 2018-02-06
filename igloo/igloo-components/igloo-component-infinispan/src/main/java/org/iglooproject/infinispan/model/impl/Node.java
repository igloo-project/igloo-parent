package org.iglooproject.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.INode;
import org.jgroups.Address;

public class Node implements Serializable, INode {

	private static final long serialVersionUID = 5121676759539404734L;

	private final AddressWrapper address;

	private final String name;

	private final Date creationDate;

	private final Date leaveDate;

	private final boolean anonymous;

	private Node(AddressWrapper address, String name, Date creationDate, Date leaveDate, boolean anonymous) {
		super();
		this.name = name;
		this.address = address;
		this.creationDate = CloneUtils.clone(creationDate);
		this.leaveDate = CloneUtils.clone(leaveDate);
		this.anonymous = anonymous;
	}

	@Override
	public AddressWrapper getAddress() {
		return address;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public Date getLeaveDate() {
		return leaveDate;
	}

	@Override
	public boolean isAnonymous() {
		return anonymous;
	}

	@Override
	public String toString() {
		return String.format("%s<%s-%s>", getClass().getSimpleName(), address, name);
	}

	public static final Node from(AddressWrapper address, String name) {
		return new Node(address, name, new Date(), null, false);
	}

	public static final Node from(AddressWrapper address) {
		return new Node(address, "anonymous", new Date(), null, true);
	}

	public static final Node from(INode node, Date leaveDate) {
		return new Node(
				node.getAddress(),
				node.getName(),
				node.getCreationDate(),
				leaveDate,
				node.isAnonymous()
		);
	}

}
