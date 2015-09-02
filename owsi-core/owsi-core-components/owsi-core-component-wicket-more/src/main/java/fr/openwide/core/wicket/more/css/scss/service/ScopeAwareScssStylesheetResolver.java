package fr.openwide.core.wicket.more.css.scss.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.vaadin.sass.internal.ScssStylesheet;
import com.vaadin.sass.internal.resolver.ClassloaderResolver;
import com.vaadin.sass.internal.resolver.ScssStylesheetResolver;

public class ScopeAwareScssStylesheetResolver extends ClassloaderResolver implements ScssStylesheetResolver {

	private static final long serialVersionUID = -7943089410176589314L;
	
	private static final Pattern LESSCSS_IMPORT_SCOPE_PATTERN = Pattern.compile("^\\$\\{scope-([a-zA-Z0-9_-]*)\\}(.*)$");
	
	private Map<String, Class<?>> scopes;
	
	public ScopeAwareScssStylesheetResolver(Map<String, Class<?>> scopes) {
		this.scopes = scopes;
	}
	
	@Override
	protected List<String> getPotentialParentPaths(ScssStylesheet parentStylesheet, String identifier) {
		List<String> potentialParents = Lists.newArrayListWithCapacity(1);
		
		Matcher scopeMatcher = LESSCSS_IMPORT_SCOPE_PATTERN.matcher(identifier);
		if (scopeMatcher.matches()) {
			Class<?> referencedScope = scopes.get(scopeMatcher.group(1));
			if (referencedScope == null) {
				throw new IllegalStateException(String.format("Scope %1$s is not supported", scopeMatcher.group(1)));
			}
			potentialParents.add(referencedScope.getPackage().getName().replace(".", "/") + "/" + extractFullPath("", scopeMatcher.group(2)));
			return potentialParents;
		} else {
			if (parentStylesheet != null) {
				potentialParents.add(extractFullPath(parentStylesheet.getDirectory(), identifier));
			}
		}
		return potentialParents;
	}

}
