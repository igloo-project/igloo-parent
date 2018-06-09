package org.iglooproject.sass.jsass;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.iglooproject.commons.io.ClassPathResourceUtil;
import org.iglooproject.sass.util.JSassWebjarUrlMatcher;
import org.iglooproject.sass.util.JSassWebjarUrlMatcher.WebjarUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.WebJarAssetLocator;

import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;

public class JSassClassPathImporter implements Importer {

	private static final Pattern SCSS_IMPORT_SCOPE_PATTERN = Pattern.compile("^\\$\\(scope-([a-zA-Z0-9_-]*)\\)(.*)$");

	private static final Logger LOGGER = LoggerFactory.getLogger(JSassClassPathImporter.class);

	private static final JSassWebjarUrlMatcher MATCHER = JSassWebjarUrlMatcher.INSTANCE;

	private final Map<String, Class<?>> scopes;
	
	private final List<String> sourceUris = new ArrayList<>();

	private final WebJarAssetLocator webjarLocator = new WebJarAssetLocator();

	private final ClassPathResourceUtil classPathResourceUtil = new ClassPathResourceUtil();

	public JSassClassPathImporter(Map<String, Class<?>> scopes) {
		this.scopes = scopes;
	}

	@Override
	public Collection<Import> apply(String url, Import previous) {
		return Collections.singletonList(getInputSource(url, previous.getAbsoluteUri()));
	}
	
	public Import getInputSource(String url, final URI previousBase) {
		url = resolveScope(url);
		
		try {
			final String basePath = FilenameUtils.getPath(url);
			final List<String> filenameCandidates = listCandidateFilenames(url);
			final List<String> paths = listCandidatePaths(basePath, filenameCandidates);
			
			// we try to circumvent any forbidden resource loading
			if (url.startsWith("/")) {
				throw new IllegalArgumentException(String.format("Absolute url %s is not allowed for @import", url));
			} else if (url.startsWith("../../../")) {
				throw new IllegalArgumentException(String.format("More than 3 level relative loading of %s is not allowed for @import", url));
			} else if (!url.startsWith("../") && url.contains("../")) {
				throw new IllegalArgumentException(String.format("More than 3 level relative loading of %s is not allowed for @import", url));
			}
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Trying paths {} for resource {} (base: {})",
						paths.stream().collect(Collectors.joining(", ")), url, previousBase);
			}
			for (String path : paths) {
				Import resource = resolveCandidate(path, previousBase);
				if (resource != null) {
					LOGGER.info("Resource {} resolved as {} (base: {})", url, path, previousBase);
					return resource;
				}
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Candidate resource {} from {} url not found (base: {})", path, url, previousBase);
				}
			}
			
			throw new IllegalArgumentException(
					String.format("File '%s' not imported because it could not be found (candidates: %s)",
							url, paths.stream().collect(Collectors.joining(",")))
			);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(String.format("Invalid URI syntax for '%s' from path '%s'", url, previousBase));
		}
	}

	/**
	 * Resolve $(scope-*) in url.
	 * 
	 * @throws IllegalArgumentException if a scope is present but not found
	 */
	private String resolveScope(String url) {
		Matcher scopeMatcher = SCSS_IMPORT_SCOPE_PATTERN.matcher(url);
		if (scopeMatcher.matches()) {
			// translate scope to a classpath:/ path
			Class<?> referencedScope = scopes.get(scopeMatcher.group(1));
			if (referencedScope == null) {
				throw new IllegalStateException(String.format("Scope %1$s is not supported", scopeMatcher.group(1)));
			}
			url = "classpath:/" + referencedScope.getPackage().getName().replace(".", "/") + "/" + scopeMatcher.group(2);
		}
		return url;
	}

	/**
	 * Try to load a candidate. Return null if provided path cannot be loaded.
	 * 
	 * @throws URISyntaxException if path segment cannot be transformed in {@link URI}
	 */
	private Import resolveCandidate(String path, final URI previousBase) throws URISyntaxException {
		// translate potentiel webjars:// path in a classpath path
		if (MATCHER.test(path)) {
			WebjarUrl webjarUrl = MATCHER.match(path);
			// "/<version>" or ""
			// "current" magic version is stripped out (for compatibility with wicket-webjars) 
			String slashVersionOrEmpty = Optional.<String>ofNullable(webjarUrl.getVersion())
					.filter((s) -> ! "current".equals(s)) // filter out magic "current" version
					.map("/"::concat) // prepend '/'
					.orElse("");
			// "/<version>/path"
			String resourcePathWithVersion = new StringBuilder()
					.append(slashVersionOrEmpty)
					.append(webjarUrl.getResourcePath()).toString();
			try {
				path = new StringBuilder()
						.append("classpath:/")
						// META-INF/resources/webjars/<webjar>/<version>/path
						.append(webjarLocator.getFullPath(webjarUrl.getWebjar(), resourcePathWithVersion))
						.toString();
			} catch (IllegalArgumentException e) {
				// resource not resolved in webjars
				return null;
			}
		}
		// if webjar-handled, path is now classpath:/...
		// else, it is a relative path
		
		// note that with webjars, resource availability is already tested, but for other case, path may be a non
		// existing resource
		
		// split path:
		// 1. absolute path part
		URI base;
		// 2. filename part
		String filename;
		if (path.contains("/")) {
			// if path is absolute (i.e. classpath:/path/file/)
			// base=classpath:/path
			// path=file
			
			// if path is relative (i.e. ../file)
			// previousBase=path1/path2
			// base=path1/
			// path=file
			base = previousBase.resolve(path.substring(0, path.lastIndexOf("/") + 1));
			filename = path.substring(path.lastIndexOf("/") + 1);
		} else {
			// if url is just a filename, only determine absolute base from previous base
			base = previousBase;
			filename = path;
		}
		
		URI potentialIdendifier = new URI(filename);
		URI potentialUri = base.resolve(potentialIdendifier);
		
		try {
			String source = classPathResourceUtil.asUtf8String(potentialUri.toString());
			addSourceUri(potentialUri.toString());
			return new Import(potentialIdendifier, potentialUri, source);
		} catch (IOException e) {
			LOGGER.debug("error reading {}", potentialUri);
			return null;
		}
	}

	/**
	 * From <url>/filename
	 * 
	 * <ul>
	 *   <li>filename: [_filename.scss, _filename.sass, filename.scss, filename.sass]</li>
	 *   <li>filename.scss: [_filename.scss, filename.scss]</li>
	 *   <li>filename.css: {@link IllegalArgumentException}</li>
	 * </ul>
	 */
	private List<String> listCandidateFilenames(String url) {
		List<String> filenameCandidates = new ArrayList<>();
		String filename = FilenameUtils.getName(url);
		String extension = FilenameUtils.getExtension(filename);
		
		if (StringUtils.isNotBlank(extension) && !"sass".equals(extension) && !"scss".equals(extension)) {
			throw new IllegalArgumentException(String.format("Explicit extension %s in %s is not allowed", extension, url));
		}
		
		// first try partial file
		if (StringUtils.isNotBlank(extension)) {
			filenameCandidates.add("_" + filename);
		} else {
			filenameCandidates.add("_" + filename + ".scss");
			filenameCandidates.add("_" + filename + ".sass");
		}
		if (StringUtils.isNotBlank(extension)) {
			filenameCandidates.add(filename);
		} else {
			filenameCandidates.add(filename + ".scss");
			filenameCandidates.add("_" + filename + ".sass");
		}
		
		return filenameCandidates;
	}

	/**
	 * Combine a base path with a list of filenames.
	 * 
	 * @param path base path to apply to all filenames
	 * @param candidateFilenames filenames to combine with path; order is preserved
	 * @return list of combined base path with candidate filenames
	 */
	private List<String> listCandidatePaths(String path, List<String> candidateFilenames) {
		return candidateFilenames.stream().map(path::concat).collect(Collectors.toList());
	}

	public List<String> getSourceUris() {
		return sourceUris;
	}

	public void addSourceUri(String sourceUri) {
		this.sourceUris.add(sourceUri);
	}

}