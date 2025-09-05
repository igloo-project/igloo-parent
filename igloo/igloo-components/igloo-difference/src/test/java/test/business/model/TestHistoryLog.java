package test.business.model;

import jakarta.persistence.Entity;
import java.time.Instant;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Indexed
public class TestHistoryLog
    extends AbstractHistoryLog<TestHistoryLog, TestHistoryEventType, TestHistoryDifference> {

  private static final long serialVersionUID = 1L;

  public TestHistoryLog() {
    super();
  }

  public TestHistoryLog(Instant date, TestHistoryEventType eventType, HistoryValue mainObject) {
    super(date, eventType, mainObject);
  }
}
