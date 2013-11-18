package fr.openwide.core.wicket.more.notification.service.impl;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.phloc.css.decl.CSSSelector;
import com.phloc.css.decl.CSSSelectorSimpleMember;
import com.phloc.css.decl.ICSSSelectorMember;

public class PhlocCssMatchableComponentTag {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PhlocCssMatchableComponentTag.class);
	
	private static final Splitter CSS_CLASSES_SPLITTER = Splitter.on(CharMatcher.WHITESPACE);
	
	private final String name;
	private final String id;
	private final Collection<String> classes;
	
	public PhlocCssMatchableComponentTag(ComponentTag tag) {
		name = StringUtils.defaultString(tag.getName());
		id = StringUtils.defaultString(tag.getId());
		classes = Sets.newHashSet(CSS_CLASSES_SPLITTER.splitToList(StringUtils.defaultString(tag.getAttribute("class"))));
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
				LOGGER.warn("Only simple selector members ('.class', 'name', '#id') are supported. The selector '{}' and the related declarations will be ignored.", selector);
				return false;
			}
		}
		
		return true;
	}

}
