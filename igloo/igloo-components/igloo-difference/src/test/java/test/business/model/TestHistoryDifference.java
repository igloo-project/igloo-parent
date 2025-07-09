package test.business.model;

import jakarta.persistence.Entity;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Indexed
public class TestHistoryDifference
    extends AbstractHistoryDifference<TestHistoryDifference, TestHistoryLog> {

  private static final long serialVersionUID = 1L;

  public TestHistoryDifference() {
    super();
  }

  public TestHistoryDifference(
      HistoryDifferencePath path,
      HistoryDifferenceEventType action,
      HistoryValue before,
      HistoryValue after) {
    super(path, action, before, after);
  }
}
