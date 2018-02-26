package org.iglooproject.sass.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class ClasspathUtil {

	public static long lastModified(ClassLoader classLoader, String classpathUrl) {
		try {
			URL url = toUrl(classLoader, classpathUrl);
			// if jar:file:/..., unwrap
			if (url.getProtocol().equals("jar")) {
				JarURLConnection conn = (JarURLConnection) url.openConnection();
				// prevent inputStream leak from getLastModified() call
				// https://www.mail-archive.com/wicket-user@lists.sourceforge.net/msg20937.html
				try (InputStream is = conn.getInputStream()) {
					return conn.getLastModified();
				}
			} else if (url.getProtocol().equals("file")) {
				File file = new File(url.getPath());
				if (!file.exists()) {
					throw new RuntimeException(String.format("file not found for %s (original url: %s)",
							url.getPath(), classpathUrl));
				}
				return file.lastModified();
			} else {
				throw new RuntimeException(String.format("protocol for %s not supported (original url: %s)",
						url, classpathUrl));
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("%s resource cannot be read", classpathUrl), e);
		}
	}

	public static String toString(ClassLoader classLoader, String classpathUrl) {
		try (InputStream is = toInputStream(classLoader, classpathUrl)) {
			return IOUtils.toString(is);
		} catch (IOException e) {
			throw new RuntimeException(String.format("%s resource cannot be read", classpathUrl), e);
		}
	}

	private static URL toUrl(ClassLoader classLoader, String classpathUrl) throws IOException {
		return classLoader.getResource(cleanClasspathUrl(classpathUrl));
	}

	private static InputStream toInputStream(ClassLoader classLoader, String classpathUrl) throws IOException {
		return classLoader.getResourceAsStream(cleanClasspathUrl(classpathUrl));
	}

	private static String cleanClasspathUrl(String classpathUrl) throws IOException {
		if (!classpathUrl.startsWith("classpath:")) {
			throw new IOException(String.format("URL format exception: %s does not start with 'classpath:'", classpathUrl));
		}
		return classpathUrl.replaceAll("^classpath:/+", "");
	}

}
