package fr.openwide.core.showcase.core.business.task.model;

import java.util.Date;

import fr.openwide.core.commons.util.report.BatchReport;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.TaskExecutionResult;

public class SuccessTask extends AbstractTask {

	private static final long serialVersionUID = -4459628313973063902L;
	
	private ShowcaseTaskQueueId queueId;

	protected SuccessTask() { }

	public SuccessTask(ShowcaseTaskQueueId queueId) {
		super(SuccessTask.class.getSimpleName(), TaskTypeEnum.SUCCESS.getTaskType(), new Date());
		this.queueId = queueId;
	}
	
	@Override
	public IQueueId selectQueue() {
		return queueId;
	}

	@Override
	protected TaskExecutionResult doTask() throws Exception {
		BatchReport batchReport = new BatchReport();
		
		batchReport.info("Task execution info.");
		
		return TaskExecutionResult.completed(new BatchReportBean(batchReport));
	}

}
