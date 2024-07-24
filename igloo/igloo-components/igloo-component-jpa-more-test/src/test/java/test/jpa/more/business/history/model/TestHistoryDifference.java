package test.jpa.more.business.history.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Bindable
@Cacheable
@Access(AccessType.FIELD)
@Table(
    indexes = {
      @Index(name = "idx_HistoryDifference_parentLog", columnList = "parentLog_id"),
      @Index(name = "idx_HistoryDifference_parentDifference", columnList = "parentDifference_id")
    })
public class TestHistoryDifference
    extends AbstractHistoryDifference<TestHistoryDifference, TestHistoryLog> {

  private static final long serialVersionUID = -8437788725042615126L;

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
