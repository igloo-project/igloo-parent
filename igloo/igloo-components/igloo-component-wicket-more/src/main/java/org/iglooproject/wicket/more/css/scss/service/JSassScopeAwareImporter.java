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

public class JSassScopeAwareImporter implements Importer {

	private static final Pattern SCSS_IMPORT_SCOPE_PATTERN = Pattern.compile("^\\$\\(scope-([a-zA-Z0-9_-]*)\\)(.*)$");

	private static final JSassWebjarUrlMatcher MATCHER = JSassWebjarUrlMatcher.INSTANCE;

	private final Map<String, Class<?>> scopes;
	
	private List<String> sourceUris = Lists.newArrayList();

	private WebJarAssetLocator webjarLocator = new WebJarAssetLocator();

	public JSassScopeAwareImporter(Map<String, Class<?>> scopes) {
		this.scopes = scopes;
	}

	@Override
	public Collection<Import> apply(String url, Import previous) {
		return Collections.singletonList(getInputSource(url, previous.getAbsoluteUri()));
	}
	
	public Import getInputSource(String url, URI base) {
		try {
			Matcher scopeMatcher = SCSS_IMPORT_SCOPE_PATTERN.matcher(url);
			if (scopeMatcher.matches()) {
				Class<?> referencedScope = scopes.get(scopeMatcher.group(1));
				if (referencedScope == null) {
					throw new IllegalStateException(String.format("Scope %1$s is not supported", scopeMatcher.group(1)));
				}
				url = "classpath:/" + referencedScope.getPackage().getName().replace(".", "/") + "/" + scopeMatcher.group(2);
			} else if (MATCHER.test(url)) {
				WebjarUrl webjarUrl = MATCHER.match(url);
				url = webjarLocator.getFullPath(webjarUrl.getProtocol(), String.format("%s%s",
						Optional.<String>of(webjarUrl.getVersion()).map("/"::concat).orElse(""),
						webjarUrl.getResourcePath()
				));
			}
			
			url = url.replaceFirst(".scss$", "");
			if (url.contains("/")) {
				base = base.resolve(url.substring(0, url.lastIndexOf("/") + 1));
				url = url.substring(url.lastIndexOf("/") + 1);
			}
		
			List<URI> potentialIdentifiers = Lists.newArrayList(
					new URI("_" + url + ".scss"),
					new URI(url + ".scss")
			);
			
			for (URI potentialIdentifier : potentialIdentifiers) {
				URI potentialUri = base.resolve(potentialIdentifier);
				
				String classpathUri = potentialUri.toString().replaceFirst("^classpath:/", "");
				ClassPathResource cpr = new ClassPathResource(classpathUri);
				try {
					addSourceUri(classpathUri);
					return new Import(potentialIdentifier, potentialUri, IOUtils.toString(cpr.getInputStream()));
				} catch (IOException e) {
				}
			}
			
			throw new IllegalArgumentException(String.format("File \"%1$s\" not imported because it could not be found", base.resolve(url)));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(String.format("Invalid URI syntax for \"%1$s\" from path \"%2$s\"", url, base));
		}
	}

	public List<String> getSourceUris() {
		return sourceUris;
	}

	public void addSourceUri(String sourceUri) {
		this.sourceUris.add(sourceUri);
	}

}