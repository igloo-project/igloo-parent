package org.iglooproject.showcase.web.application.task.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import org.iglooproject.showcase.core.business.task.model.ShowcaseBatchReportBean;

public class NotTreatedObjectNameModel extends LoadableDetachableModel<String> {
	
	private static final long serialVersionUID = 2876610662675776425L;
	
	private final IModel<ShowcaseBatchReportBean> reportBeanModel;
	
	private final IModel<Long> idModel;
	
	public NotTreatedObjectNameModel(IModel<ShowcaseBatchReportBean> reportBeanModel, IModel<Long> idModel) {
		this.reportBeanModel = reportBeanModel;
		this.idModel = idModel;
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		reportBeanModel.detach();
		idModel.detach();
	}
	
	@Override
	protected String load() {
		ShowcaseBatchReportBean showcaseReportBean = reportBeanModel.getObject();
		Long id = idModel.getObject();
		if (showcaseReportBean != null && id != null) {
			return showcaseReportBean.getNotTreatedObjects().get(id);
		}
		return null;
	}
}
