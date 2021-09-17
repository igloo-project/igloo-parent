package org.iglooproject.test.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.filefilter.DelegateFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.iglooproject.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import de.schlichtherle.truezip.file.TFile;

public class TestFileUtils {

	private static final String DIRECTORY = "src/test/resources/FileUtils/";
	
	private static final String ZIP_FILE_PATH = DIRECTORY + "test.zip";

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testGetFile() {
		File file;
		
		// Test sur un répertoire
		File directoryFile = new File(DIRECTORY);
		
		assertTrue(directoryFile.exists());
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
	}

	/**
	 * Behavior check for getting a null file
	 */
	@Test
	public void testGetError() throws IOException {
		File archiveFile = new File(ZIP_FILE_PATH);
		TFile archiveDirectory = new TFile(archiveFile.getAbsolutePath());
		Assertions.assertThatCode(() -> FileUtils.getFile(archiveDirectory, "test4.txt")).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testList() {
		List<File> files;
		
		// Test sur un répertoire
		File directoryFile = new File(DIRECTORY);
		
		assertTrue(directoryFile.exists());
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
	}

	/**
	 * Behavior check when list on a not existing folder
	 */
	@Test
	public void testListNotExisting() {
		// Test sur une archive non-existante
		File archiveDirectory = new TFile("foo.bar");
		
		Assertions.assertThatCode(() -> FileUtils.list(archiveDirectory, new NameFileFilter("abcdef")))
			.isInstanceOf(IllegalStateException.class);
	}

	/**
	 * Behavior check for list on null
	 */
	@Test
	public void nullDirectory() {
		Assertions.assertThatCode(() -> FileUtils.getFile(null, "test")).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * Behavior with a non-existing target
	 */
	@Test
	public void cleanNotExisting() {
		File file = new File(folder.getRoot(), "notExisting");
		Assertions.assertThatCode(() -> FileUtils.cleanDirectory(file, null)).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * Behavior with a null argument
	 */
	@Test
	public void cleanDirectoryNull() {
		Assertions.assertThatCode(() -> FileUtils.cleanDirectory(null, null)).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * Behavior with a file instead of a directory
	 */
	@Test
	public void testCleanDirectoryWithFile() throws IOException {
		File file = folder.newFile("test");
		Assertions.assertThatCode(() -> FileUtils.cleanDirectory(file, null)).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * Clean all content
	 */
	@Test
	public void cleanDirectoryAll() throws IOException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		File dir1 = new File(subFolder, "dir1");
		File file3 = new File(dir1, "file3");
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		FileUtils.cleanDirectory(subFolder, null);
		
		Assertions.assertThat(folder.getRoot().exists()).isTrue().as("Parent folder must be kept");
		Assertions.assertThat(subFolder.exists()).isTrue().as("Cleaned folder must be kept");
		Assertions.assertThat(file1.exists()).isFalse().as("This file must be cleaned");
		Assertions.assertThat(file2.exists()).isFalse().as("This file must be cleaned");
		// subfolder is managed as a whole
		Assertions.assertThat(dir1.exists()).isFalse().as("This file must be cleaned");
		Assertions.assertThat(file3.exists()).isFalse().as("This file must be cleaned");
	}

	/**
	 * Clean by last modification time, with a file usecase
	 */
	@Test
	public void cleanDirectoryDate() throws IOException, InterruptedException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		Assertions.assertThat(file1.createNewFile()).isTrue();
		
		Date date = waitSomeTime();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		FileUtils.cleanDirectory(subFolder, date);
		
		Assertions.assertThat(folder.getRoot().exists()).isTrue().as("Parent folder must be kept");
		Assertions.assertThat(subFolder.exists()).isTrue().as("Cleaned folder must be kept");
		Assertions.assertThat(file1.exists()).isFalse().as("This file must be cleaned");
		Assertions.assertThat(file2.exists()).isTrue();
	}

	/**
	 * Clean by last modification time, with a directory usecase
	 */
	@Test
	public void cleanDirectoryDateWithDirectory() throws IOException, InterruptedException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		File dir1 = new File(subFolder, "dir1");
		File file3 = new File(dir1, "file3");
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		Date date = waitSomeTime();
		Assertions.assertThat(dir1.mkdirs()).isTrue();
		Assertions.assertThat(file3.mkdirs()).isTrue();
		
		FileUtils.cleanDirectory(subFolder, date);
		
		Assertions.assertThat(folder.getRoot().exists()).isTrue().as("Parent folder must be kept");
		Assertions.assertThat(subFolder.exists()).isTrue().as("Cleaned folder must be kept");
		Assertions.assertThat(file1.exists()).isFalse().as("This file must be cleaned");
		Assertions.assertThat(file2.exists()).isFalse().as("This file must be cleaned");
		// subfolder is managed as a whole
		Assertions.assertThat(dir1.exists()).isTrue().as("This directory is recent and must be kept");
		Assertions.assertThat(file3.exists()).isTrue().as("This file is in a recent folder");
	}

	/**
	 * Check we use last modification time and not creation time
	 */
	@Test
	public void cleanDirectoryLastModified() throws IOException, InterruptedException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		Date date = waitSomeTime();
		// update file1 lastModification
		try (FileOutputStream fos = new FileOutputStream(file1)) {
			fos.write(1000);
		}
		
		FileUtils.cleanDirectory(subFolder, date);
		
		Assertions.assertThat(folder.getRoot().exists()).isTrue().as("Parent folder must be kept").as("Parent folder must be kept");
		Assertions.assertThat(subFolder.exists()).isTrue().as("Cleaned folder must be kept");
		Assertions.assertThat(file1.exists()).isTrue().as("This file was modified after provided date and must be kept");
		Assertions.assertThat(file2.exists()).isFalse().as("This file was created before provided date and must be deleted");
	}

	/**
	 * Check behavior for read directory error
	 */
	@Test
	public void cleanDirectoryReadError() throws IOException, InterruptedException {
		File file = Mockito.mock(File.class);
		Mockito.when(file.listFiles()).thenReturn(null);
		Mockito.when(file.exists()).thenReturn(true);
		Mockito.when(file.isDirectory()).thenReturn(true);
		Assertions.assertThatCode(() -> FileUtils.cleanDirectory(file, null)).isInstanceOf(IOException.class);
	}

	/**
	 * Check behavior if clean directory cannot be done because a deletion fails
	 */
	@Test
	public void cleanDirectoryFailing() throws IOException, InterruptedException {
		Assumptions.assumeThat(System.getenv().getOrDefault("CI_RUNNER_TAGS", "")).doesNotContain("docker");
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		File dir1 = new File(subFolder, "dir1");
		File file3 = new File(dir1, "file3");
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(dir1.mkdirs()).isTrue();
		Assertions.assertThat(file3.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		dir1.setWritable(false);
		
		Assertions.assertThatCode(() -> FileUtils.cleanDirectory(subFolder, null)).isInstanceOf(IOException.class);
		
		Assertions.assertThat(folder.getRoot().exists()).isTrue().as("Parent folder must be kept");
		Assertions.assertThat(subFolder.exists()).isTrue().as("Cleaned folder must be kept");
		Assertions.assertThat(file1.exists()).isFalse().as("This file must be cleaned");
		Assertions.assertThat(file2.exists()).isFalse().as("This file must be cleaned");
		Assertions.assertThat(dir1.exists()).isTrue().as("Protected folder cannot be deleted as content cannot be removed");
		Assertions.assertThat(file3.exists()).isTrue().as("File cannot be removed as folder is write-protected");
	}

	/**
	 * Behavior for null directory
	 */
	@Test
	public void listFilesNull() {
		Assertions.assertThatCode(() -> FileUtils.listFiles(null, null)).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * Behavior for null directory
	 */
	@Test
	public void listNull() {
		Assertions.assertThatCode(() -> FileUtils.list(null, null)).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * List files without a {@link FileFilter}
	 */
	@Test
	public void listFiles() throws IOException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		File dir1 = new File(subFolder, "dir1");
		File file3 = new File(dir1, "file3");
		
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(dir1.mkdirs()).isTrue();
		Assertions.assertThat(file3.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		List<File> files = FileUtils.listFiles(subFolder, null);
		Assertions.assertThat(files).hasSize(3);
		Assertions.assertThat(files).containsSequence(dir1, file1, file2);
	}

	/**
	 * Behavior when listing null directory
	 */
	@Test
	public void listRecursivelyNull() throws IOException {
		Assertions.assertThatCode(() -> FileUtils.listRecursively(null,
				new DelegateFileFilter(f -> true), // stub
				new DelegateFileFilter(f -> true)) // stub
		).isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * List directory with a {@link FilenameFilter}
	 */
	@Test
	public void listFilter() throws IOException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		List<File> files = FileUtils.list(subFolder, (dir, fName) -> fName.equals(file1.getName()));
		Assertions.assertThat(files).hasSize(1);
		Assertions.assertThat(files).containsSequence(file1);
	}

	/**
	 * List directory with a {@link FileFilter}
	 */
	@Test
	public void listFilesFilter() throws IOException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		
		List<File> files = FileUtils.listFiles(subFolder, (f) -> f.getName().equals(file1.getName()));
		Assertions.assertThat(files).hasSize(1);
		Assertions.assertThat(files).containsSequence(file1);
	}

	/**
	 * Behavior for a read directory error
	 */
	@Test
	public void listFilesReadError() throws IOException {
		File file = Mockito.mock(File.class);
		Mockito.when(file.listFiles()).thenReturn(null);
		Assertions.assertThatCode(() -> FileUtils.listFiles(file, null)).isInstanceOf(IllegalStateException.class);
	}

	/**
	 * List recursively, with a recurse behavior and a results {@link IOFileFilter}
	 */
	@Test
	public void listRecursively() throws IOException {
		File subFolder = folder.newFolder("directory");
		File file1 = new File(subFolder, "file1");
		File file2 = new File(subFolder, "file2");
		File dir1 = new File(subFolder, "dir1");
		File file3 = new File(dir1, "file3");
		File dir2 = new File(subFolder, "dir2");
		File file4 = new File(dir2, "file4");
		
		Assertions.assertThat(file1.createNewFile()).isTrue();
		Assertions.assertThat(dir1.mkdirs()).isTrue();
		Assertions.assertThat(dir2.mkdirs()).isTrue();
		Assertions.assertThat(file3.createNewFile()).isTrue();
		Assertions.assertThat(file2.createNewFile()).isTrue();
		Assertions.assertThat(file4.createNewFile()).isTrue();
		
		Collection<File> files = FileUtils.listRecursively(subFolder,
				FileFileFilter.FILE, // list only files
				new DelegateFileFilter(f -> f.getName().equals(dir1.getName())) // recurse only in dir1
		);
		Assertions.assertThat(files).hasSize(3);
		Assertions.assertThat(files).containsSequence(file1, file2, file3);
	}

	private Date waitSomeTime() throws InterruptedException {
		Date date = new Date();
		// under 1000ms, delta-time is not high enough to ensure that file2 last modification date is late enough
		Thread.sleep(1000);
		return date;
	}
}
