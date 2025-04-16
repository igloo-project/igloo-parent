package org.iglooproject.basicapp.core.business.history.model;

import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Bindable
@Cacheable
@Table(
    indexes = {
      @Index(
          name = "historylog_type_id_main_idx",
          columnList = "mainObject_reference_type, mainObject_reference_id"),
      @Index(
          name = "historylog_type_id_1_idx",
          columnList = "object1_reference_type, object1_reference_id"),
      @Index(
          name = "historylog_type_id_2_idx",
          columnList = "object2_reference_type, object2_reference_id"),
      @Index(
          name = "historylog_type_id_3_idx",
          columnList = "object3_reference_type, object3_reference_id"),
      @Index(
          name = "historylog_type_id_4_idx",
          columnList = "object4_reference_type, object4_reference_id")
    })
public class HistoryLog
    extends AbstractHistoryLog<HistoryLog, HistoryEventType, HistoryDifference> {

  private static final long serialVersionUID = -8557932643510393694L;

  public HistoryLog() {
    // nothing to do
  }

  public HistoryLog(Date date, HistoryEventType eventType, HistoryValue mainObject) {
    super(date, eventType, mainObject);
  }
}
