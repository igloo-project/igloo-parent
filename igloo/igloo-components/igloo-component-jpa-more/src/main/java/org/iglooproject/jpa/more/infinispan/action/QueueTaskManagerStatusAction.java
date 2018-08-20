package org.iglooproject.jpa.more.infinispan.action;

import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.impl.SimpleAction;
import org.iglooproject.jpa.more.infinispan.model.QueueTaskManagerStatus;
import org.iglooproject.jpa.more.infinispan.service.IInfinispanQueueTaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;

public class QueueTaskManagerStatusAction extends SimpleAction<QueueTaskManagerStatus> {

	private static final long serialVersionUID = 1740511274735773773L;

	@Autowired
	private transient IInfinispanQueueTaskManagerService infinispanQueueTaskManagerService;

	public QueueTaskManagerStatusAction(AddressWrapper target) {
		super(target, false, true);
	}

	@Override
	public QueueTaskManagerStatus call() throws Exception {
		return infinispanQueueTaskManagerService.createQueueTaskManagerStatus();
	}

	public static final QueueTaskManagerStatusAction get(AddressWrapper address){
		return new QueueTaskManagerStatusAction(address);
	}

}
