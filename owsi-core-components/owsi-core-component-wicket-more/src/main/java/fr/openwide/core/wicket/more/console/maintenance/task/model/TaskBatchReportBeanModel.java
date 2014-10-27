package fr.openwide.core.wicket.more.console.maintenance.task.model;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;
import fr.openwide.core.wicket.more.model.BindingModel;

public class TaskBatchReportBeanModel<B extends BatchReportBean> extends BatchReportBeanModel<B> {
	
	private static final long serialVersionUID = 421890607339627456L;
	
	public TaskBatchReportBeanModel(Class<B> clazz, IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		super(clazz, BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().report()));
	}
}
