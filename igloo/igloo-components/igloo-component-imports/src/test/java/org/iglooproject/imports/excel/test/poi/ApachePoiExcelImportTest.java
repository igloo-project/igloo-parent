package org.iglooproject.imports.excel.test.poi;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.javatuples.Quartet;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

class ApachePoiExcelImportTest {
	
	@Test
	void testSuccess() throws TableImportException {
		InputStream stream = ApachePoiExcelImportTest.class.getResourceAsStream("/wellFormattedFile.xlsx");
		TestApachePoiExcelImporter importer = new TestApachePoiExcelImporter();
		List<Quartet<Date, Boolean, String, Integer>> results = importer.doImport(stream, "wellFormattedFile.xlsx");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, Calendar.FEBRUARY, 14);
		Date firstDate = DateUtils.truncate(calendar.getTime(), Calendar.DATE);
		calendar.set(2014, Calendar.FEBRUARY, 15);
		Date secondDate = DateUtils.truncate(calendar.getTime(), Calendar.DATE);
		
		List<Quartet<Date, Boolean, String, Integer>> expectedResults = ImmutableList.of(
				newResult(firstDate, true, "String 3", 123),
				newResult(null, false, "string 4", 123),
				newResult(null, false, null, 9723),
				newResult(secondDate, false, "4244.12", null)
		);
		
		assertEquals(expectedResults.size(), results.size());
		
		int index = 0;
		for (Quartet<Date, Boolean, String, Integer> expectedResult : expectedResults) {
			Quartet<Date, Boolean, String, Integer> actualResult = results.get(index);
			assertValueEquals(index, 0, "date", expectedResult, actualResult);
			assertValueEquals(index, 1, "boolean", expectedResult, actualResult);
			assertValueEquals(index, 2, "string", expectedResult, actualResult);
			assertValueEquals(index, 3, "integer", expectedResult, actualResult);
			++index;
		}
	}
	
	private void assertValueEquals(int rowIndex, int valueIndex, String valueName,
			Quartet<Date, Boolean, String, Integer> expectedResult, Quartet<Date, Boolean, String, Integer> actualResult) {
		Object expectedValue = expectedResult.getValue(valueIndex);
		Object actualValue = actualResult.getValue(valueIndex);
		assertEquals(expectedValue, actualValue, "Wrong " + valueName + " at row " + (rowIndex+2));
	}

	private Quartet<Date, Boolean, String, Integer> newResult(Date date, Boolean bool, String string, Integer integer) {
		return Quartet.with(date, bool, string, integer);
	}

}
