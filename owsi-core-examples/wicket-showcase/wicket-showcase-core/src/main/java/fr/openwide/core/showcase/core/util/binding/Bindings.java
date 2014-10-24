package fr.openwide.core.showcase.core.util.binding;

import fr.openwide.core.showcase.core.business.task.model.ShowcaseBatchReportBeanBinding;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;

public final class Bindings {

	private Bindings() { }
	
	private static final UserBinding USER = new UserBinding();
	
	private static final ShowcaseBatchReportBeanBinding SHOWCASE_BATCH_REPORT = new ShowcaseBatchReportBeanBinding();
	
	public static UserBinding user() {
		return USER;
	}
	
	public static ShowcaseBatchReportBeanBinding showcaseBatchReport() {
		return SHOWCASE_BATCH_REPORT;
	}
}
