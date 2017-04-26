package fr.openwide.core.infinispan.model.impl;

import java.util.Date;

import org.infinispan.remoting.transport.Address;

import com.google.common.base.Objects;

import fr.openwide.core.infinispan.model.IAttribution;

public class AbstractAttribution implements IAttribution {

	private static final long serialVersionUID = 138189627869759402L;

	private final Address owner;

	private final Date attributionDate;

	protected AbstractAttribution(Address owner, Date attributionDate) {
		super();
		this.owner = owner;
		this.attributionDate = attributionDate;
	}

	@Override
	public Address getOwner() {
		return owner;
	}

	@Override
	public Date getAttributionDate() {
		return attributionDate;
	}

	@Override
	public boolean match(Address address) {
		return Objects.equal(getOwner(), address);
	}

	@Override
	public boolean match(IAttribution attribution) {
		return Objects.equal(getOwner(), attribution.getOwner());
	}

	@Override
	public String toString() {
		return String.format("%s<%s (at %tF %tT %tz)>", getClass().getSimpleName(), getOwner(), getAttributionDate());
	}

}
