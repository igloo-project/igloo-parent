package org.iglooproject.infinispan.model.impl;

import java.util.Date;

import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.IAttribution;
import org.jgroups.Address;

import com.google.common.base.Objects;

public class AbstractAttribution implements IAttribution {

	private static final long serialVersionUID = 138189627869759402L;

	private final AddressWrapper owner;

	private final Date attributionDate;

	protected AbstractAttribution(AddressWrapper owner, Date attributionDate) {
		super();
		this.owner = owner;
		this.attributionDate = attributionDate;
	}

	@Override
	public AddressWrapper getOwner() {
		return owner;
	}

	@Override
	public Date getAttributionDate() {
		return attributionDate;
	}

	@Override
	public boolean match(AddressWrapper address) {
		return Objects.equal(getOwner(), address);
	}

	@Override
	public boolean match(IAttribution attribution) {
		return Objects.equal(getOwner(), attribution.getOwner());
	}

	@Override
	public String toString() {
		return String.format("%s<%s (at %tF %<tT %<tz)>", getClass().getSimpleName(), getOwner(), getAttributionDate());
	}

}
