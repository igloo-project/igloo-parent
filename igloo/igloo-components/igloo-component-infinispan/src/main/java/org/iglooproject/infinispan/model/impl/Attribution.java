package org.iglooproject.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.IAttribution;

public class Attribution extends AbstractAttribution implements Serializable, IAttribution {

	private static final long serialVersionUID = 2830812408213624466L;

	private Attribution(AddressWrapper owner, Date attributionDate) {
		super(owner, attributionDate);
	}

	public static final Attribution from(AddressWrapper owner, Date attributionDate) {
		return new Attribution(owner, attributionDate);
	}

}
