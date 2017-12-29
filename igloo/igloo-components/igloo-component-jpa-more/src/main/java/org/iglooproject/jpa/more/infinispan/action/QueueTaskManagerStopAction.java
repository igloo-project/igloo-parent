package org.iglooproject.jpa.more.infinispan.action;

import org.jgroups.Address;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.infinispan.model.impl.SimpleAction;
import org.iglooproject.jpa.more.infinispan.service.IInfinispanQueueTaskManagerService;

public class QueueTaskManagerStopAction extends SimpleAction<SwitchStatusQueueTaskManagerResult> {

	private static final long serialVersionUID = -5968225724015355537L;

	@Autowired
	private transient IInfinispanQueueTaskManagerService infinispanQueueTaskManagerService;

	protected QueueTaskManagerStopAction(Address target) {
		super(target, false, true);
	}

	@Override
	public SwitchStatusQueueTaskManagerResult call() throws Exception {
		return infinispanQueueTaskManagerService.stop();
	}

	public static final QueueTaskManagerStopAction get(Address address){
		return new QueueTaskManagerStopAction(address);
	}

}
