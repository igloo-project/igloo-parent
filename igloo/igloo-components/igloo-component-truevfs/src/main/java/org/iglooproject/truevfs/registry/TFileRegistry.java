package org.iglooproject.truevfs.registry;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TVFS;
import net.java.truevfs.kernel.spec.FsSyncException;
import net.java.truevfs.kernel.spec.FsSyncOptions;

/**
 * A registry responsible for keeping track of open {@link TFile TFiles} and for cleaning them up upon closing it.
 * <p>This should generally be used as a factory for creating TFiles (see the <code>create</code> methods), and the
 * calling of {@link #open()} and {@link #close()} should be done in some generic code (such as a servlet filter).
 * <p>Please note that this registry is using a {@link ThreadLocal}. Opening and closing the registry must therefore
 * be done in the same thread, and each TFile-creating thread should use its own registry.
 */
public final class TFileRegistry implements AutoCloseable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TFileRegistry.class);
	
	private static final ThreadLocal<TFileRegistry> THREAD_LOCAL = new ThreadLocal<>();

	private final Set<TFile> registeredFiles = new HashSet<>();
	
	protected TFileRegistry() { }
	
	/**
	 * Enables the (thread-local) TFileRegistry, so that every call to the createXX() or register() static methods
	 * will result in the relevant TFile to be stored for further cleaning of TrueZip internal resources.
	 * <p>The actual cleaning must be performed by calling the {@link #close()} static method on TFileRegistry.
	 */
	public static TFileRegistry open() {
		if (THREAD_LOCAL.get() == null) {
			TFileRegistry registry = new TFileRegistry();
			THREAD_LOCAL.set(registry);
			return registry;
		} else {
			throw new IllegalStateException("TFileRegistry.open() should not be called twice without calling close() in-between.");
		}
	}
	
	public TFile create(String path) {
		TFile tFile = new TFile(path);
		register(tFile);
		return tFile;
	}
	
	public TFile create(File file) {
		TFile tFile = new TFile(file);
		register(tFile);
		return tFile;
	}
	
	public TFile create(String parent, String member) {
		TFile tFile = new TFile(parent, member);
		register(tFile);
		return tFile;
	}
	
	public TFile create(File parent, String member) {
		TFile tFile = new TFile(parent, member);
		register(tFile);
		return tFile;
	}
	
	public TFile create(URI uri) {
		TFile tFile = new TFile(uri);
		register(tFile);
		return tFile;
	}

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
				LOGGER.error("Error while trying to sync the trueVFS filesystem on '" + tFile + "'", e);
			}
		}
		if (this.equals(THREAD_LOCAL.get())) {
			THREAD_LOCAL.remove();
		} else {
			LOGGER.error("Error closing TFileRegistry; thread registered object doest not match expected one.");
		}
	}
}
