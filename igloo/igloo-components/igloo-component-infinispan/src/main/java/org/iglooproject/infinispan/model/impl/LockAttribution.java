package org.iglooproject.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.jgroups.Address;
import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.ILockAttribution;

public class LockAttribution extends AbstractAttribution implements ILockAttribution, Serializable {

	private static final long serialVersionUID = -509420452188516219L;

	private LockAttribution(AddressWrapper owner, Date attributionDate) {
		super(owner, attributionDate);
	}

	public static final LockAttribution from(AddressWrapper owner, Date attributionDate) {
		return new LockAttribution(owner, attributionDate);
	}

}
