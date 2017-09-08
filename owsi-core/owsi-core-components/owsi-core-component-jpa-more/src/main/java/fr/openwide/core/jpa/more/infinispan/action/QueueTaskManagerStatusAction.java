package fr.openwide.core.jpa.more.infinispan.action;

import org.jgroups.Address;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.infinispan.model.impl.SimpleAction;
import fr.openwide.core.jpa.more.infinispan.model.QueueTaskManagerStatus;
import fr.openwide.core.jpa.more.infinispan.service.IInfinispanQueueTaskManagerService;

public class QueueTaskManagerStatusAction extends SimpleAction<QueueTaskManagerStatus> {

	private static final long serialVersionUID = 1740511274735773773L;

	@Autowired
	private transient IInfinispanQueueTaskManagerService infinispanQueueTaskManagerService;

	public QueueTaskManagerStatusAction(Address target) {
		super(target, false, true);
	}

	@Override
	public QueueTaskManagerStatus call() throws Exception {
		return infinispanQueueTaskManagerService.createQueueTaskManagerStatus();
	}

	public static final QueueTaskManagerStatusAction get(Address address){
		return new QueueTaskManagerStatusAction(address);
	}

}
