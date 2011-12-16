package fr.openwide.core.test.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.junit.Test;

import de.schlichtherle.truezip.file.TFile;
import fr.openwide.core.commons.util.FileUtils;

public class TestFileUtils {

	private static final String DIRECTORY = "src/test/resources/FileUtils/";
	
	private static final String ZIP_FILE_PATH = DIRECTORY + "test.zip";

	@Test
	public void testGetFile() {
		File file;
		
		// Test sur un répertoire
		File directoryFile = new File(DIRECTORY);
		
		assertTrue(directoryFile.exists());
		assertTrue(directoryFile.length() > 0);
		assertTrue(directoryFile.isDirectory());
		
		file = FileUtils.getFile(directoryFile, "test1.txt");
		assertTrue(file.exists());
		
		file = FileUtils.getFile(directoryFile, "test2");
		assertTrue(file.exists());
		
		// Test sur une archive
		File archiveFile = new File(ZIP_FILE_PATH);
		
		assertTrue(archiveFile.exists());
		assertTrue(archiveFile.length() > 0);
		
		TFile archiveDirectory = new TFile(archiveFile.getAbsolutePath());
		
		assertTrue(archiveDirectory.isDirectory());
		
		file = FileUtils.getFile(archiveDirectory, "test1.xls");
		assertTrue(file.exists());
		
		file = FileUtils.getFile(archiveDirectory, "test2.doc");
		assertTrue(file.exists());
		
		file = FileUtils.getFile(archiveDirectory, "test3");
		assertTrue(file.exists());
		
		try {
			file = FileUtils.getFile(archiveDirectory, "test4.txt");
			fail("Si aucun fichier n'est trouvé avec FileUtils.getFile une exception est levée");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testList() {
		List<File> files;
		
		// Test sur un répertoire
		File directoryFile = new File(DIRECTORY);
		
		assertTrue(directoryFile.exists());
		assertTrue(directoryFile.length() > 0);
		assertTrue(directoryFile.isDirectory());
		
		files = FileUtils.list(directoryFile, new NameFileFilter("test1.txt"));
		assertEquals(1, files.size());
		assertTrue(files.iterator().next().exists());
		
		files = FileUtils.list(directoryFile, new NameFileFilter("test2"));
		assertEquals(1, files.size());
		assertTrue(files.iterator().next().exists());
		
		// Test sur une archive existante
		File archiveFile = new File(ZIP_FILE_PATH);
		
		assertTrue(archiveFile.exists());
		assertTrue(archiveFile.length() > 0);
		
		TFile archiveDirectory = new TFile(archiveFile.getAbsolutePath());
		
		assertTrue(archiveDirectory.isDirectory());
		
		files = FileUtils.list(archiveDirectory, new NameFileFilter("test1.xls"));
		assertEquals(1, files.size());
		assertTrue(files.iterator().next().exists());
		
		files = FileUtils.list(archiveDirectory, new NameFileFilter("test2.doc"));
		assertEquals(1, files.size());
		assertTrue(files.iterator().next().exists());
		
		files = FileUtils.list(archiveDirectory, new NameFileFilter("test3"));
		assertEquals(1, files.size());
		assertTrue(files.iterator().next().exists());
		
		files = FileUtils.list(archiveDirectory, new NameFileFilter("test4.txt"));
		assertEquals(0, files.size());
		
		// Test sur une archive non-existante
		archiveDirectory = new TFile("foo.bar");
		
		try {
			files = FileUtils.list(archiveDirectory, new NameFileFilter("abcdef"));
			fail("Si l'archive ne peut être lue une exception est levée");
		} catch (IllegalStateException e) {
		}
	}
}
