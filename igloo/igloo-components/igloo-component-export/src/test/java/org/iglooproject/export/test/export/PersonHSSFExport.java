package org.iglooproject.export.test.export;

import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.iglooproject.export.excel.AbstractExcelTableExport;
import org.iglooproject.export.excel.ColumnInformation;
import org.iglooproject.export.test.person.Person;

public class PersonHSSFExport extends AbstractExcelTableExport {

  public PersonHSSFExport() {
    super(new HSSFWorkbook());

    // C'est entre l'appel au constructeur et le init()
    // qu'il faut surcharger les couleurs et les polices
    setFontName("Calibri");
    setNormalFontHeight((short) 10);
    setHeaderFontHeight((short) 12);
    setHeaderFontColor("#FFFFFF");

    init();
  }

  public HSSFWorkbook generate(List<Person> persons, List<ColumnInformation> columnsInfos) {
    HSSFSheet sheet = (HSSFSheet) createSheet("Feuille 1");

    int rowIndex = 0;

    addHeadersToSheet(sheet, rowIndex, columnsInfos);
    rowIndex++;

    for (Person person : persons) {
      HSSFRow currentRow = sheet.createRow(rowIndex);
      rowIndex++;

      int columnIndex = 0;

      for (ColumnInformation columnInfo : columnsInfos) {
        addCell(currentRow, columnIndex, person, columnInfo.getHeaderKey());
        columnIndex++;
      }
    }

    finalizeSheet(sheet, columnsInfos);

    return (HSSFWorkbook) workbook;
  }

  private void addCell(HSSFRow currentRow, int columnIndex, Person person, String column) {
    if ("username".equals(column)) {
      addTextCell(currentRow, columnIndex, person.getUsername());
    } else if ("firstname".equals(column)) {
      addTextCell(currentRow, columnIndex, person.getFirstName());
    } else if ("lastname".equals(column)) {
      addTextCell(currentRow, columnIndex, person.getLastName());
    } else if ("birth date".equals(column)) {
      addDateCell(currentRow, columnIndex, person.getBirthDate());
    } else if ("birth hour".equals(column)) {
      addDateTimeCell(currentRow, columnIndex, person.getBirthDate());
    } else if ("age".equals(column)) {
      addIntegerCell(currentRow, columnIndex, person.getAge());
    } else if ("size".equals(column)) {
      addDecimalCell(currentRow, columnIndex, person.getSize());
    } else if ("percentage".equals(column)) {
      addPercentCell(currentRow, columnIndex, person.getPercentage());
    }
  }

  @Override
  protected String localize(String key) {
    return key;
  }
}
