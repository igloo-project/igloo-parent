package fr.openwide.core.wicket.more.lesscss;

import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import fr.openwide.core.spring.util.StringUtils;

public class LessCssUtil {

	public static final Pattern LESSCSS_IMPORT_PATTERN =
		Pattern.compile("^\\p{Blank}*@import\\p{Blank}+\"([^\"]+.less)\";\\p{Blank}*$", Pattern.MULTILINE);

	public static String getRelativeToScopePath(String sourceFile, String importFilename) {
		String contextPath = FilenameUtils.getFullPath(sourceFile);
		String relativeToScopeFilename;
		if (StringUtils.hasLength(contextPath)) {
			relativeToScopeFilename = FilenameUtils.concat(contextPath, importFilename);
		} else {
			relativeToScopeFilename = importFilename;
		}
		
		return relativeToScopeFilename;
	}

}
