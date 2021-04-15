package org.iglooproject.wicket.more.notification.service.impl;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSSelectorSimpleMember;
import com.helger.css.decl.ICSSSelectorMember;
import com.helger.css.writer.CSSWriterSettings;

@Deprecated
public class PhlocCssMatchableHtmlTag {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PhlocCssMatchableHtmlTag.class);
	
	private static final Splitter CSS_CLASSES_SPLITTER = Splitter.on(CharMatcher.whitespace()).omitEmptyStrings().trimResults();
	
	private final String name;
	private final String id;
	private final Collection<String> classes;
	
	public PhlocCssMatchableHtmlTag(String tagName, String tagId, String classAttribute) {
		name = StringUtils.defaultString(tagName);
		id = StringUtils.defaultString(tagId);
		classes = Sets.newHashSet(CSS_CLASSES_SPLITTER.splitToList(StringUtils.defaultString(classAttribute)));
	}
	
	public boolean matches(CSSSelectorSimpleMember simpleMember) {
		if (simpleMember.isClass()) {
			return classes.contains(simpleMember.getValue().substring(1)); // Remove leading '.'
		} else if (simpleMember.isElementName()) {
			return name.equals(simpleMember.getValue());
		} else if (simpleMember.isHash()) {
			return id.equals(simpleMember.getValue().substring(1)); // Remove leading '#'
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public boolean matches(CSSSelector selector) {
		for (ICSSSelectorMember member : selector.getAllMembers()) {
			if (member instanceof CSSSelectorSimpleMember && !((CSSSelectorSimpleMember) member).isPseudo()) {
				CSSSelectorSimpleMember simpleMember = (CSSSelectorSimpleMember) member;
				if (!matches(simpleMember)) {
					return false;
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Only simple selector members ('.class', 'name', '#id') are supported. The selector '{}' and the related declarations will be ignored.",
							selector.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
				}
				return false;
			}
		}
		
		return true;
	}

}
