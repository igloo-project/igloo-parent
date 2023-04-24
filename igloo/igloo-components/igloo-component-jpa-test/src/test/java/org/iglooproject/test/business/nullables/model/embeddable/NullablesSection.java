package org.iglooproject.test.business.nullables.model.embeddable;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.iglooproject.commons.util.CloneUtils;

@Embeddable
public class NullablesSection {

	@Basic(optional = false)
	@Column(nullable = false)
	private Date basicFalseColumnFalse;

	@Basic(optional = false)
	@Column(nullable = true)
	private Date basicFalseColumnTrue;

	@Basic(optional = true)
	@Column(nullable = false)
	private Date basicTrueColumnFalse;

	@Basic(optional = true)
	@Column(nullable = true)
	private Date basicTrueColumnTrue;

	public Date getBasicFalseColumnFalse() {
		return CloneUtils.clone(basicFalseColumnFalse);
	}

	public void setBasicFalseColumnFalse(Date basicFalseColumnFalse) {
		this.basicFalseColumnFalse = CloneUtils.clone(basicFalseColumnFalse);
	}

	public Date getBasicFalseColumnTrue() {
		return CloneUtils.clone(basicFalseColumnTrue);
	}

	public void setBasicFalseColumnTrue(Date basicFalseColumnTrue) {
		this.basicFalseColumnTrue = CloneUtils.clone(basicFalseColumnTrue);
	}

	public Date getBasicTrueColumnFalse() {
		return CloneUtils.clone(basicTrueColumnFalse);
	}

	public void setBasicTrueColumnFalse(Date basicTrueColumnFalse) {
		this.basicTrueColumnFalse = CloneUtils.clone(basicTrueColumnFalse);
	}

	public Date getBasicTrueColumnTrue() {
		return CloneUtils.clone(basicTrueColumnTrue);
	}

	public void setBasicTrueColumnTrue(Date basicTrueColumnTrue) {
		this.basicTrueColumnTrue = CloneUtils.clone(basicTrueColumnTrue);
	}
}
