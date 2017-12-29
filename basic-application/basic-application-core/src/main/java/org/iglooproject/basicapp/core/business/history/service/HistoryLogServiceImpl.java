package org.iglooproject.basicapp.core.business.history.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Supplier;

import org.iglooproject.basicapp.core.business.history.dao.IHistoryLogDao;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.history.model.bean.HistoryLogAdditionalInformationBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.more.business.history.service.AbstractHistoryLogServiceImpl;

@Service
public class HistoryLogServiceImpl extends AbstractHistoryLogServiceImpl<HistoryLog, HistoryEventType,
		HistoryDifference, HistoryLogAdditionalInformationBean>
		implements IHistoryLogService {

	private static final Supplier<HistoryDifference> HISTORY_DIFFERENCE_SUPPLIER = new Supplier<HistoryDifference>() {
		@Override
		public HistoryDifference get() {
			return new HistoryDifference();
		}
	};
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	public HistoryLogServiceImpl(IHistoryLogDao dao) {
		super(dao);
	}
	
	@Override
	protected <T> HistoryLog newHistoryLog(Date date, HistoryEventType eventType, List<HistoryDifference> differences,
			T mainObject, HistoryLogAdditionalInformationBean additionalInformation) {
		HistoryLog log = new HistoryLog(date, eventType, valueService.create(mainObject));
		
		User subject = userService.getAuthenticatedUser();
		log.setSubject(valueService.create(subject));
		
		if (additionalInformation != null) {
			setAdditionalInformation(log, additionalInformation);
		}
		
		return log;
	}

	@Override
	protected Supplier<HistoryDifference> newHistoryDifferenceSupplier() {
		return HISTORY_DIFFERENCE_SUPPLIER;
	}

}
