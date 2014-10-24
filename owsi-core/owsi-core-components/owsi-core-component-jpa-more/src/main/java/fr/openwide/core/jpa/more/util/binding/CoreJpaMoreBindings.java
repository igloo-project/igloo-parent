package fr.openwide.core.jpa.more.util.binding;

import fr.openwide.core.commons.util.report.BatchReportItemBinding;
import fr.openwide.core.jpa.more.business.task.model.BatchReportBeanBinding;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolderBinding;

public final class CoreJpaMoreBindings {

	private static final QueuedTaskHolderBinding QUEUED_TASK_HOLDER = new QueuedTaskHolderBinding();
	private static final BatchReportBeanBinding BATCH_REPORT_BEAN = new BatchReportBeanBinding();
	private static final BatchReportItemBinding BATCH_REPORT_ITEM = new BatchReportItemBinding();
	
	public static QueuedTaskHolderBinding queuedTaskHolder() {
		return QUEUED_TASK_HOLDER;
	}
	
	public static BatchReportBeanBinding batchReportBean() {
		return BATCH_REPORT_BEAN;
	}
	
	public static BatchReportItemBinding batchReportItem() {
		return BATCH_REPORT_ITEM;
	}
	
	private CoreJpaMoreBindings() {
	}

}
