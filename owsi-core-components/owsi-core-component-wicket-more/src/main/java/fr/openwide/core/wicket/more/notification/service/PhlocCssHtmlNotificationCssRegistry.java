package fr.openwide.core.wicket.more.notification.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.ComponentTag;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.phloc.css.CCSS;
import com.phloc.css.ECSSVersion;
import com.phloc.css.ICSSWriterSettings;
import com.phloc.css.decl.CSSDeclaration;
import com.phloc.css.decl.CSSSelector;
import com.phloc.css.decl.CSSStyleRule;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.writer.CSSWriterSettings;

import fr.openwide.core.commons.util.ordering.AbstractNullSafeComparator;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;
import fr.openwide.core.wicket.more.notification.service.impl.PhlocCssMatchableComponentTag;

public class PhlocCssHtmlNotificationCssRegistry implements IHtmlNotificationCssRegistry {
	
	private static final ICSSWriterSettings STYLE_ATTRIBUTE_WRITER_SETTINGS;
	static {
		CSSWriterSettings settings = new CSSWriterSettings(ECSSVersion.CSS30, true);
		STYLE_ATTRIBUTE_WRITER_SETTINGS = settings;
	}
	
	private static final Comparator<CSSDeclaration> CSS_DECLARATION_PROPERTY_COMPARATOR = new AbstractNullSafeComparator<CSSDeclaration>() {
		private static final long serialVersionUID = 1L;
		@Override
		protected int compareNotNullObjects(CSSDeclaration left, CSSDeclaration right) {
			return left.getProperty().compareTo(right.getProperty());
		}
	};

	private final CascadingStyleSheet styleSheet;
	
	public PhlocCssHtmlNotificationCssRegistry(CascadingStyleSheet styleSheet) {
		super();
		Assert.notNull(styleSheet);
		this.styleSheet = styleSheet;
	}

	@Override
	public String getStyle(ComponentTag tag) {
		PhlocCssMatchableComponentTag matchableTag = new PhlocCssMatchableComponentTag(tag);
		Map<CSSStyleRule, Collection<CSSSelector>> matchedRules = getMatchingRules(matchableTag);
		Collection<CSSDeclaration> declarations = mergeRules(matchedRules);
		return buildString(declarations);
	}

	private Map<CSSStyleRule, Collection<CSSSelector>> getMatchingRules(PhlocCssMatchableComponentTag matchableTag) {
		Map<CSSStyleRule, Collection<CSSSelector>> matchedRules = Maps.newHashMap();
		for (CSSStyleRule rule : styleSheet.getAllStyleRules()) {
			Collection<CSSSelector> matchedSelectors = getMatchedSelectors(matchableTag, rule);
			if (!matchedSelectors.isEmpty()) {
				matchedRules.put(rule, matchedSelectors);
			}
		}
		return matchedRules;
	}

	private Collection<CSSSelector> getMatchedSelectors(PhlocCssMatchableComponentTag matchableTag, CSSStyleRule rule) {
		List<CSSSelector> matchedSelectors = Lists.newArrayList();
		for (CSSSelector selector : rule.getAllSelectors()) {
			if (matchableTag.matches(selector)) {
				matchedSelectors.add(selector);
			}
		}
		return matchedSelectors;
	}

	private Collection<CSSDeclaration> mergeRules(Map<CSSStyleRule, Collection<CSSSelector>> matchedRules) {
		Map<CSSDeclaration, Integer> mergedDeclarations = Maps.newTreeMap(CSS_DECLARATION_PROPERTY_COMPARATOR);
		
		for (Map.Entry<CSSStyleRule, Collection<CSSSelector>> entry : matchedRules.entrySet()) {
			int score = 0;
			// Compute max. score
			for (CSSSelector selector : entry.getValue()) {
				int selectorScore = computeScore(selector);
				if (selectorScore > score) {
					score = selectorScore;
				}
			}
			// Merge declarations if the score is high enough
			for (CSSDeclaration declaration : entry.getKey().getAllDeclarations()) {
				Integer currentScore = mergedDeclarations.get(declaration);
				if (currentScore == null || score >= currentScore) {
					mergedDeclarations.put(declaration, score);
				}
			}
		}
		return mergedDeclarations.keySet();
	}
	
	private int computeScore(CSSSelector selector) {
		// TODO compute scores for each selector, so that we only return one declaration per property when merging the rules
		// (e.g. not 2 'background-color' declarations)
		return 0;
	}

	private String buildString(Collection<CSSDeclaration> declarations) {
		StringBuilder builder = new StringBuilder();
		for (CSSDeclaration declaration : declarations) {
			builder
					.append(declaration.getAsCSSString(STYLE_ATTRIBUTE_WRITER_SETTINGS, 0))
					.append(CCSS.DEFINITION_END);
		}
		return builder.toString();
	}

}
