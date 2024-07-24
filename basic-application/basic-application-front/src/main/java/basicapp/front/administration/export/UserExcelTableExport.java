package basicapp.front.administration.export;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserBinding;
import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Iterator;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.iglooproject.export.excel.ColumnInformation;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.wicket.more.export.excel.AbstractSimpleExcelTableExport;

public class UserExcelTableExport extends AbstractSimpleExcelTableExport {

  private static final String SHEET_NAME_RESOURCE_KEY =
      "administration.user.list.export.excel.sheetName";

  private final Collection<ColumnInformation> columns =
      ImmutableList.of(
          new ColumnInformation("business.user.username"),
          new ColumnInformation("business.user.lastName"),
          new ColumnInformation("business.user.firstName"),
          new ColumnInformation("business.user.email"),
          new ColumnInformation("business.user.enabled"),
          new ColumnInformation("business.user.creationDate"),
          new ColumnInformation("business.user.lastUpdateDate"),
          new ColumnInformation("business.user.lastLoginDate"));

  public UserExcelTableExport(Component component) {
    super(component);
  }

  public Workbook generate(IDataProvider<? extends User> dataProvider) {
    Sheet sheet = createSheet(localize(SHEET_NAME_RESOURCE_KEY));

    int rowIndex = 0;

    // Headers
    addHeadersToSheet(sheet, rowIndex, columns);
    ++rowIndex;

    // Rows
    Iterator<? extends User> iterator = dataProvider.iterator(0, Integer.MAX_VALUE);
    while (iterator.hasNext()) {
      Row currentRow = sheet.createRow(rowIndex);

      populateRow(currentRow, HibernateUtils.unwrap(iterator.next()));

      ++rowIndex;
    }

    finalizeSheet(sheet, columns);

    return workbook;
  }

  private void populateRow(Row row, User user) {
    UserBinding binding = new UserBinding(user);

    int columnIndex = 0;

    addTextCell(row, columnIndex++, binding.username().getSafely());
    addTextCell(row, columnIndex++, binding.lastName().getSafely());
    addTextCell(row, columnIndex++, binding.firstName().getSafely());

    XSSFCreationHelper helper = (XSSFCreationHelper) workbook.getCreationHelper();
    XSSFHyperlink emailLink = helper.createHyperlink(HyperlinkType.EMAIL);
    String emailAddress = binding.email().getSafely();
    emailLink.setAddress("mailto:" + emailAddress);
    addLinkToCell(addTextCell(row, columnIndex++, emailAddress), emailLink);

    if (binding.enabled().getSafely()) {
      addTextCell(row, columnIndex++, "Oui");
    } else {
      addTextCell(row, columnIndex++, "Non");
    }

    Instant creationDate = binding.creationDate().getSafely();
    if (creationDate != null) {
      addLocalDateCell(
          row, columnIndex++, LocalDate.ofInstant(creationDate, ZoneId.systemDefault()));
    } else {
      addTextCell(row, columnIndex++, "");
    }

    Instant lastUpdateDate = binding.lastUpdateDate().getSafely();
    if (lastUpdateDate != null) {
      addLocalDateCell(
          row, columnIndex++, LocalDate.ofInstant(lastUpdateDate, ZoneId.systemDefault()));
    } else {
      addTextCell(row, columnIndex++, "");
    }

    Instant lastLoginDate = binding.lastLoginDate().getSafely();
    if (lastLoginDate != null) {
      addLocalDateCell(
          row, columnIndex++, LocalDate.ofInstant(lastLoginDate, ZoneId.systemDefault()));
    } else {
      addTextCell(row, columnIndex++, "");
    }
  }
}
