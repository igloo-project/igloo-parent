package fr.openwide.core.showcase.web.application.task.model;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.collect.Sets;

import fr.openwide.core.showcase.core.business.task.model.ShowcaseBatchReportBean;

public class NotTreatedObjectsIdsModel extends LoadableDetachableModel<Set<Long>> {
	
	private static final long serialVersionUID = 5492136947148504828L;
	
	private final IModel<ShowcaseBatchReportBean> reportBeanModel;
	
	public NotTreatedObjectsIdsModel(IModel<ShowcaseBatchReportBean> reportBeanModel) {
		this.reportBeanModel = reportBeanModel;
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		reportBeanModel.detach();
	}
	
	@Override
	protected Set<Long> load() {
		ShowcaseBatchReportBean reportBean = reportBeanModel.getObject();
		return reportBean != null ? reportBean.getNotTreatedObjects().keySet() : Sets.<Long>newHashSet();
	}
}
