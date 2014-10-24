package fr.openwide.core.wicket.more.console.maintenance.task.model;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

public class BatchReportBeanModel extends GenericBatchReportBeanModel<BatchReportBean> {
	
	private static final long serialVersionUID = 1226667856014694255L;
	
	public BatchReportBeanModel(IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		super(BatchReportBean.class, queuedTaskHolderModel);
	}
}
