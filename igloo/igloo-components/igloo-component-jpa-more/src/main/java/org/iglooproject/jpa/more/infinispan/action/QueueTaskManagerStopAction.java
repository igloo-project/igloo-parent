package org.iglooproject.jpa.more.infinispan.action;

import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.impl.SimpleAction;
import org.iglooproject.jpa.more.infinispan.service.IInfinispanQueueTaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;

public class QueueTaskManagerStopAction extends SimpleAction<SwitchStatusQueueTaskManagerResult> {

	private static final long serialVersionUID = -5968225724015355537L;

	@Autowired
	private transient IInfinispanQueueTaskManagerService infinispanQueueTaskManagerService;

	protected QueueTaskManagerStopAction(AddressWrapper target) {
		super(target, false, true);
	}

	@Override
	public SwitchStatusQueueTaskManagerResult call() throws Exception {
		return infinispanQueueTaskManagerService.stop();
	}

	public static final QueueTaskManagerStopAction get(AddressWrapper address){
		return new QueueTaskManagerStopAction(address);
	}

}
