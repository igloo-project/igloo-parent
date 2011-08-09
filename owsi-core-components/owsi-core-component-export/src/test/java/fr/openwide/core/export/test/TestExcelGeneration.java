package fr.openwide.core.export.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import fr.openwide.core.export.test.export.PersonHSSFExport;
import fr.openwide.core.export.test.export.PersonXSSFExport;
import fr.openwide.core.export.test.person.Person;

public class TestExcelGeneration {

	@Test
	public void testHSSFGeneration() {
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
		persons.add(new Person("username1", "firstname1", "lastname1", new Date(), 24, 1.80, .88));
		persons.add(new Person("username2", "firstname2", "lastname2", new Date(), 25, 1.70, .20));
		persons.add(new Person("username3", "firstname3", "lastname3", new Date(), 32, 1.75, .50));
		persons.add(new Person("username4", "firstname4", "lastname4", new Date(), 53, 1.72, .21));
		persons.add(new Person("username5", "firstname5", "lastname5", new Date(), 19, 1.88, .23));

		PersonHSSFExport export = new PersonHSSFExport();
		@SuppressWarnings("unused")
		HSSFWorkbook workbook = export.generate(persons, columns);
		
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
	public void testXSSFGeneration() {
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
		persons.add(new Person("username1", "firstname1", "lastname1", new Date(), 24, 1.80, .88));
		persons.add(new Person("username2", "firstname2", "lastname2", new Date(), 25, 1.70, .20));
		persons.add(new Person("username3", "firstname3", "lastname3", new Date(), 32, 1.75, .50));
		persons.add(new Person("username4", "firstname4", "lastname4", new Date(), 53, 1.72, .21));
		persons.add(new Person("username5", "firstname5", "lastname5", new Date(), 19, 1.88, .23));

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
