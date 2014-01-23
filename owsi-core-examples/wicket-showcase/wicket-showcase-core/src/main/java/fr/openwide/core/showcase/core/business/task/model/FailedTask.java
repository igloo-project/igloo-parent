package fr.openwide.core.showcase.core.business.task.model;

import java.util.Date;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

public class FailedTask extends AbstractTask {

	private static final long serialVersionUID = 5633587943863303524L;
	
	private ShowcaseTaskQueueId queueId;

	protected FailedTask() { }

	public FailedTask(ShowcaseTaskQueueId queueId) {
		super(FailedTask.class.getSimpleName(), TaskTypeEnum.FAILED.getTaskType(), new Date());
		this.queueId = queueId;
	}
	
	@Override
	public IQueueId selectQueue() {
		return queueId;
	}

	@Override
	protected void doTask(QueuedTaskHolder queuedTaskHolder) throws Exception {
		queuedTaskHolder.setName("Pray for a rollback");
		queuedTaskHolderService.update(queuedTaskHolder);

		throw new Exception("Expected exception from a FailedTask.");
	}

}
