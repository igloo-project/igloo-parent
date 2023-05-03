package test.jpa.more.business.history.service;

import java.time.Instant;
import java.util.List;

import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.history.service.AbstractHistoryLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.jpa.more.business.history.dao.ITestHistoryLogDao;
import test.jpa.more.business.history.model.TestHistoryDifference;
import test.jpa.more.business.history.model.TestHistoryLog;
import test.jpa.more.business.history.model.atomic.TestHistoryEventType;
import test.jpa.more.business.history.model.bean.TestHistoryLogAdditionalInformationBean;

@Service
public class TestHistoryLogServiceImpl extends AbstractHistoryLogServiceImpl<TestHistoryLog, TestHistoryEventType,
		TestHistoryDifference, TestHistoryLogAdditionalInformationBean>
		implements ITestHistoryLogService {

	private static final Supplier2<TestHistoryDifference> HISTORY_DIFFERENCE_SUPPLIER = () -> new TestHistoryDifference();
	
	@Autowired
	public TestHistoryLogServiceImpl(ITestHistoryLogDao dao) {
		super(dao);
	}
	
	@Override
	protected <T> TestHistoryLog newHistoryLog(Instant date, TestHistoryEventType eventType, List<TestHistoryDifference> differences,
			T mainObject, TestHistoryLogAdditionalInformationBean additionalInformation) {
		TestHistoryLog log = new TestHistoryLog(date, eventType, valueService.create(mainObject));
		
		// Don't set any subject here (we don't have a IUserService)
		
		if (additionalInformation != null) {
			setAdditionalInformation(log, additionalInformation);
		}
		
		return log;
	}

	@Override
	protected Supplier2<TestHistoryDifference> newHistoryDifferenceSupplier() {
		return HISTORY_DIFFERENCE_SUPPLIER;
	}

}
