package fr.openwide.core.infinispan.model.impl;

import java.io.Serializable;
import java.util.Date;

import org.infinispan.remoting.transport.Address;

import com.google.common.base.Objects;

import fr.openwide.core.infinispan.model.IAttribution;
import fr.openwide.core.infinispan.model.IRoleAttribution;

public class RoleAttribution implements IRoleAttribution, Serializable {

	private static final long serialVersionUID = -3156360084124623566L;

	public Address owner;

	public Date attributionDate;

	protected RoleAttribution(Address owner, Date attributionDate) {
		super();
		this.owner = owner;
		this.attributionDate = attributionDate;
	}

	protected RoleAttribution() {
		super();
	}

	@Override
	public Address getOwner() {
		return owner;
	}

	@Override
	public Date getAttributionDate() {
		return attributionDate;
	}

	public void setOwner(Address owner) {
		this.owner = owner;
	}

	public void setAttributionDate(Date attributionDate) {
		this.attributionDate = attributionDate;
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
		return String.format("%s<%s (at %tF %<tT %<tz)>", getClass().getSimpleName(), getOwner(), getAttributionDate());
	}

	public static final RoleAttribution from(Address owner, Date attributionDate) {
		return new RoleAttribution(owner, attributionDate);
	}

}
