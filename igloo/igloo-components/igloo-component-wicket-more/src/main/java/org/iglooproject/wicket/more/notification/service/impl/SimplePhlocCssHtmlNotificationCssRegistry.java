package org.iglooproject.wicket.more.notification.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.helger.css.CCSS;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSSelectorSimpleMember;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSSelectorMember;
import com.helger.css.writer.CSSWriterSettings;
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.ComponentTag;
import org.iglooproject.commons.util.ordering.SerializableCollator;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;
import org.jsoup.nodes.Node;
import org.springframework.util.Assert;

/**
 * A simple IHtmlNotificationCssRegistry using Phloc CSS.
 *
 * <p>This registry does <strong>not</strong> optimize selector matching at all : each time
 * getStyle() is called, the component tags are compared to every single selector in the stylesheet.
 */
@Deprecated
public class SimplePhlocCssHtmlNotificationCssRegistry implements IHtmlNotificationCssRegistry {

  private static final Comparator<String> CSS_PROPERTY_NAME_COLLATOR;

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

  public SimplePhlocCssHtmlNotificationCssRegistry(CascadingStyleSheet styleSheet) {
    super();
    Assert.notNull(
        styleSheet, "[Assertion failed] - this argument is required; it must not be null");
    this.styleSheet = styleSheet;
  }

  @Override
  public String getStyle(ComponentTag tag) {
    return getStyle(
        new PhlocCssMatchableHtmlTag(tag.getName(), tag.getId(), tag.getAttribute("class")));
  }

  @Override
  public String getStyle(Node node) {
    return getStyle(
        new PhlocCssMatchableHtmlTag(node.nodeName(), node.attr("id"), node.attr("class")));
  }

  private String getStyle(PhlocCssMatchableHtmlTag matchableTag) {
    Map<CSSStyleRule, CssSelectorSpecificity> matchedRules = getMatchedRules(matchableTag);
    Collection<CSSDeclaration> declarations = mergeRules(matchedRules);
    return buildString(declarations);
  }

  private Map<CSSStyleRule, CssSelectorSpecificity> getMatchedRules(
      PhlocCssMatchableHtmlTag matchableTag) {
    Map<CSSStyleRule, CssSelectorSpecificity> matchedRules = Maps.newHashMap();
    for (CSSStyleRule rule : styleSheet.getAllStyleRules()) {
      Collection<CSSSelector> matchedSelectors = getMatchedSelectors(matchableTag, rule);
      Collection<CssSelectorSpecificity> matchedSelectorsSpecificities =
          matchedSelectors.stream()
              .map(input -> computeSpecificity(input))
              .collect(Collectors.toList());
      if (!matchedSelectorsSpecificities.isEmpty()) {
        matchedRules.put(rule, Ordering.natural().max(matchedSelectorsSpecificities));
      }
    }
    return matchedRules;
  }

  private Collection<CSSSelector> getMatchedSelectors(
      PhlocCssMatchableHtmlTag matchableTag, CSSStyleRule rule) {
    List<CSSSelector> matchedSelectors = Lists.newArrayList();
    for (CSSSelector selector : rule.getAllSelectors()) {
      if (matchableTag.matches(selector)) {
        matchedSelectors.add(selector);
      }
    }
    return matchedSelectors;
  }

  private Collection<CSSDeclaration> mergeRules(
      Map<CSSStyleRule, CssSelectorSpecificity> matchedRules) {
    // Maps property names to the elected CSS declaration and its precedence

    Map<String, Pair<CSSDeclaration, CssDeclarationPrecedence>> mergedDeclarations =
        Maps.newTreeMap(CSS_PROPERTY_NAME_COLLATOR);

    for (Map.Entry<CSSStyleRule, CssSelectorSpecificity> entry : matchedRules.entrySet()) {
      final CssSelectorSpecificity specificity = entry.getValue();

      // Merge declarations if the score is high enough
      for (CSSDeclaration currentDeclaration : entry.getKey().getAllDeclarations()) {
        String propertyName = currentDeclaration.getProperty();
        CssDeclarationPrecedence currentDeclarationPrecedence =
            new CssDeclarationPrecedence(currentDeclaration.isImportant(), specificity);

        Pair<CSSDeclaration, CssDeclarationPrecedence> electedDeclaration =
            mergedDeclarations.get(propertyName);
        if (electedDeclaration == null
            || electedDeclaration.getRight().compareTo(currentDeclarationPrecedence) <= 0) {
          mergedDeclarations.put(
              propertyName, Pair.of(currentDeclaration, currentDeclarationPrecedence));
        }
      }
    }

    return mergedDeclarations.values().stream()
        .map(input -> input.getLeft())
        .collect(Collectors.toList());
  }

  private CssSelectorSpecificity computeSpecificity(CSSSelector selector) {
    int idSelectors = 0;
    int classAndPseudoClassSelectors = 0;
    int typeSelectorsAndPseudoElements = 0;

    for (ICSSSelectorMember member : selector.getAllMembers()) {
      if (member instanceof CSSSelectorSimpleMember
          && !((CSSSelectorSimpleMember) member).isPseudo()) {
        CSSSelectorSimpleMember simpleMember = (CSSSelectorSimpleMember) member;
        if (simpleMember.isClass()) {
          ++classAndPseudoClassSelectors;
        } else if (simpleMember.isElementName()) {
          ++typeSelectorsAndPseudoElements;
        } else if (simpleMember.isHash()) {
          ++idSelectors;
        } else {
          throw new IllegalStateException(
              "Unexpected type for simple selector member " + simpleMember);
        }
      } else {
        throw new IllegalStateException("Unexpected type for selector member " + member);
      }
    }

    return new CssSelectorSpecificity(
        idSelectors, classAndPseudoClassSelectors, typeSelectorsAndPseudoElements);
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
