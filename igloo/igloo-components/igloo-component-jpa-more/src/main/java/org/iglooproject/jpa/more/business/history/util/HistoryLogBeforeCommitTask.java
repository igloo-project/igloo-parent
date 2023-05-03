package org.iglooproject.jpa.more.business.history.util;

import java.time.Instant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;

public class HistoryLogBeforeCommitTask<T,
				HLAIB extends AbstractHistoryLogAdditionalInformationBean,
				HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>>
		implements ITransactionSynchronizationBeforeCommitTask {
	
	protected final Instant date;
	
	protected final HET eventType;
	
	protected final T mainObject;
	
	protected final HLAIB additionalInformation;
	
	@Autowired
	private IGenericHistoryLogService<HL, HET, HD, HLAIB> historyLogService;

	public HistoryLogBeforeCommitTask(Instant date, HET eventType, T mainObject, HLAIB additionalInformation) {
		super();
		this.date = date;
		this.eventType = eventType;
		this.mainObject = mainObject;
		this.additionalInformation = additionalInformation;
	}
	
	/**
	 * @return true, because this task requires its parameters to be still attached to the session when it executes.
	 */
	@Override
	public boolean shouldRunBeforeClear() {
		return true;
	}
	
	public T getMainObject() {
		return mainObject;
	}

	@Override
	public void run() throws Exception {
		logNow();
	}
	
	protected final IGenericHistoryLogService<HL, HET, HD, HLAIB> getHistoryLogService() {
		return historyLogService;
	}

	protected void logNow() throws ServiceException, SecurityServiceException {
		getHistoryLogService().logNow(date, eventType, ImmutableList.<HD>of(), mainObject, additionalInformation);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HistoryLogBeforeCommitTask) {
			HistoryLogBeforeCommitTask<?, ?, ?, ?, ?> other = (HistoryLogBeforeCommitTask<?, ?, ?, ?, ?>) obj;
			return new EqualsBuilder()
					.append(eventType, other.eventType)
					.append(mainObject, other.mainObject)
					.build();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(eventType)
				.append(mainObject)
				.build();
	}

}
