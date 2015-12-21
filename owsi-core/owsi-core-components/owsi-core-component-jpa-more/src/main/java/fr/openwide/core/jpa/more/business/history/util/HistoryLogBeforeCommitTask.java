package fr.openwide.core.jpa.more.business.history.util;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogObjectsBean;
import fr.openwide.core.jpa.more.business.history.service.IAbstractHistoryLogService;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;

public class HistoryLogBeforeCommitTask<T, HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>>
		implements ITransactionSynchronizationBeforeCommitTask {
	
	protected final Date date;
	
	protected final HET eventType;
	
	protected final AbstractHistoryLogObjectsBean<T> logObjects;
	
	@Autowired
	private IAbstractHistoryLogService<HL, HET, HD> historyLogService;

	public HistoryLogBeforeCommitTask(Date date, HET eventType, AbstractHistoryLogObjectsBean<T> objects) {
		super();
		this.date = CloneUtils.clone(date);
		this.eventType = eventType;
		this.logObjects = objects;
	}
	
	public T getMainObject() {
		return logObjects.getMainObject();
	}

	@Override
	public void run() throws Exception {
		logNow();
	}
	
	protected final IAbstractHistoryLogService<HL, HET, HD> getHistoryLogService() {
		return historyLogService;
	}

	protected void logNow() throws ServiceException, SecurityServiceException {
		getHistoryLogService().logNow(date, eventType, ImmutableList.<HD>of(), logObjects);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HistoryLogBeforeCommitTask) {
			HistoryLogBeforeCommitTask<?, ?, ?, ?> other = (HistoryLogBeforeCommitTask<?, ?, ?, ?>) obj;
			return new EqualsBuilder()
					.append(eventType, other.eventType)
					.append(logObjects, other.logObjects)
					.build();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(eventType)
				.append(logObjects)
				.build();
	}

}
