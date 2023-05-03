package org.iglooproject.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.bindgen.Bindable;

@Embeddable
@Bindable
public class UserAnnouncementInformation implements Serializable {

	private static final long serialVersionUID = 635631181343124656L;

	@Basic(optional = false)
	private boolean open = true;

	@Basic
	private Instant lastActionDate;

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Instant getLastActionDate() {
		return lastActionDate;
	}

	public void setLastActionDate(Instant lastActionDate) {
		this.lastActionDate = lastActionDate;
	}

}
