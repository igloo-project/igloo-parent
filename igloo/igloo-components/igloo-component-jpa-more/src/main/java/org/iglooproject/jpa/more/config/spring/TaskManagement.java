package org.iglooproject.jpa.more.config.spring;

import java.util.List;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.immutables.value.Value;

@Value.Immutable
public interface TaskManagement {

  List<IQueueId> queueIds();
}
