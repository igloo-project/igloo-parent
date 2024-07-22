package basicapp.back.business.history.model;

import basicapp.back.business.history.model.atomic.HistoryEventType;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import java.time.Instant;
import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Bindable
@Cacheable
@Indexed
public class HistoryLog
    extends AbstractHistoryLog<HistoryLog, HistoryEventType, HistoryDifference> {

  private static final long serialVersionUID = -8557932643510393694L;

  public HistoryLog() {
    // nothing to do
  }

  public HistoryLog(Instant date, HistoryEventType eventType, HistoryValue mainObject) {
    super(date, eventType, mainObject);
  }
}
