package fr.openwide.core.wicket.more.export.excel.component;

import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.wicket.more.common.WorkInProgressPopup;

public class ExcelExportWorkInProgressModalPopupPanel extends WorkInProgressPopup {
	
	private static final long serialVersionUID = -2634100306392990085L;
	
	public ExcelExportWorkInProgressModalPopupPanel(String id) {
		super(id, new ResourceModel("common.action.export.excel.loading"));
	}
}
