package fr.openwide.core.infinispan.model.impl;

import java.util.Date;

import fr.openwide.core.infinispan.model.ILeaveEvent;

public class LeaveEvent implements ILeaveEvent {

	private final Date leaveDate;

	private LeaveEvent(Date leaveDate) {
		super();
		this.leaveDate = leaveDate;
	}

	@Override
	public Date getLeaveDate() {
		return leaveDate;
	}

	@Override
	public String toString() {
		return String.format("%s<%tF %tT %tz>", leaveDate);
	}

	public static final LeaveEvent from(Date leaveDate) {
		return new LeaveEvent(leaveDate);
	}

}
