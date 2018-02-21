package org.iglooproject.wicket.more.css.scss.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.iglooproject.wicket.more.css.scss.util.JSassWebjarUrlMatcher;
import org.iglooproject.wicket.more.css.scss.util.JSassWebjarUrlMatcher.WebjarUrl;
import org.springframework.core.io.ClassPathResource;
import org.webjars.WebJarAssetLocator;

import com.google.common.collect.Lists;

import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;

public class JSassClassPathImporter implements Importer {

	private static final Pattern SCSS_IMPORT_SCOPE_PATTERN = Pattern.compile("^\\$\\(scope-([a-zA-Z0-9_-]*)\\)(.*)$");

	private static final JSassWebjarUrlMatcher MATCHER = JSassWebjarUrlMatcher.INSTANCE;

	private final Map<String, Class<?>> scopes;
	
	private List<String> sourceUris = Lists.newArrayList();

	private WebJarAssetLocator webjarLocator = new WebJarAssetLocator();

	public JSassClassPathImporter(Map<String, Class<?>> scopes) {
		this.scopes = scopes;
	}

	@Override
	public Collection<Import> apply(String url, Import previous) {
		return Collections.singletonList(getInputSource(url, previous.getAbsoluteUri()));
	}
	
	public Import getInputSource(String url, final URI previousBase) {
		try {
			Matcher scopeMatcher = SCSS_IMPORT_SCOPE_PATTERN.matcher(url);
			if (scopeMatcher.matches()) {
				// translate scope to a classpath url
				Class<?> referencedScope = scopes.get(scopeMatcher.group(1));
				if (referencedScope == null) {
					throw new IllegalStateException(String.format("Scope %1$s is not supported", scopeMatcher.group(1)));
				}
				url = "classpath:/" + referencedScope.getPackage().getName().replace(".", "/") + "/" + scopeMatcher.group(2);
			} else if (MATCHER.test(url)) {
				WebjarUrl webjarUrl = MATCHER.match(url);
				// "/<version>" or ""
				String slashVersionOrEmpty = Optional.<String>ofNullable(webjarUrl.getVersion()).map("/"::concat).orElse("");
				// "/<version>/path"
				String resourcePathWithVersion = new StringBuilder()
						.append(slashVersionOrEmpty)
						.append(webjarUrl.getResourcePath()).toString();
				url = new StringBuilder()
						.append("classpath:/")
						// META-INF/resources/webjars/<webjar>/<version>/path
						.append(webjarLocator.getFullPath(webjarUrl.getWebjar(), resourcePathWithVersion))
						.toString();
			} else if (url.startsWith("/")) {
				// we try to circumvent any forbidden resource loading
				throw new IllegalArgumentException(String.format("Absolute url %s is not allowed for @import", url));
			} else if (url.startsWith("../../../")) {
				throw new IllegalArgumentException(String.format("More than 3 level relative loading of %s is not allowed for @import", url));
			} else if (!url.startsWith("../") && url.contains("../")) {
				throw new IllegalArgumentException(String.format("More than 3 level relative loading of %s is not allowed for @import", url));
			}
			// if scope or webjar-handled, url is now classpath:/...
			// else, it is a relative path
			
			url = url.replaceFirst(".scss$", "");
			URI base = previousBase;
			if (url.contains("/")) {
				// if url is absolute: classpath:/path/file/
				// base=classpath:/path
				// url=file
				
				// if url is relative: ../file
				// previousBase=path1/path2
				// base=path1/
				// url=file
				base = base.resolve(url.substring(0, url.lastIndexOf("/") + 1));
				url = url.substring(url.lastIndexOf("/") + 1);
			}
			
			// try with and without _ prefix
			List<URI> potentialIdentifiers = Lists.newArrayList(
					new URI("_" + url + ".scss"),
					new URI(url + ".scss")
			);
			
			for (URI potentialIdentifier : potentialIdentifiers) {
				URI potentialUri = base.resolve(potentialIdentifier);
				
				String classpathUri = potentialUri.toString().replaceFirst("^classpath:/", "");
				ClassPathResource cpr = new ClassPathResource(classpathUri);
				try {
					String source = IOUtils.toString(cpr.getInputStream());
					addSourceUri(classpathUri);
					return new Import(potentialIdentifier, potentialUri, source);
				} catch (IOException e) {
				}
			}
			
			throw new IllegalArgumentException(String.format("File \"%1$s\" not imported because it could not be found", base.resolve(url)));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(String.format("Invalid URI syntax for \"%1$s\" from path \"%2$s\"", url, previousBase));
		}
	}

	public List<String> getSourceUris() {
		return sourceUris;
	}

	public void addSourceUri(String sourceUri) {
		this.sourceUris.add(sourceUri);
	}

}