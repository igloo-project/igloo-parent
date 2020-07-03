package org.iglooproject.truezip.registry;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TVFS;
import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.fs.FsSyncOptions;

/**
 * A registry responsible for keeping track of open {@link TFile TFiles} and for cleaning them up upon closing it.
 * <p>This should generally be used as a factory for creating TFiles (see the <code>create</code> methods), and the
 * calling of {@link #open()} and {@link #close()} should be done in some generic code (such as a servlet filter).
 * <p>Please note that this registry is using a {@link ThreadLocal}. Opening and closing the registry must therefore
 * be done in the same thread, and each TFile-creating thread should use its own registry.
 */
public final class TFileRegistry {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TFileRegistry.class);
	
	private static final ThreadLocal<TFileRegistryImpl> THREAD_LOCAL = new ThreadLocal<>();
	
	private TFileRegistry() { }
	
	/**
	 * Enables the (thread-local) TFileRegistry, so that every call to the createXX() or register() static methods
	 * will result in the relevant TFile to be stored for further cleaning of TrueZip internal resources.
	 * <p>The actual cleaning must be performed by calling the {@link #close()} static method on TFileRegistry.
	 */
	public static void open() {
		TFileRegistryImpl registry = THREAD_LOCAL.get();
		if (registry == null) {
			THREAD_LOCAL.set(new TFileRegistryImpl());
		} else {
			throw new IllegalStateException("TFileRegistry.open() should not be called twice without calling close() in-between.");
		}
	}
	
	/**
	 * {@link TVFS#sync(de.schlichtherle.truezip.fs.FsMountPoint, de.schlichtherle.truezip.util.BitField) Synchronize}
	 * the TrueZip virtual filesystem for every registered file, and clears the registry.
	 * <p><strong>WARNING :</strong> If some {@link InputStream InputStreams} or {@link OutputStream OutputStreams}
	 * managed by the current thread have not been closed yet, they will be ignored.
	 */
	public static void close() {
		try {
			TFileRegistryImpl registry = THREAD_LOCAL.get();
			if (registry != null) {
				registry.close();
			} else {
				throw new IllegalStateException("TFileRegistry.close() should not be called if TFileRegistry.open() has not been called before.");
			}
		} finally {
			THREAD_LOCAL.remove();
		}
	}
	
	public static TFile create(String path) {
		TFile tFile = new TFile(path);
		register(tFile);
		return tFile;
	}
	
	public static TFile create(File file) {
		TFile tFile = new TFile(file);
		register(tFile);
		return tFile;
	}
	
	public static TFile create(String parent, String member) {
		TFile tFile = new TFile(parent, member);
		register(tFile);
		return tFile;
	}
	
	public static TFile create(File parent, String member) {
		TFile tFile = new TFile(parent, member);
		register(tFile);
		return tFile;
	}
	
	public static TFile create(URI uri) {
		TFile tFile = new TFile(uri);
		register(tFile);
		return tFile;
	}

	public static void register(File file) {
		Objects.requireNonNull(file, "file must not be null");
		TFileRegistryImpl registry = THREAD_LOCAL.get();
		if (registry != null) {
			registry.register(file);
		} else {
			LOGGER.info("Trying to register file '{}', but the TFileRegistry has not been open (see TFileRegistry.open()). Ignoring registration.", file);
			THREAD_LOCAL.remove();
		}
	}

	public static void register(Iterable<? extends File> files) {
		Objects.requireNonNull(files, "files must not be null");
		TFileRegistryImpl registry = THREAD_LOCAL.get();
		if (registry != null) {
			registry.register(files);
		} else {
			LOGGER.info("Trying to register files '{}', but the TFileRegistry has not been open (see TFileRegistry.open()). Ignoring registration.", files);
			THREAD_LOCAL.remove();
		}
	}
	
	private static final class TFileRegistryImpl implements Closeable {
		private final Set<TFile> registeredFiles = new HashSet<>();
		
		private TFileRegistryImpl() { }
	
		public void register(File file) {
			if (file instanceof TFile) {
				TFile topLevelArchive = ((TFile)file).getTopLevelArchive();
				if (topLevelArchive != null) {
					registeredFiles.add(topLevelArchive);
				}
			}
		}
	
		public void register(Iterable<? extends File> files) {
			for (File file : files) {
				register(file);
			}
		}
		
		@Override
		public void close() {
			for (TFile tFile : registeredFiles) {
				try {
					TVFS.sync(tFile, FsSyncOptions.SYNC);
				} catch (RuntimeException | FsSyncException e) {
					LOGGER.error("Error while trying to sync the truezip filesystem on '" + tFile + "'", e);
				}
			}
		}
	}
}
