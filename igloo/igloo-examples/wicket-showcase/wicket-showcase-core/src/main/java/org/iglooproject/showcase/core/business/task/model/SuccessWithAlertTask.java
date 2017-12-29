package org.iglooproject.showcase.core.business.task.model;

import java.util.Date;

import org.iglooproject.commons.util.report.BatchReport;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.model.TaskExecutionResult;

public class SuccessWithAlertTask extends AbstractTask {

	private static final long serialVersionUID = -4459628313973063902L;
	
	private ShowcaseTaskQueueId queueId;
	
	protected SuccessWithAlertTask() { }
	
	public SuccessWithAlertTask(ShowcaseTaskQueueId queueId) {
		super(SuccessWithAlertTask.class.getSimpleName(), TaskTypeEnum.SUCCESS.getTaskType(), new Date());
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
		
		batchReport.setContext("My context");
		batchReport.info("Task execution info in specific context.");
		batchReport.warn("Task execution warn in specific context.");
		
		return TaskExecutionResult.completed(new BatchReportBean(batchReport));
	}
}
