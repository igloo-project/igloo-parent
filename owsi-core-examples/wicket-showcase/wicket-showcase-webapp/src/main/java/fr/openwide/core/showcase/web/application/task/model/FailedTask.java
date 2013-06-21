package fr.openwide.core.showcase.web.application.task.model;

import org.hibernate.Hibernate;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

public class FailedTask extends AbstractTask {

	private static final long serialVersionUID = 5633587943863303524L;

	public FailedTask() {
		super();

		setTaskName(Hibernate.getClass(this).getSimpleName());
		setTaskType(TaskTypeEnum.FAILED.getTaskType());
	}

	@Override
	protected void doTask(QueuedTaskHolder queuedTaskHolder) throws Exception {
		queuedTaskHolder.setName("Pray for a rollback");
		queuedTaskHolderService.update(queuedTaskHolder);

		throw new Exception("Excepted exception from a ErrorTask.");
	}

}
