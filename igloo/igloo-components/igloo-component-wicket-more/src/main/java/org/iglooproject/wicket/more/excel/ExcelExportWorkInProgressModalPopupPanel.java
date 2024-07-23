package org.iglooproject.wicket.more.excel;

import igloo.bootstrap.modal.WorkInProgressPopup;
import org.apache.wicket.model.ResourceModel;

public class ExcelExportWorkInProgressModalPopupPanel extends WorkInProgressPopup {

  private static final long serialVersionUID = -2634100306392990085L;

  public ExcelExportWorkInProgressModalPopupPanel(String id) {
    super(id, new ResourceModel("common.action.export.excel.loading"));
  }
}
