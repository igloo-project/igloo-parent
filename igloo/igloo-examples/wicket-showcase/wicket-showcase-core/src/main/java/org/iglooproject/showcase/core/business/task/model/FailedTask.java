package org.iglooproject.showcase.core.business.task.model;

import java.util.Date;

import org.iglooproject.commons.util.report.BatchReport;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.model.TaskExecutionResult;

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
	protected TaskExecutionResult doTask() throws Exception {
		BatchReport batchReport = new BatchReport();
		
		batchReport.info("Task execution info.");
		
		throw new Exception("Unexpected technical exception from a FailedTask.");
		
		// Rest of the task...
	}

}
