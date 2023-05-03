package org.iglooproject.test.business.nullables.model.embeddable;

import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NullablesSection {

	@Basic(optional = false)
	@Column(nullable = false)
	private Instant basicFalseColumnFalse;

	@Basic(optional = false)
	@Column(nullable = true)
	private Instant basicFalseColumnTrue;

	@Basic(optional = true)
	@Column(nullable = false)
	private Instant basicTrueColumnFalse;

	@Basic(optional = true)
	@Column(nullable = true)
	private Instant basicTrueColumnTrue;

	public Instant getBasicFalseColumnFalse() {
		return basicFalseColumnFalse;
	}

	public void setBasicFalseColumnFalse(Instant basicFalseColumnFalse) {
		this.basicFalseColumnFalse = basicFalseColumnFalse;
	}

	public Instant getBasicFalseColumnTrue() {
		return basicFalseColumnTrue;
	}

	public void setBasicFalseColumnTrue(Instant basicFalseColumnTrue) {
		this.basicFalseColumnTrue = basicFalseColumnTrue;
	}

	public Instant getBasicTrueColumnFalse() {
		return basicTrueColumnFalse;
	}

	public void setBasicTrueColumnFalse(Instant basicTrueColumnFalse) {
		this.basicTrueColumnFalse = basicTrueColumnFalse;
	}

	public Instant getBasicTrueColumnTrue() {
		return basicTrueColumnTrue;
	}

	public void setBasicTrueColumnTrue(Instant basicTrueColumnTrue) {
		this.basicTrueColumnTrue = basicTrueColumnTrue;
	}

}
