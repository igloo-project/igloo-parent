package org.iglooproject.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.iglooproject.commons.io.internal.ClassPathResourceHelper;

/**
 * Some utilities to handle classpath:/ urls without Spring. Prefix may be <em>classpath:/</em> or <em>classpath:</em>.
 */
public class ClassPathResourceUtil {

	private final ClassLoader classLoader;

	/**
	 * Internal behavior splitted in an helper class.
	 */
	private final ClassPathResourceHelper helper;

	/**
	 * This default implementation uses {@link ClassPathResourceUtil} {@link ClassLoader} for resource lookup.
	 */
	public ClassPathResourceUtil() {
		this(null);
	}

	/**
	 * Use a custom {@link ClassLoader} for resource lookup. If you don't know if you need it, just use no-arg
	 * {@link ClassPathResourceUtil#ClassPathResourceUtil()} constructor.
	 */
	public ClassPathResourceUtil(ClassLoader classLoader) {
		this(classLoader, null);
	}

	/**
	 * This package-protected constructor allow to provide a custom-implementation {@link ClassPathResourceHelper}
	 * (internal API).
	 */
	ClassPathResourceUtil(ClassLoader classLoader, ClassPathResourceHelper helper) {
		super();
		this.classLoader = Optional.ofNullable(classLoader).orElse(ClassPathResourceUtil.class.getClassLoader());
		this.helper = Optional.ofNullable(helper).orElse(new ClassPathResourceHelper());
	}

	/**
	 * Extract last modified date (as epoch) from a <em>classpath:/</em> url. For jar resources, returned date is
	 * computed from jar file.
	 * 
	 * @throws IOException if file cannot be found or modified time read.
	 * @throws IllegalArgumentException if url is not well-formed.
	 */
	public long lastModified(String classpathUrl) throws IOException {
		try {
			URL url = helper.toUrl(classLoader, classpathUrl);
			return lastModified(classpathUrl, url);
		} catch (IOException e) {
			throw new IOException(String.format("%s resource cannot be read", classpathUrl), e);
		}
	}

	/**
	 * Extract text content from a resource. Content must be an utf-8 encoded text.
	 * 
	 * @see ClassPathResourceUtil
	 * @param classpathUrl <em>classpath:...</em> resource url.
	 * @return file content as a string
	 * @throws IOException if file cannot be found or read, or content cannot be read as a string.
	 */
	public String asUtf8String(String classpathUrl) throws IOException {
		return asString(classpathUrl, Charsets.UTF_8.name());
	}

	/**
	 * Extract text content from a resource. Content must be an text encoded with the provided encoding.
	 * 
	 * @see ClassPathResourceUtil
	 * @param classpathUrl <em>classpath:...</em> resource url
	 * @param encoding encoding of the targetted resource
	 * @return file content as a string
	 * @throws IOException if file cannot be found or read, or content cannot be read as a string.
	 */
	public String asString(String classpathUrl, String encoding) throws IOException {
		try (InputStream is = openStream(classpathUrl)) {
			return IOUtils.toString(is, encoding);
		} catch (IOException e) {
			throw new IOException(String.format("%s resource cannot be read", classpathUrl), e);
		}
	}

	/**
	 * Provide an {@link InputStream} for the provided <em>classpath:...</em> url. Closing the provided
	 * {@link InputStream} after usage is needed.
	 * 
	 * @see ClassPathResourceUtil
	 * @param classpathUrl <em>classpath:...</em> resource url
	 * @return an opened {@link InputStream}
	 * @throws IOException if file cannot be found or read, or content cannot be read as a string.
	 */
	public InputStream openStream(String classpathUrl) throws IOException {
		return helper.openStream(helper.toUrl(classLoader, classpathUrl));
	}

	private long lastModified(String classPathUrl, URL url) throws IOException {
		if (protocol(url).equals("jar")) {
			// if jar:/...
			return helper.lastModifiedJarResource(url);
		} else if (protocol(url).equals("file")) {
			// if file:/...
			return helper.lastModifiedFileResource(url);
		} else {
			// other resources are not handled
			throw new IllegalArgumentException(String.format("protocol for %s not supported (original url: %s)",
					url, classPathUrl));
		}
	}

	private String protocol(URL url) {
		return url.getProtocol();
	}

}
