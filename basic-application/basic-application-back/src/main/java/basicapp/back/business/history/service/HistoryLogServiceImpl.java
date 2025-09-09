package basicapp.back.business.history.service;

import basicapp.back.business.history.model.HistoryDifference;
import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.repository.IHistoryLogRepository;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.history.service.AbstractHistoryLogJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoryLogServiceImpl
    extends AbstractHistoryLogJpaServiceImpl<
        HistoryLog, HistoryEventType, HistoryDifference, HistoryLogAdditionalInformationBean>
    implements IHistoryLogService {

  private static final Supplier2<HistoryDifference> HISTORY_DIFFERENCE_SUPPLIER =
      HistoryDifference::new;

  private final IUserService userService;
  private final IHistoryLogRepository historyLogRepository;

  @Autowired
  public HistoryLogServiceImpl(
      IUserService userService, IHistoryLogRepository historyLogRepository) {
    this.userService = userService;
    this.historyLogRepository = historyLogRepository;
  }

  @Override
  protected <T> HistoryLog newHistoryLog(
      Instant date,
      HistoryEventType eventType,
      List<HistoryDifference> differences,
      T mainObject,
      HistoryLogAdditionalInformationBean additionalInformation) {
    HistoryLog log = new HistoryLog(date, eventType, valueService.createHistoryValue(mainObject));

    User subject = userService.getAuthenticatedUser();
    log.setSubject(valueService.createHistoryValue(subject));

    if (additionalInformation != null) {
      setAdditionalInformation(log, additionalInformation);
    }

    return log;
  }

  @Override
  protected void saveHistoryLog(HistoryLog historyLog) {
    Objects.requireNonNull(historyLog);
    historyLogRepository.save(historyLog);
  }

  @Override
  protected Supplier2<HistoryDifference> newHistoryDifferenceSupplier() {
    return HISTORY_DIFFERENCE_SUPPLIER;
  }

  @Override
  @Transactional(readOnly = true)
  public HistoryLog getById(Long id) {
    return id == null ? null : historyLogRepository.findById(id).orElse(null);
  }
}
