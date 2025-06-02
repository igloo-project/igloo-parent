package org.iglooproject.imports.excel.test.poi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.javatuples.Quartet;
import org.junit.jupiter.api.Test;

class ApachePoiExcelImportTest {

  @Test
  void testSuccess() throws TableImportException {
    InputStream stream =
        ApachePoiExcelImportTest.class.getResourceAsStream("/wellFormattedFile.xlsx");
    TestApachePoiExcelImporter importer = new TestApachePoiExcelImporter();
    List<Quartet<LocalDate, Boolean, String, Integer>> results =
        importer.doImport(stream, "wellFormattedFile.xlsx");

    List<Quartet<LocalDate, Boolean, String, Integer>> expectedResults =
        List.of(
            newResult(LocalDate.of(2014, Month.FEBRUARY, 14), true, "String 3", 123),
            newResult(null, false, "string 4", 123),
            newResult(null, false, null, 9723),
            newResult(LocalDate.of(2014, Month.FEBRUARY, 15), false, "4244.12", null));

    assertEquals(expectedResults.size(), results.size());

    int index = 0;
    for (Quartet<LocalDate, Boolean, String, Integer> expectedResult : expectedResults) {
      Quartet<LocalDate, Boolean, String, Integer> actualResult = results.get(index);
      assertValueEquals(index, 0, "date", expectedResult, actualResult);
      assertValueEquals(index, 1, "boolean", expectedResult, actualResult);
      assertValueEquals(index, 2, "string", expectedResult, actualResult);
      assertValueEquals(index, 3, "integer", expectedResult, actualResult);
      ++index;
    }
  }

  private void assertValueEquals(
      int rowIndex,
      int valueIndex,
      String valueName,
      Quartet<LocalDate, Boolean, String, Integer> expectedResult,
      Quartet<LocalDate, Boolean, String, Integer> actualResult) {
    Object expectedValue = expectedResult.getValue(valueIndex);
    Object actualValue = actualResult.getValue(valueIndex);
    assertEquals(expectedValue, actualValue, "Wrong " + valueName + " at row " + (rowIndex + 2));
  }

  private Quartet<LocalDate, Boolean, String, Integer> newResult(
      LocalDate date, Boolean bool, String string, Integer integer) {
    return Quartet.with(date, bool, string, integer);
  }
}
