package test.business.service;

import java.time.Instant;
import java.util.List;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.history.service.AbstractHistoryLogServiceImpl;
import test.business.dao.ITestHistoryLogDao;
import test.business.model.HistoryLogAdditionalInformationBean;
import test.business.model.TestHistoryDifference;
import test.business.model.TestHistoryEventType;
import test.business.model.TestHistoryLog;
import test.business.model.TestUser;

public class TestHistoryLogServiceImpl
    extends AbstractHistoryLogServiceImpl<
        TestHistoryLog,
        TestHistoryEventType,
        TestHistoryDifference,
        HistoryLogAdditionalInformationBean>
    implements ITestHistoryLogService {

  private final ISubjectService subjectService;

  public TestHistoryLogServiceImpl(ITestHistoryLogDao dao, ISubjectService subjectService) {
    super(dao);
    this.subjectService = subjectService;
  }

  @Override
  protected <T> TestHistoryLog newHistoryLog(
      Instant date,
      TestHistoryEventType eventType,
      List<TestHistoryDifference> differences,
      T mainObject,
      HistoryLogAdditionalInformationBean additionalInformation) {
    TestHistoryLog log =
        new TestHistoryLog(date, eventType, valueService.createHistoryValue(mainObject));

    TestUser subject = subjectService.getSubject();
    log.setSubject(valueService.createHistoryValue(subject));

    if (additionalInformation != null) {
      setAdditionalInformation(log, additionalInformation);
    }

    return log;
  }

  @Override
  protected Supplier2<TestHistoryDifference> newHistoryDifferenceSupplier() {
    return null;
  }
}
