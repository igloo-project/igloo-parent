package fr.openwide.core.wicket.more.notification.service;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.ComponentTag;
import org.springframework.util.Assert;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.phloc.css.CCSS;
import com.phloc.css.ECSSVersion;
import com.phloc.css.ICSSWriterSettings;
import com.phloc.css.decl.CSSDeclaration;
import com.phloc.css.decl.CSSSelector;
import com.phloc.css.decl.CSSSelectorSimpleMember;
import com.phloc.css.decl.CSSStyleRule;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.decl.ICSSSelectorMember;
import com.phloc.css.writer.CSSWriterSettings;

import fr.openwide.core.commons.util.ordering.SerializableCollator;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;
import fr.openwide.core.wicket.more.notification.service.impl.CssDeclarationPrecedence;
import fr.openwide.core.wicket.more.notification.service.impl.CssSelectorSpecificity;
import fr.openwide.core.wicket.more.notification.service.impl.PhlocCssMatchableComponentTag;

public class PhlocCssHtmlNotificationCssRegistry implements IHtmlNotificationCssRegistry {
	private static Comparator<String> CSS_PROPERTY_NAME_COLLATOR;
	static {
		SerializableCollator collator = new SerializableCollator(Locale.ROOT);
		collator.setStrength(Collator.SECONDARY);
		CSS_PROPERTY_NAME_COLLATOR = collator;
	}
	
	private static final ICSSWriterSettings STYLE_ATTRIBUTE_WRITER_SETTINGS;
	static {
		CSSWriterSettings settings = new CSSWriterSettings(ECSSVersion.CSS30, true);
		STYLE_ATTRIBUTE_WRITER_SETTINGS = settings;
	}

	private final CascadingStyleSheet styleSheet;
	
	public PhlocCssHtmlNotificationCssRegistry(CascadingStyleSheet styleSheet) {
		super();
		Assert.notNull(styleSheet);
		this.styleSheet = styleSheet;
	}

	@Override
	public String getStyle(ComponentTag tag) {
		PhlocCssMatchableComponentTag matchableTag = new PhlocCssMatchableComponentTag(tag);
		Map<CSSStyleRule, CssSelectorSpecificity> matchedRules = getMatchedRules(matchableTag);
		Collection<CSSDeclaration> declarations = mergeRules(matchedRules);
		return buildString(declarations);
	}

	private Map<CSSStyleRule, CssSelectorSpecificity> getMatchedRules(PhlocCssMatchableComponentTag matchableTag) {
		Map<CSSStyleRule, CssSelectorSpecificity> matchedRules = Maps.newHashMap();
		for (CSSStyleRule rule : styleSheet.getAllStyleRules()) {
			Collection<CSSSelector> matchedSelectors = getMatchedSelectors(matchableTag, rule);
			Collection<CssSelectorSpecificity> matchedSelectorsSpecificities = Collections2.transform(matchedSelectors, new Function<CSSSelector, CssSelectorSpecificity>() {
				@Override
				public CssSelectorSpecificity apply(CSSSelector input) {
					return computeSpecificity(input);
				}
			});
			if (!matchedSelectorsSpecificities.isEmpty()) {
				matchedRules.put(rule, Ordering.natural().max(matchedSelectorsSpecificities));
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

	private Collection<CSSDeclaration> mergeRules(Map<CSSStyleRule, CssSelectorSpecificity> matchedRules) {
		// Maps property names to the elected CSS declaration and its precedence
		
		Map<String, Pair<CSSDeclaration, CssDeclarationPrecedence>> mergedDeclarations = Maps.newTreeMap(CSS_PROPERTY_NAME_COLLATOR);
		
		for (Map.Entry<CSSStyleRule, CssSelectorSpecificity> entry : matchedRules.entrySet()) {
			final CssSelectorSpecificity specificity = entry.getValue();
			
			// Merge declarations if the score is high enough
			for (CSSDeclaration currentDeclaration : entry.getKey().getAllDeclarations()) {
				String propertyName = currentDeclaration.getProperty();
				CssDeclarationPrecedence currentDeclarationPrecedence = new CssDeclarationPrecedence(currentDeclaration.isImportant(), specificity);
				
				Pair<CSSDeclaration, CssDeclarationPrecedence> electedDeclaration = mergedDeclarations.get(propertyName);
				if (electedDeclaration == null || electedDeclaration.getRight().compareTo(currentDeclarationPrecedence) <= 0) {
					mergedDeclarations.put(propertyName, Pair.of(currentDeclaration, currentDeclarationPrecedence));
				}
			}
		}
		
		return Collections2.transform(mergedDeclarations.values(), new Function<Pair<CSSDeclaration, CssDeclarationPrecedence>, CSSDeclaration>() {
			@Override
			public CSSDeclaration apply(Pair<CSSDeclaration, CssDeclarationPrecedence> input) {
				return input.getLeft();
			}
		});
	}
	
	private CssSelectorSpecificity computeSpecificity(CSSSelector selector) {
		int idSelectors = 0;
		int classAndPseudoClassSelectors = 0;
		int typeSelectorsAndPseudoElements = 0;
		
		for (ICSSSelectorMember member : selector.getAllMembers()) {
			if (member instanceof CSSSelectorSimpleMember && !((CSSSelectorSimpleMember) member).isPseudo()) {
				CSSSelectorSimpleMember simpleMember = (CSSSelectorSimpleMember) member;
				if (simpleMember.isClass()) {
					++classAndPseudoClassSelectors;
				} else if (simpleMember.isElementName()) {
					++typeSelectorsAndPseudoElements;
				} else if (simpleMember.isHash()) {
					++idSelectors;
				} else {
					throw new IllegalStateException("Unexpected type for simple selector member " + simpleMember);
				}
			} else {
				throw new IllegalStateException("Unexpected type for selector member " + member);
			}
		}
		
		return new CssSelectorSpecificity(idSelectors, classAndPseudoClassSelectors, typeSelectorsAndPseudoElements);
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
