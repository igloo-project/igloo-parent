package org.iglooproject.wicket.more.console.maintenance.task.model;

import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;

import igloo.wicket.model.BindingModel;

public class TaskBatchReportBeanModel<B extends BatchReportBean> extends BatchReportBeanModel<B> {

	private static final long serialVersionUID = 421890607339627456L;

	public TaskBatchReportBeanModel(Class<B> clazz, IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		super(clazz, BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().report()));
	}

}
