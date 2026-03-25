package basicapp.front.common.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Component;
import org.iglooproject.wicket.more.export.excel.AbstractSimpleExcelTableExport;

public class AbstractBasicApplicationSimpleExcelTableExport extends AbstractSimpleExcelTableExport {

  protected AbstractBasicApplicationSimpleExcelTableExport(Component component) {
    super(component);
  }

  protected AbstractBasicApplicationSimpleExcelTableExport(Workbook workbook, Component component) {
    super(workbook, component);
  }

  @Override
  protected void initColors() {
    setHeaderBackgroundColor("#6610f2");
    super.initColors();
  }
}
