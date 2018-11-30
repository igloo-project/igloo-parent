package org.iglooproject.commons.io.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

public class ClassPathResourceHelper {

	public long lastModifiedFileResource(URL url) {
		File file = new File(url.getPath());
		return file.lastModified();
	}

	public long lastModifiedJarResource(URL url) throws IOException {
		JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
		return new File((jarURLConnection).getJarFile().getName()).lastModified();
	}

	public URL resolveUrl(ClassLoader classLoader, String resourcePath) throws FileNotFoundException {
		URL resource = classLoader.getResource(resourcePath);
		if (resource == null) {
			throw new FileNotFoundException(String.format("Resource %s not found", resourcePath));
		} else {
			return resource;
		}
	}

	public InputStream openStream(URL url) throws IOException {
		return url.openStream();
	}

	public URL toUrl(ClassLoader classLoader, String classpathUrl) throws FileNotFoundException {
		return resolveUrl(classLoader, cleanClasspathUrl(classpathUrl));
	}

	public String cleanClasspathUrl(String classpathUrl) {
		if (!classpathUrl.startsWith("classpath:")) {
			throw new IllegalArgumentException(String.format("URL format exception: %s does not start with 'classpath:'", classpathUrl));
		}
		return classpathUrl.replaceAll("^classpath:/?", "");
	}

}
