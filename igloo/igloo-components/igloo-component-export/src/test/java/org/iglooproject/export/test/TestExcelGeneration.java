package org.iglooproject.export.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iglooproject.export.excel.ColumnInformation;
import org.iglooproject.export.test.export.PersonHSSFExport;
import org.iglooproject.export.test.export.PersonXSSFExport;
import org.iglooproject.export.test.person.Person;
import org.junit.jupiter.api.Test;

class TestExcelGeneration {

  @Test
  void testHSSFGeneration() {
    int maxColumnWidth = 2000;

    List<ColumnInformation> columns =
        ImmutableList.<ColumnInformation>builder()
            .add(new ColumnInformation("username", false, maxColumnWidth))
            .add(new ColumnInformation("firstname", false, maxColumnWidth, 1500))
            .add(new ColumnInformation("lastname", false, maxColumnWidth))
            .add(new ColumnInformation("birth date", true, maxColumnWidth, 2500))
            .add(new ColumnInformation("birth hour", true, maxColumnWidth))
            .add(new ColumnInformation("age", false, maxColumnWidth))
            .add(new ColumnInformation("size", true, maxColumnWidth))
            .add(new ColumnInformation("percentage", false, maxColumnWidth))
            .build();

    List<Person> persons = new LinkedList<Person>();
    persons.add(
        new Person("username1", "firstname1", "lastname1", LocalDateTime.now(), 24, 1.80, .88));
    persons.add(
        new Person("username2", "firstname2", "lastname2", LocalDateTime.now(), 25, 1.70, .20));
    persons.add(
        new Person("username3", "firstname3", "lastname3", LocalDateTime.now(), 32, 1.75, .50));
    persons.add(
        new Person("username4", "firstname4", "lastname4", LocalDateTime.now(), 53, 1.72, .21));
    persons.add(
        new Person("username5", "firstname5", "lastname5", LocalDateTime.now(), 19, 1.88, .23));

    PersonHSSFExport export = new PersonHSSFExport();
    HSSFWorkbook workbook = export.generate(persons, columns);

    // Validation de la taille des colonnes
    int columnIndex = 0;
    for (ColumnInformation column : columns) {
      if ("firstname".equals(column.getHeaderKey())) {
        assertEquals(1500, workbook.getSheetAt(0).getColumnWidth(columnIndex));
      } else {
        assertTrue(workbook.getSheetAt(0).getColumnWidth(columnIndex) <= 2000);
      }

      columnIndex++;
    }

    // Génération du fichier
    //		try {
    //			FileOutputStream outputStream = new FileOutputStream("/testHSSF.xls");
    //			workbook.write(outputStream);
    //			outputStream.close();
    //		} catch (FileNotFoundException e) {
    //			e.printStackTrace();
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		}
  }

  @Test
  void testXSSFGeneration() {
    List<String> columns = new ArrayList<String>();
    columns.add("username");
    columns.add("firstname");
    columns.add("lastname");
    columns.add("birth date");
    columns.add("birth hour");
    columns.add("age");
    columns.add("size");
    columns.add("percentage");

    List<Person> persons = new LinkedList<Person>();
    persons.add(
        new Person("username1", "firstname1", "lastname1", LocalDateTime.now(), 24, 1.80, .88));
    persons.add(
        new Person("username2", "firstname2", "lastname2", LocalDateTime.now(), 25, 1.70, .20));
    persons.add(
        new Person("username3", "firstname3", "lastname3", LocalDateTime.now(), 32, 1.75, .50));
    persons.add(
        new Person("username4", "firstname4", "lastname4", LocalDateTime.now(), 53, 1.72, .21));
    persons.add(
        new Person("username5", "firstname5", "lastname5", LocalDateTime.now(), 19, 1.88, .23));

    PersonXSSFExport export = new PersonXSSFExport();
    @SuppressWarnings("unused")
    XSSFWorkbook workbook = export.generate(persons, columns);

    // Génération du fichier
    //		try {
    //			FileOutputStream outputStream = new FileOutputStream("/testXSSF.xlsx");
    //			workbook.write(outputStream);
    //			outputStream.close();
    //		} catch (FileNotFoundException e) {
    //			e.printStackTrace();
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		}
  }
}
