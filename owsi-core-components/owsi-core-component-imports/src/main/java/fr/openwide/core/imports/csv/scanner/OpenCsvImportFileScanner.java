package fr.openwide.core.imports.csv.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.Validate;

import au.com.bytecode.opencsv.CSVReader;
import de.schlichtherle.truezip.file.TFileInputStream;
import fr.openwide.core.imports.csv.location.OpenCsvImportNavigator;
import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportFileException;

public class OpenCsvImportFileScanner implements ICsvImportFileScanner<CsvSheet, CsvRow, CsvCell, CsvCellReference> {
	
	private static final String HIDDEN_FILE_PREFIX = ".";

	// Default is to scan all files except hidden files and files in hidden directories.
	public static final FileFilter DEFAULT_DIRECTORY_CHILD_FILTER = FileFilterUtils.notFileFilter(FileFilterUtils.prefixFileFilter(HIDDEN_FILE_PREFIX));
	
	/**
	 * A shorthand for implementing {@code ICsvImportFileVisitor<CsvSheet, CsvRow, CsvCell, CsvCellReference>} without writing
	 * down the generics (which is painful).
	 */
	public interface IOpenCsvImportFileVisitor extends ICsvImportFileVisitor<CsvSheet, CsvRow, CsvCell, CsvCellReference>{
		
	}
	
	protected FileFilter getDirectoryChildFilter() {
		return DEFAULT_DIRECTORY_CHILD_FILTER;
	}

	@Override
	public void scanRecursively(File file, String filename, ICsvImportFileVisitor<CsvSheet, CsvRow, CsvCell, CsvCellReference> visitor) throws ExcelImportException {
		Validate.notNull(file);
		Validate.notNull(visitor);
		
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
	public void scan(File file, String filename, ICsvImportFileVisitor<CsvSheet, CsvRow, CsvCell, CsvCellReference> visitor) throws ExcelImportException {
		Validate.notNull(file);
		Validate.notNull(visitor);
		
		OpenCsvImportNavigator navigator = new OpenCsvImportNavigator(filename);
		
		try (
				InputStream stream = new TFileInputStream(file);
				Reader reader = createReader(stream, filename);
				CSVReader csvReader = createCsvReader(reader, filename)
		) {
			CsvSheet sheet = new CsvSheet(csvReader.readAll());
			visitor.visitSheet(navigator, sheet);
		} catch (IOException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

	@Override
	public void scan(InputStream stream, String filename, ICsvImportFileVisitor<CsvSheet, CsvRow, CsvCell, CsvCellReference> visitor) throws ExcelImportException {
		Validate.notNull(stream);
		Validate.notNull(visitor);

		OpenCsvImportNavigator navigator = new OpenCsvImportNavigator(filename);
		
		try (
				Reader reader = createReader(stream, filename);
				CSVReader csvReader = createCsvReader(reader, filename)
		) {
			CsvSheet sheet = new CsvSheet(csvReader.readAll());
			visitor.visitSheet(navigator, sheet);
		} catch (IOException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

}
