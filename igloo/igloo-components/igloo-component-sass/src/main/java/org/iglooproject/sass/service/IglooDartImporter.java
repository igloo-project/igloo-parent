package org.iglooproject.sass.service;

import java.io.IOException;
import java.net.URL;

import de.larsgrefer.sass.embedded.importer.ClasspathImporter;
import de.larsgrefer.sass.embedded.importer.WebjarsImporter;

/**
 * Handle both webjars (<code>META-INF/resources/webjars/...</code>) and scoped (<code>$(scope-NAME)/path/file<code>)
 * URLs.
 */
public final class IglooDartImporter extends ClasspathImporter { //NOSONAR no interest to override equals/hashcode

	private final WebjarsImporter webjarImporter;
	private final IScopeResolver scopeResolver;

	public IglooDartImporter(WebjarsImporter webjarImporter, IScopeResolver scopeResolver) {
		this.webjarImporter = webjarImporter;
		this.scopeResolver = scopeResolver;
	}

	@Override
	public String canonicalize(String url, boolean fromImport) throws Exception {
		// handle file URLs
		if (url.startsWith("file:///")) {
			return super.canonicalize(url, fromImport);
		}
		// handle classpath URLs
		if (url.startsWith("jar:file:/") && url.contains("!")) {
			var tried = new URL(url);
			try {
				tried.openStream();
				return tried.toString();
			} catch (IOException e) {
				// not existing path must return null
			}
		}
		// handle webjars URL
		if (url.startsWith("META-INF/resources/webjars/")) {
			// will be handled by canonicalizeUrl
			return super.canonicalize(url, fromImport);
		}
		// handle $(scope-NAME)
		if (url.startsWith("$(scope-")) {
			return super.canonicalize(scopeResolver.resolveScope(url), fromImport);
		}
		return super.canonicalize(url, fromImport);
	}
	
	@Override
	public URL canonicalizeUrl(String url) throws IOException {
		if (url.startsWith("META-INF/resources/webjars/")) {
			return webjarImporter.canonicalizeUrl(url);
		}
		return super.canonicalizeUrl(url);
	}
}