package fr.openwide.core.showcase.core.business.task.model;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.commons.util.report.BatchReport;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.TaskExecutionResult;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;

public class FailedWithBusinessExceptionTask extends AbstractTask {

	private static final long serialVersionUID = 5633587943863303524L;
	
	@Autowired
	private IUserService userService;
	
	private ShowcaseTaskQueueId queueId;

	protected FailedWithBusinessExceptionTask() { }

	public FailedWithBusinessExceptionTask(ShowcaseTaskQueueId queueId) {
		super(FailedWithBusinessExceptionTask.class.getSimpleName(), TaskTypeEnum.FAILED.getTaskType(), new Date());
		this.queueId = queueId;
	}
	
	@Override
	public IQueueId selectQueue() {
		return queueId;
	}

	@Override
	protected TaskExecutionResult doTask() throws Exception {
		BatchReport batchReport = new BatchReport();
		try {
			doSomething(batchReport);
			return TaskExecutionResult.completed(new BatchReportBean(batchReport));
		} catch (MyBusinessException e) {
			batchReport.error("Task stopped and cancelled by business exception.");
			return TaskExecutionResult.failed(new BatchReportBean(batchReport), e);
		}
	}
	
	protected void doSomething(BatchReport batchReport) throws MyBusinessException, ServiceException, SecurityServiceException {
		batchReport.info("Doing something.");
		
		// Will be rolled back in case of task failure
		User user = userService.getByUserName("admin");
		user.setPosition(user.getPosition() + 1);
		userService.update(user);
		
		try {
			try {
				doSomethingWichFail();
			} catch (Exception e) {
				throw new IllegalStateException("No way.", e);
			}
		} catch (Exception e) {
			throw new MyBusinessException("Expected business exception wich must cancel the task.", e);
		}
		
		// Rest of the task...
	}
	
	protected void doSomethingWichFail() throws IOException {
		throw new IOException("IO error.");
	}

}
