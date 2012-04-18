package fr.openwide.export.excel.example;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.export.excel.example.export.PersonXSSFExport;
import fr.openwide.export.excel.example.person.Person;

public final class XSSFExcelGeneration {

	private static final String XLS_FILE_NAME = "my_person_example.xlsx";

	private static final Logger LOGGER = LoggerFactory.getLogger(XSSFExcelGeneration.class);

	public static void main(String[] args) {
		List<String> columns = new ArrayList<String>();
		columns.add("username");
		columns.add("firstname");
		columns.add("lastname");
		columns.add("birth date");
		columns.add("birth hour");
		columns.add("size");
		columns.add("percentage");

		List<Person> persons = new LinkedList<Person>();
		persons.add(new Person("username1", "firstname1", "lastname1", new Date(), 180, .88));
		persons.add(new Person("username2", "firstname2", "lastname2", new Date(), 170, .20));
		persons.add(new Person("username3", "firstname3", "lastname3", new Date(), 175, .50));
		persons.add(new Person("username4", "firstname4", "lastname4", new Date(), 172, .21));
		persons.add(new Person("username5", "firstname5", "lastname5", new Date(), 188, .23));

		PersonXSSFExport export = new PersonXSSFExport();
		XSSFWorkbook workbook = export.generate(persons, columns);

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(XLS_FILE_NAME);
			workbook.write(outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found", e);
		} catch (IOException e) {
			LOGGER.error("I/O error", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private XSSFExcelGeneration() {
	}
}
