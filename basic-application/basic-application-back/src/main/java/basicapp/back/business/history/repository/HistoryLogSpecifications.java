package basicapp.back.business.history.repository;

import basicapp.back.business.history.model.HistoryDifference_;
import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.HistoryLog_;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.user.model.User;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.Collection;
import org.iglooproject.jpa.jparepository.CriteriaJoinBuilder;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue_;
import org.springframework.data.jpa.domain.Specification;

public class HistoryLogSpecifications {

  private HistoryLogSpecifications() {}

  public static Specification<HistoryLog> date(Instant dateMin, Instant dateMax) {
    if (dateMin == null) {
      return dateBefore(dateMax);
    }
    if (dateMax == null) {
      return dateAfter(dateMin);
    }
    return (historyLog, cq, cb) -> cb.between(historyLog.get(HistoryLog_.DATE), dateMin, dateMax);
  }

  public static Specification<HistoryLog> dateBefore(Instant dateMax) {
    return (historyLog, cq, cb) -> cb.lessThanOrEqualTo(historyLog.get(HistoryLog_.DATE), dateMax);
  }

  public static Specification<HistoryLog> dateAfter(Instant dateMin) {
    return (historyLog, cq, cb) ->
        cb.greaterThanOrEqualTo(historyLog.get(HistoryLog_.DATE), dateMin);
  }

  public static Specification<HistoryLog> eventType(Collection<HistoryEventType> eventTypes) {
    return (historyLog, cq, cb) -> cb.in(historyLog.get(HistoryLog_.EVENT_TYPE)).value(eventTypes);
  }

  public static Specification<HistoryLog> subject(User subject) {
    return (historyLog, cq, cb) ->
        cb.equal(
            historyLog.get(HistoryLog_.SUBJECT).get(HistoryValue_.REFERENCE),
            new HistoryEntityReference(subject));
  }

  public static Specification<HistoryLog> mainObject(HistoryEntityReference mainObject) {
    return (historyLog, cq, cb) ->
        cb.equal(historyLog.get(HistoryLog_.MAIN_OBJECT).get(HistoryValue_.REFERENCE), mainObject);
  }

  public static Specification<HistoryLog> object1(HistoryEntityReference object1) {
    return (historyLog, cq, cb) ->
        cb.equal(historyLog.get(HistoryLog_.OBJECT1).get(HistoryValue_.REFERENCE), object1);
  }

  public static Specification<HistoryLog> object2(HistoryEntityReference object2) {
    return (historyLog, cq, cb) ->
        cb.equal(historyLog.get(HistoryLog_.OBJECT2).get(HistoryValue_.REFERENCE), object2);
  }

  public static Specification<HistoryLog> object3(HistoryEntityReference object3) {
    return (historyLog, cq, cb) ->
        cb.equal(historyLog.get(HistoryLog_.OBJECT3).get(HistoryValue_.REFERENCE), object3);
  }

  public static Specification<HistoryLog> object4(HistoryEntityReference object4) {
    return (historyLog, cq, cb) ->
        cb.equal(historyLog.get(HistoryLog_.OBJECT4).get(HistoryValue_.REFERENCE), object4);
  }

  public static Specification<HistoryLog> hasHistoryDifference() {
    return (historyLog, cq, cb) ->
        cb.isNotNull(
            CriteriaJoinBuilder.root(historyLog)
                .join(HistoryLog_.DIFFERENCES, JoinType.LEFT)
                .get(HistoryDifference_.ID));
  }
}
