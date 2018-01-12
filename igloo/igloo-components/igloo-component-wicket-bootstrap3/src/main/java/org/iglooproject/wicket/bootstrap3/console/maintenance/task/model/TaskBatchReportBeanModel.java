package org.iglooproject.wicket.bootstrap3.console.maintenance.task.model;

import org.apache.wicket.model.IModel;

import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.more.model.BindingModel;

public class TaskBatchReportBeanModel<B extends BatchReportBean> extends BatchReportBeanModel<B> {
	
	private static final long serialVersionUID = 421890607339627456L;
	
	public TaskBatchReportBeanModel(Class<B> clazz, IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		super(clazz, BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().report()));
	}
}
