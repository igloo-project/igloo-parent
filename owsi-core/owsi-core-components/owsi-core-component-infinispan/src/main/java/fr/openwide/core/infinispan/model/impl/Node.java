package fr.openwide.core.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.jgroups.Address;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.infinispan.model.INode;

public class Node implements Serializable, INode {

	private static final long serialVersionUID = 5121676759539404734L;

	private final Address address;

	private final String name;

	private final Date creationDate;

	private final Date leaveDate;

	private final boolean anonymous;

	private Node(Address address, String name, Date creationDate, Date leaveDate, boolean anonymous) {
		super();
		this.name = name;
		this.address = address;
		this.creationDate = CloneUtils.clone(creationDate);
		this.leaveDate = CloneUtils.clone(leaveDate);
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

	public static final Node from(Address address, String name) {
		return new Node(address, name, new Date(), null, false);
	}

	public static final Node from(Address address) {
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
