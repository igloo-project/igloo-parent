package fr.openwide.core.showcase.core.business.task.model;

import java.util.Map;

import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.report.BatchReport;
import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;

public class ShowcaseBatchReportBean extends BatchReportBean {
	
	private static final long serialVersionUID = 2709053770448419280L;
	
	private Map<Long, String> notTreatedObjects = Maps.newLinkedHashMap();

	protected ShowcaseBatchReportBean() {
	}

	public ShowcaseBatchReportBean(BatchReport batchReport, Map<Long, String> notTreatedObjects) {
		super(batchReport);
		this.notTreatedObjects = notTreatedObjects;
	}

	// Do not use methods like ImmutableMap.copyOf(notTreatedObjects) or other,
	// because serialized objects will not be deserialized, because of constructor lack
	public Map<Long, String> getNotTreatedObjects() {
		return notTreatedObjects;
	}

	public void setNotTreatedObjects(Map<Long, String> notTreatedObjects) {
		this.notTreatedObjects = notTreatedObjects;
	}
}
