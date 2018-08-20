package org.iglooproject.jpa.more.business.audit.model.embeddable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.iglooproject.commons.util.CloneUtils;

@Embeddable
public class TimeAuditableProperty extends AbstractAuditableProperty<Date> {
	
	private static final long serialVersionUID = 8032695845159475049L;
	
	@Column
	@Temporal(TemporalType.TIME)
	private Date value;

	@Override
	public Date getValue() {
		return CloneUtils.clone(value);
	}

	@Override
	public void setValue(Date value) {
		this.value = CloneUtils.clone(value);
	}

}
