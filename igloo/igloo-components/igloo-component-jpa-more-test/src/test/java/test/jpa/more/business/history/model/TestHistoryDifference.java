package test.jpa.more.business.history.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.bindgen.Bindable;
import org.hibernate.annotations.TypeDef;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.hibernate.FieldPathType;
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
@TypeDef(defaultForType = FieldPath.class, typeClass = FieldPathType.class)
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
