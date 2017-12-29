package org.iglooproject.jpa.more.infinispan.action;

import org.jgroups.Address;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.infinispan.model.impl.SimpleAction;
import org.iglooproject.jpa.more.infinispan.service.IInfinispanQueueTaskManagerService;

public class QueueTaskManagerStartAction extends SimpleAction<SwitchStatusQueueTaskManagerResult> {

	private static final long serialVersionUID = 1854734111545990917L;

	@Autowired
	private transient IInfinispanQueueTaskManagerService infinispanQueueTaskManagerService;

	protected QueueTaskManagerStartAction(Address target) {
		super(target, false, true);
	}

	@Override
	public SwitchStatusQueueTaskManagerResult call() throws Exception {
		return infinispanQueueTaskManagerService.start();
	}

	public static final QueueTaskManagerStartAction get(Address address){
		return new QueueTaskManagerStartAction(address);
	}

}
