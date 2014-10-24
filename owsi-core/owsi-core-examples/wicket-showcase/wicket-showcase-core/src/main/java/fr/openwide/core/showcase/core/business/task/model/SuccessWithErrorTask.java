package fr.openwide.core.showcase.core.business.task.model;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.report.BatchReport;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.TaskExecutionResult;

public class SuccessWithErrorTask extends AbstractTask {

	private static final long serialVersionUID = -4459628313973063902L;
	
	private ShowcaseTaskQueueId queueId;

	protected SuccessWithErrorTask() { }

	public SuccessWithErrorTask(ShowcaseTaskQueueId queueId) {
		super(SuccessWithErrorTask.class.getSimpleName(), TaskTypeEnum.SUCCESS.getTaskType(), new Date());
		this.queueId = queueId;
	}
	
	@Override
	public IQueueId selectQueue() {
		return queueId;
	}
	
	@Override
	protected TaskExecutionResult doTask() throws Exception {
		BatchReport batchReport = new BatchReport();
		
		Map<Long, String> notTreatedObjects = Maps.newHashMap();
		
		batchReport.info("Task execution info.");
		
		batchReport.setContext("My context");
		batchReport.info("Task execution info in specific context.");
		batchReport.warn("Task execution warn in specific context.");
		
		batchReport.info("Treated object.");
		batchReport.info("Treated object.");
		batchReport.error("Not treated object.");
		notTreatedObjects.put(3L, "My 3rd object");
		batchReport.info("Treated object.");
		
		batchReport.setContext(BatchReport.GLOBAL_CONTEXT);
		batchReport.error("Task execution error in global context.");
		
		return TaskExecutionResult.completed(new ShowcaseBatchReportBean(batchReport, notTreatedObjects));
	}
}
