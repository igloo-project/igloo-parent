package org.iglooproject.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.jgroups.Address;

import org.iglooproject.infinispan.model.IAttribution;

public class Attribution extends AbstractAttribution implements Serializable, IAttribution {

	private static final long serialVersionUID = 2830812408213624466L;

	private Attribution(Address owner, Date attributionDate) {
		super(owner, attributionDate);
	}

	public static final Attribution from(Address owner, Date attributionDate) {
		return new Attribution(owner, attributionDate);
	}

}
