package fr.openwide.core.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.jgroups.Address;

import fr.openwide.core.infinispan.model.ILockAttribution;

public class LockAttribution extends AbstractAttribution implements ILockAttribution, Serializable {

	private static final long serialVersionUID = -509420452188516219L;

	private LockAttribution(Address owner, Date attributionDate) {
		super(owner, attributionDate);
	}

	public static final LockAttribution from(Address owner, Date attributionDate) {
		return new LockAttribution(owner, attributionDate);
	}

}
