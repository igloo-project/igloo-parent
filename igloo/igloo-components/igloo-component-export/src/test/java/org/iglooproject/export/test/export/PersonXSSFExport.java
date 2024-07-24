package org.iglooproject.export.test.export;

import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iglooproject.export.excel.AbstractExcelTableExport;
import org.iglooproject.export.test.person.Person;

public class PersonXSSFExport extends AbstractExcelTableExport {

  public PersonXSSFExport() {
    super(new XSSFWorkbook());

    // C'est entre l'appel au constructeur et le init()
    // qu'il faut surcharger les couleurs et les polices
    setFontName("Calibri");
    setNormalFontHeight((short) 10);
    setHeaderFontHeight((short) 12);
    setHeaderFontColor("#FFFFFF");

    init();
  }

  public XSSFWorkbook generate(List<Person> persons, List<String> columns) {
    XSSFSheet sheet = (XSSFSheet) createSheet("Document .xlsx");

    int rowIndex = 0;

    addHeadersToSheet(sheet, rowIndex, columns);
    rowIndex++;

    for (Person person : persons) {
      XSSFRow currentRow = sheet.createRow(rowIndex);
      rowIndex++;

      int columnIndex = 0;

      for (String column : columns) {
        addCell(currentRow, columnIndex, person, column);
        columnIndex++;
      }
    }

    finalizeSheet(sheet, columns);

    return (XSSFWorkbook) workbook;
  }

  private void addCell(XSSFRow currentRow, int columnIndex, Person person, String column) {
    if ("username".equals(column)) {
      addTextCell(currentRow, columnIndex, person.getUsername());
    } else if ("firstname".equals(column)) {
      addTextCell(currentRow, columnIndex, person.getFirstName());
    } else if ("lastname".equals(column)) {
      addTextCell(currentRow, columnIndex, person.getLastName());
    } else if ("birth date".equals(column)) {
      addLocalDateCell(currentRow, columnIndex, person.getBirthDateTime().toLocalDate());
    } else if ("birth hour".equals(column)) {
      addLocalDateTimeCell(currentRow, columnIndex, person.getBirthDateTime());
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
