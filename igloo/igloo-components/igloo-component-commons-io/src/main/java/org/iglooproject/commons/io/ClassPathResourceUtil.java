package org.iglooproject.commons.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

/**
 * Some utilities to handle classpath:/ urls without Spring
 */
public class ClassPathResourceUtil {

	private final ClassLoader classLoader;

	public ClassPathResourceUtil() {
		this(ClassPathResourceUtil.class.getClassLoader());
	}

	public ClassPathResourceUtil(ClassLoader classLoader) {
		super();
		this.classLoader = classLoader;
	}

	/**
	 * Extract last modified date (as epoch) from a <em>classpath:/</em> url. For jar resources, returned date is
	 * computed from jar file.
	 * 
	 * @throws IOException if file cannot be read.
	 * @throws IllegalArgumentException if url is not well-formed.
	 */
	public long lastModified(String classpathUrl) throws IOException {
		try {
			URL url = toUrl(classLoader, classpathUrl);
			if (url.getProtocol().equals("jar")) {
				// if jar:file:/...
				JarURLConnection conn = (JarURLConnection) url.openConnection();
				// prevent inputStream leak from getLastModified() call
				// https://www.mail-archive.com/wicket-user@lists.sourceforge.net/msg20937.html
				try (InputStream is = conn.getInputStream()) {
					return conn.getLastModified();
				}
			} else if (url.getProtocol().equals("file")) {
				// if file:/...
				File file = new File(url.getPath());
				if (!file.exists()) {
					throw new FileNotFoundException(String.format("file not found for %s (original url: %s)",
							url.getPath(), classpathUrl));
				}
				return file.lastModified();
			} else {
				// other resources are not handled
				throw new IllegalArgumentException(String.format("protocol for %s not supported (original url: %s)",
						url, classpathUrl));
			}
		} catch (IOException e) {
			throw new IOException(String.format("%s resource cannot be read", classpathUrl), e);
		}
	}

	public String toStringAsUtf8(String classpathUrl) throws IOException {
		return toString(classpathUrl, Charsets.UTF_8.name());
	}

	/**
	 * Extract content of a classpath resource as String.
	 */
	public String toString(String classpathUrl, String encoding) throws IOException {
		try (InputStream is = toInputStream(classpathUrl)) {
			return IOUtils.toString(is, encoding);
		} catch (IOException e) {
			throw new IOException(String.format("%s resource cannot be read", classpathUrl), e);
		}
	}

	/**
	 * You need to close returned {@link InputStream} after usage.
	 */
	public InputStream toInputStream(String classpathUrl) throws IOException {
		InputStream is = classLoader.getResourceAsStream(cleanClasspathUrl(classpathUrl));
		if (is == null) {
			throw new FileNotFoundException(String.format("Content not found for %s", classpathUrl));
		}
		return is;
	}

	private static URL toUrl(ClassLoader classLoader, String classpathUrl) throws IOException {
		return classLoader.getResource(cleanClasspathUrl(classpathUrl));
	}

	private static String cleanClasspathUrl(String classpathUrl) throws IOException {
		if (!classpathUrl.startsWith("classpath:/")) {
			throw new IOException(String.format("URL format exception: %s does not start with 'classpath:/'", classpathUrl));
		}
		return classpathUrl.replaceAll("^classpath:/+", "");
	}

}
