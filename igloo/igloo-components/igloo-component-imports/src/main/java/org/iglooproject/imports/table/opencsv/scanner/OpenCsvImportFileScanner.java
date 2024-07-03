package org.iglooproject.imports.table.opencsv.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.Validate;
import org.iglooproject.imports.table.common.csv.scanner.ICsvImportFileScanner;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.iglooproject.imports.table.common.event.exception.TableImportFileException;
import org.iglooproject.imports.table.opencsv.location.OpenCsvImportNavigator;
import org.iglooproject.imports.table.opencsv.model.CsvCell;
import org.iglooproject.imports.table.opencsv.model.CsvCellReference;
import org.iglooproject.imports.table.opencsv.model.CsvRow;
import org.iglooproject.imports.table.opencsv.model.CsvTable;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class OpenCsvImportFileScanner implements ICsvImportFileScanner<CsvTable, CsvRow, CsvCell, CsvCellReference> {
	
	private static final String HIDDEN_FILE_PREFIX = ".";

	// Default is to scan all files except hidden files and files in hidden directories.
	public static final FileFilter DEFAULT_DIRECTORY_CHILD_FILTER = FileFilterUtils.notFileFilter(FileFilterUtils.prefixFileFilter(HIDDEN_FILE_PREFIX));
	
	/**
	 * A shorthand for implementing {@code ICsvImportFileVisitor<CsvSheet, CsvRow, CsvCell, CsvCellReference>} without writing
	 * down the generics (which is painful).
	 */
	public interface IOpenCsvImportFileVisitor extends ICsvImportFileVisitor<CsvTable, CsvRow, CsvCell, CsvCellReference>{
		
	}
	
	protected FileFilter getDirectoryChildFilter() {
		return DEFAULT_DIRECTORY_CHILD_FILTER;
	}

	@Override
	public void scanRecursively(File file, String filename, ICsvImportFileVisitor<CsvTable, CsvRow, CsvCell, CsvCellReference> visitor) throws TableImportException {
		Validate.notNull(file, "file must not be null");
		Validate.notNull(visitor, "visitor must not be null");
		
		if (file.isDirectory()) {
			// Recurse
			FileFilter filter = getDirectoryChildFilter();
			for (File child : file.listFiles(filter)) {
				scanRecursively(child, child.getName(), visitor);
			}
		} else {
			scan(file, filename, visitor);
		}
	}
	
	protected Reader createReader(InputStream inputStream, String filename) throws IOException {
		return new InputStreamReader(inputStream);
	}
	
	protected CSVReader createCsvReader(Reader reader, String filename) {
		return new CSVReader(reader);
	}

	@Override
	public void scan(File file, String filename, ICsvImportFileVisitor<CsvTable, CsvRow, CsvCell, CsvCellReference> visitor) throws TableImportException {
		Validate.notNull(file, "file must not be null");
		Validate.notNull(visitor, "visitor must not be null");
		
		OpenCsvImportNavigator navigator = new OpenCsvImportNavigator(filename);
		
		try (
				InputStream stream = new FileInputStream(file);
				Reader reader = createReader(stream, filename);
				CSVReader csvReader = createCsvReader(reader, filename)
		) {
			CsvTable sheet = new CsvTable(csvReader.readAll());
			visitor.visitTable(navigator, sheet);
		} catch (IOException | CsvException e) {
			throw new TableImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

	@Override
	public void scan(InputStream stream, String filename, ICsvImportFileVisitor<CsvTable, CsvRow, CsvCell, CsvCellReference> visitor) throws TableImportException {
		Validate.notNull(stream, "stream must not be null");
		Validate.notNull(visitor, "visitor must not be null");

		OpenCsvImportNavigator navigator = new OpenCsvImportNavigator(filename);
		
		try (
				Reader reader = createReader(stream, filename);
				CSVReader csvReader = createCsvReader(reader, filename)
		) {
			CsvTable sheet = new CsvTable(csvReader.readAll());
			visitor.visitTable(navigator, sheet);
		} catch (IOException | CsvException e) {
			throw new TableImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

}
