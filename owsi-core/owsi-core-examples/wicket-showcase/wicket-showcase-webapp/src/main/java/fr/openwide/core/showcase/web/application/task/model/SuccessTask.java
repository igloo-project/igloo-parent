package fr.openwide.core.showcase.web.application.task.model;

import org.hibernate.Hibernate;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

public class SuccessTask extends AbstractTask {

	private static final long serialVersionUID = -4459628313973063902L;

	public SuccessTask() {
		super();

		setTaskName(Hibernate.getClass(this).getSimpleName());
		setTaskType(TaskTypeEnum.SUCCESS.getTaskType());
	}

	@Override
	protected void doTask(QueuedTaskHolder queuedTaskHolder) throws Exception {
	}

}
