package org.iglooproject.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.bindgen.Bindable;
import org.iglooproject.commons.util.CloneUtils;

@Embeddable
@Bindable
public class UserAnnouncementInformation implements Serializable {

	private static final long serialVersionUID = 635631181343124656L;

	@Basic(optional = false)
	private boolean open = true;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastActionDate;

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Date getLastActionDate() {
		return CloneUtils.clone(lastActionDate);
	}

	public void setLastActionDate(Date lastActionDate) {
		this.lastActionDate = CloneUtils.clone(lastActionDate);
	}

}
